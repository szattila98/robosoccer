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

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  lastLeft: User;
  lastRight: User;
  lastBall: Ball;

  disabledPlayers: { player: Player, position: Position }[] = [];
  // TODO: a player addig legyen disabled, amíg el nem mozdult X távolságra a rögzített kezdőpozíciójától

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

  // private reachedDestination(player: Player, position: Position): boolean {
  //   const user = player.side.valueOf() === Side.LEFT.valueOf() ? this.lastLeft : this.lastRight;
  //   const team = user.team;
  //   const disabledPlayer = team.find(oldPlayer => oldPlayer.id === player.id);

  //   if (!disabledPlayer) {
  //     return true;
  //   }

  //   return disabledPlayer.position.x === player.position.x && disabledPlayer.position.y === player.position.y;
  // }

  private isPlayerDisabled(player: Player): boolean {
    for (const disabledPlayer of this.disabledPlayers) {
      if (disabledPlayer.player.id === player.id && disabledPlayer.player.side.valueOf() === player.side.valueOf()) {
        return true;
      }
    }

    return false;
  }

  private isNewRound(leftUser: User, rightUser: User): boolean {
    return !this.lastLeft || !this.lastRight || !this.lastBall
    || leftUser.points !== this.lastLeft.points || rightUser.points !== this.lastRight.points;
  }

  sendCommands(match: Match, mySide: Side): void {
    const newLeft = this.getUserBySide(match, Side.LEFT);
    const newRight = this.getUserBySide(match, Side.RIGHT);
    const newBall = match.ball;

    if (this.isNewRound(newLeft, newRight)) {
      this.disabledPlayers = [];
      this.saveNewState(newLeft, newRight, newBall);
      return;
    }

    // for (const i in this.disabledPlayers) {
    //   if (this.reachedDestination(this.disabledPlayers[i].player)) {

    //   }
    // }

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
      console.log('OTHER TEAM HAS THE BALL');

      // melyik játékos(ok) van(nak) a legközelebb hozzá (elindul(nak) a labda pozíciójába)
      // több játékos: pl ha CHECK_RADIUS-on belül többen vannak
    }

    if (this.nobodyHasBall(newBall)) {
      const myUser: User = this.getUserBySide(match, mySide);
      const distances: { player: Player, distance: number }[] = [];

      for (const player of myUser.team) {
        distances.push({
          player,
          distance: this.geometryService.getDistance(player.position, newBall.position)
        });
      }

      distances.sort((a, b) => a.distance - b.distance);
      const playerToRun = distances[0].player;

      if (!this.isPlayerDisabled(playerToRun)) {
        this.disabledPlayers.push({ player: playerToRun, position: playerToRun.position });
        this.socketService.sendMoveCommand({ playerId: playerToRun.id, destination: newBall.position });
      }
    }

    this.saveNewState(newLeft, newRight, newBall);
  }
}
