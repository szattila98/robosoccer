import { Injectable } from '@angular/core';
import { Ball } from '../core/models/ball';
import { Side } from '../core/models/enum/side';
import { Match } from '../core/models/match';
import { User } from '../core/models/user';
import { SocketService } from '../core/services/socket.service';

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

  constructor(private socketService: SocketService) { }

  private getUserBySide(match: Match, side: Side): User {
    for (const user of match.users) {
      if (user.side === Side[side.toString()]) {
        return user;
      }
    }
  }

  saveNewState(left: User, right: User, ball: Ball): void {
    this.lastLeft = left;
    this.lastRight = right;
    this.lastBall = ball;
  }

  isFirstStepOfRound(left, right): boolean {
    return (!this.lastLeft || !this.lastRight)
      || left.points !== this.lastLeft.points
      || right.points !== this.lastRight.points;
  }

  sendCommands(match: Match, mySide: Side): void {
    const newLeft = this.getUserBySide(match, Side.LEFT);
    const newRight = this.getUserBySide(match, Side.RIGHT);
    const newBall = match.ball;

    if (this.isFirstStepOfRound(newLeft, newRight)) {
      this.socketService.sendMoveCommand({
        playerId: 6,
        destination: {
          x: mySide.toString() === Side[Side.LEFT] ? FIELD_X_MAX : 0,
          y: FIELD_Y_MAX / 2
        }
      });
      return this.saveNewState(newLeft, newRight, newBall);
    }

    if (newBall.player?.side.toString() === mySide.toString()) {
      console.log('I HAVE THE BALL');
    }

    if (!newBall.player) {
      console.log('NO ONE HAS THE BALL');
    }

    this.saveNewState(newLeft, newRight, newBall);
  }
}
