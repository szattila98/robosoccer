import { Injectable } from '@angular/core';
import { Ball } from '../core/models/ball';
import { Side } from '../core/models/enum/side';
import { Match } from '../core/models/match';
import { Player } from '../core/models/player';
import { Position } from '../core/models/position';
import { User } from '../core/models/user';
import { SocketService } from '../core/services/socket.service';
import { GeometryService } from './geometry.service';

const FIELD_X_MAX = 140;
const FIELD_Y_MAX = 100;
const CHECK_RADIUS = 20;
const DISABLED_DISTANCE = 5;
const DESTINATION_TOLERANCE = 0.4;

interface PlayerMovement {
  player: Player;
  start: Position;
  destination: Position;
}

interface Distance {
  player: Player;
  distance: number;
}

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  lastLeft: User;
  lastRight: User;
  lastBall: Ball;

  disabledMovements: PlayerMovement[] = [];

  constructor(
    private socketService: SocketService,
    private geometryService: GeometryService) { }

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

  private isNewRound(leftUser: User, rightUser: User): boolean {
    return !this.lastLeft || !this.lastRight || !this.lastBall
      || leftUser.points !== this.lastLeft.points || rightUser.points !== this.lastRight.points;
  }

  private isDisabledPlayerMovedEnough(player: Player): boolean {
    const disabled = this.disabledMovements.find(element => element.player.id === player.id);
    return disabled && this.geometryService.getDistance(disabled.start, player.position) >= DISABLED_DISTANCE;
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
        destination
      });
      this.socketService.sendMoveCommand({ playerId: player.id, destination });
    }
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

  sendCommands(match: Match, mySide: Side): void {
    const newLeft = this.getUserBySide(match, Side.LEFT);
    const newRight = this.getUserBySide(match, Side.RIGHT);
    const newBall = match.ball;

    if (this.isNewRound(newLeft, newRight)) {
      this.disabledMovements = [];
      this.saveNewState(newLeft, newRight, newBall);
      return;
    }

    this.unblockPlayers(Side.LEFT.valueOf() === mySide.valueOf() ? newLeft : newRight);

    if (this.iHaveBall(newBall, mySide)) {
      console.log('I HAVE THE BALL');
      // félkör, CHECK_RADIUS sugárral -> 4 körcikk
      // melyik körcikkben mennyi saját és mennyi other player van
      // ha van olyan, ahol senki nincs, akkor arra indul CHECK_RADIUS távolságra
      // ha több olyan van, ahol senki nincs, akkor sorsol

      // ha valamelyikben csak saját játékos van, felé passzol
      // ha többen is, sorsol

      // ha 2-nél több other player van mindegyikben, elindul a saját kapu fele v. passzol
    }

    if (this.otherTeamHasBall(newBall, mySide)) {
      const myUser: User = this.getUserBySide(match, mySide);
      const playersNearBall = this.getPlayersNearBall(myUser, newBall);
      playersNearBall.forEach(player => this.movePlayer(player, newBall.position));
    }

    if (this.nobodyHasBall(newBall)) {
      const myUser: User = this.getUserBySide(match, mySide);
      const distances: Distance[] = this.sortPlayersByDistance(myUser.team, newBall.position);
      const playerToRun = distances[0].player; // closest player to ball
      this.movePlayer(playerToRun, newBall.position);
    }

    // TODO: every player in team: go to start position when ball is not inside CHECK_RADIUS
    // TODO: use ball movement direction instead of just the current position

    this.saveNewState(newLeft, newRight, newBall);
  }
}
