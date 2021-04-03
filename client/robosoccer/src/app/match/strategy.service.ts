import { Injectable } from '@angular/core';
import { Ball } from '../core/models/ball';
import { Side } from '../core/models/enum/side';
import { Match } from '../core/models/match';
import { Player } from '../core/models/player';
import { Position } from '../core/models/position';
import { User } from '../core/models/user';
import { SocketService } from '../core/services/socket.service';
import { GeometryService, Sector } from './geometry.service';

const FIELD_X_MAX = 140;
const FIELD_Y_MAX = 100;
const CHECK_RADIUS = 30;
const DISABLED_FRAMES = 8;
const DESTINATION_TOLERANCE = 0.8;
const ATTACK_SECTOR_COUNT = 6;

interface PlayerMovement {
  player: Player;
  start: Position;
  startFrame: number;
  destination: Position;
}

interface Distance {
  player: Player;
  distance: number;
}

interface DecisionData {
  sector: Sector;
  teammates: Player[];
  opponents: Player[];
}

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  startPositionsLeft: Player[];
  startPositionsRight: Player[];

  lastLeft: User;
  lastRight: User;
  lastBall: Ball;

  disabledMovements: PlayerMovement[] = [];

  currentFrame: number = 1;

  constructor(
    private socketService: SocketService,
    private geometryService: GeometryService) { }

  get frame() {
    return this.currentFrame;
  }

  private getUserBySide(match: Match, side: Side): User {
    for (const user of match.users) {
      if (user.side.valueOf() === side.valueOf()) {
        return user;
      }
    }
  }

  private saveNewState(left: User, right: User, ball: Ball): void {
    this.lastLeft = left;
    this.lastRight = right;
    this.lastBall = ball;
  }

  private iHaveBall(ball: Ball, side: Side): boolean {
    return ball.player?.side.toString() === side.toString();
  }

  private otherTeamHasBall(ball: Ball, side: Side): boolean {
    return ball.player && ball.player.side.toString() !== side.toString();
  }

  private nobodyHasBall(ball: Ball): boolean {
    return !ball.player;
  }

  private reachedDestination(player: Player): boolean {
    for (const disabled of this.disabledMovements) {
      if (disabled.player.id === player.id
        && this.geometryService.getDistance(disabled.destination, player.position) < DESTINATION_TOLERANCE) {
        return true;
      }
    }

    return false;
  }

  private isPlayerDisabled(player: Player): boolean {
    for (const disabled of this.disabledMovements) {
      if (disabled.player.id === player.id) {
        return true;
      }
    }

    return false;
  }

  private isFirstRound(): boolean {
    return !this.lastLeft || !this.lastRight;
  }

  private isNewRound(leftUser: User, rightUser: User): boolean {
    return this.isFirstRound() || !this.lastBall
      || leftUser.points !== this.lastLeft.points || rightUser.points !== this.lastRight.points;
  }

  private isDisabledPlayerMovedEnough(player: Player): boolean {
    const disabled = this.disabledMovements.find(element => element.player.id === player.id);
    return disabled && this.currentFrame - disabled.startFrame >= DISABLED_FRAMES;
  }

  private isNearGate(player: Player): boolean {
    return (player.side.valueOf() === Side.LEFT.valueOf() && player.position.x > FIELD_X_MAX - 30)
      || (player.side.valueOf() === Side.RIGHT.valueOf() && player.position.x < 30);
  }

  private unblockPlayers(myUser: User): void {
    const players = myUser?.team;

    if (!players) {
      return;
    }

    for (const player of players) {
      if (this.isPlayerDisabled(player)
        && (this.reachedDestination(player) || this.isDisabledPlayerMovedEnough(player))) {
        const index = this.disabledMovements.findIndex(disabled => disabled.player.id === player.id);
        this.disabledMovements.splice(index, 1);
      }
    }
  }

  private movePlayer(player: Player, destination: Position): void {
    if (!this.isPlayerDisabled(player)) {
      this.disabledMovements.push({
        player,
        start: player.position,
        startFrame: this.currentFrame,
        destination
      });
      this.socketService.sendMoveCommand({ playerId: player.id, destination });
    }
  }

  private kickBall(player: Player, destination: Position, forceOfKick: number): void {
    this.disabledMovements.push({
      player,
      start: player.position,
      startFrame: this.currentFrame,
      destination
    });
    this.socketService.sendKickCommand({ destination, forceOfKick });
  }

  private getPlayersNearBall(user: User, ball: Ball): Player[] {
    const team = user.team;
    const playersNearBall = [];

    for (const player of team) {
      if (this.geometryService.getDistance(player.position, ball.position) <= CHECK_RADIUS) {
        playersNearBall.push(player);
      }
    }

    return playersNearBall;
  }

  private sortPlayersByDistance(team: Player[], position: Position): Distance[] {
    const distances = [];

    for (const player of team) {
      distances.push({
        player,
        distance: this.geometryService.getDistance(player.position, position)
      });
    }

    return distances.sort((a, b) => a.distance - b.distance);
  }

  private revertSide(side: Side): Side {
    return side.valueOf() === Side.LEFT.valueOf() ? Side.RIGHT : Side.LEFT;
  }

  private getDecisionsWithZeroPlayers(decisions: DecisionData[]): DecisionData[] {
    return decisions.filter(decision => decision.teammates.length === 0 && decision.opponents.length === 0);
  }

  private getDecisionsWithOnlyTeammates(decisions: DecisionData[]): DecisionData[] {
    return decisions.filter(decision => decision.teammates.length > 0 && decision.opponents.length === 0);
  }

  private getDecisionsWithOnlyOpponents(decisions: DecisionData[]): DecisionData[] {
    return decisions.filter(decision => decision.teammates.length === 0 && decision.opponents.length > 0);
  }

  private getStartPosition(playerToSearch: Player): Position {
    const players = playerToSearch.side.valueOf() === Side.LEFT.valueOf() ? this.startPositionsLeft : this.startPositionsRight;
    for (const player of players) {
      if (player.id === playerToSearch.id) {
        return player.position;
      }
    }
  }

  // min included, max excluded
  private randomBetweenUniform(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min)) + min;
  }

  private gaussianRand(): number {
    let rand = 0;
    for (let i = 0; i < 6; i += 1) {
      rand += Math.random();
    }
    return rand / 6;
  }

  // min included, max excluded
  private randomBetweenNormal(min: number, max: number): number {
    return Math.floor(this.gaussianRand() * (max - min)) + min;
  }

  public sendCommands(match: Match, mySide: Side): void {
    this.currentFrame++;

    const newLeft = this.getUserBySide(match, Side.LEFT);
    const newRight = this.getUserBySide(match, Side.RIGHT);
    const newBall = match.ball;

    if (this.isFirstRound()) {
      this.startPositionsLeft = newLeft.team;
      this.startPositionsRight = newRight.team;
    }

    if (this.isNewRound(newLeft, newRight)) {
      this.disabledMovements = [];
      this.saveNewState(newLeft, newRight, newBall);
      return;
    }

    const myUser: User = this.getUserBySide(match, mySide);

    this.unblockPlayers(Side.LEFT.valueOf() === mySide.valueOf() ? newLeft : newRight);

    if (this.iHaveBall(newBall, mySide)) {
      const playerWithBall = newBall.player;
      const otherUser = this.getUserBySide(match, this.revertSide(mySide));

      const attackSectors = this.geometryService.getSectors(
        playerWithBall.position, CHECK_RADIUS, this.revertSide(mySide), ATTACK_SECTOR_COUNT);

      const possibleDecisions: DecisionData[] = attackSectors.map(sector => {
        return {
          sector,
          teammates: myUser.team.filter(player => this.geometryService.isInsideSector(playerWithBall.position,
            player.position, sector.sectorStart, sector.sectorEnd, CHECK_RADIUS)
            && player.id !== playerWithBall.id
            && player.position.x !== playerWithBall.position.x
            && player.position.y !== playerWithBall.position.y),
          opponents: otherUser.team.filter(player => this.geometryService.isInsideSector(playerWithBall.position,
            player.position, sector.sectorStart, sector.sectorEnd, CHECK_RADIUS)),
        };
      });

      const zeroPlayers = this.getDecisionsWithZeroPlayers(possibleDecisions);
      const onlyTeammates = this.getDecisionsWithOnlyTeammates(possibleDecisions);
      const onlyOpponents = this.getDecisionsWithOnlyOpponents(possibleDecisions);

      if (this.isNearGate(playerWithBall)) {
        this.kickBall(playerWithBall,
          mySide.valueOf() === Side.LEFT.valueOf() ? { x: 140, y: 50 } : { x: 0, y: 50 }, 1.5);
      } else if (onlyTeammates.length > 0) {
        const decision = onlyTeammates[this.randomBetweenNormal(0, onlyTeammates.length)];
        const destination = decision.teammates[this.randomBetweenUniform(0, decision.teammates.length)].position;
        this.kickBall(playerWithBall, destination, 1.5);
        // TODO: miért nem passzolnak?
        // TODO: miért passzolnak hátra?
      } else if (zeroPlayers.length > 0) {
        const decision = zeroPlayers[this.randomBetweenNormal(0, zeroPlayers.length)];
        const destination = this.geometryService.getMidPoint(decision.sector.sectorStart, decision.sector.sectorEnd);
        this.movePlayer(playerWithBall, destination);
      } else if (onlyOpponents.length > 0) { // go back home
        const deltaX = mySide.valueOf() === Side.LEFT.valueOf() ? -10 : 10;
        this.movePlayer(playerWithBall, {
          x: playerWithBall.position.x + deltaX,
          y: playerWithBall.position.y
        });
      } else { // default strategy
        this.movePlayer(playerWithBall,
          mySide.valueOf() === Side.LEFT.valueOf() ? { x: 140, y: 50 } : { x: 0, y: 50 });
      }
    }

    if (this.otherTeamHasBall(newBall, mySide)) {
      const playersNearBall = this.getPlayersNearBall(myUser, newBall);
      playersNearBall.forEach(player => this.movePlayer(player, newBall.position));
      // TODO: what to do when no one is inside CHECK_RADIUS?
    }

    if (this.nobodyHasBall(newBall)) {
      const distances: Distance[] = this.sortPlayersByDistance(myUser.team, newBall.position);
      const playerToRun = distances[0].player; // closest player to ball
      this.movePlayer(playerToRun, newBall.position);
    }

    // send back players to their start position
    for (const player of myUser.team) {
      if (this.geometryService.getDistance(player.position, newBall.position) > CHECK_RADIUS) {
        this.movePlayer(player, this.getStartPosition(player));
      }
    }

    // TODO: use ball movement direction instead of just the current position
    // TODO: when players are in the same position for longer time, choose one and move to random direction
    // TODO: why player freezes when catched the ball?

    this.saveNewState(newLeft, newRight, newBall);
  }
}
