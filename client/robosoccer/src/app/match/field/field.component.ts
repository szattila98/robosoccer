import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { StompSubscription } from '@stomp/stompjs';
import { Ball } from 'src/app/core/models/ball';
import { Side } from 'src/app/core/models/enum/side';
import { Match } from 'src/app/core/models/match';
import { Player } from 'src/app/core/models/player';
import { User } from 'src/app/core/models/user';
import { SessionStorageService } from 'src/app/core/services/session-storage.service';
import { SocketService } from 'src/app/core/services/socket.service';
import { StrategyService } from '../strategy.service';

const FIELD_IMG_RATIO = 2873 / 1844;
const FIELD_X_MAX = 140;
const FIELD_Y_MAX = 100;

let BALL_RADIUS_PX = window.innerWidth > 1000 ? 7 : 5;
let PLAYER_RADIUS_PX = window.innerWidth > 1000 ? 10 : 7;

const COLOR_BALL = 'red';
const COLOR_LEFT_SIDE = 'purple';
const COLOR_RIGHT_SIDE = 'orange';

@Component({
  selector: 'app-field',
  templateUrl: './field.component.html',
  styleUrls: ['./field.component.css']
})
export class FieldComponent implements AfterViewInit {

  @ViewChild('football_field', { static: false })
  canvas: ElementRef<HTMLCanvasElement>;

  context: CanvasRenderingContext2D;

  sessionId: string;

  socketSubscription: StompSubscription;

  match: Match;

  _debug = true;
  _selectedPlayer: Player;
  _clickTimer;

  constructor(
    private router: Router,
    private socketService: SocketService,
    private sessionStorageService: SessionStorageService,
    private strategyService: StrategyService) { }

  ngAfterViewInit(): void {
    this.setCanvasSize();
    window.addEventListener('resize', () => this.setCanvasSize());

    this.context = this.canvas.nativeElement.getContext('2d');

    this.sessionId = this.sessionStorageService.getSession().sessionId;

    try {
      this.socketSubscription = this.socketService.subscribeToGame((msg) => this.onMatchStateUpdated(msg));
      this.socketService.sendReadyState();
    } catch (err) {
      console.error(err);
      this.router.navigateByUrl('/error');
    }
  }

  get canShowResults(): boolean {
    return this.match && Array.isArray(this.match.users) && this.match.users.length === 2;
  }

  get canvasWidth(): number {
    return this.canvas.nativeElement.width;
  }

  get canvasHeight(): number {
    return this.canvas.nativeElement.height;
  }

  getCanvasPointX(x: number): number {
    return Math.floor(this.canvasWidth / FIELD_X_MAX * x);
  }

  getCanvasPointY(y: number): number {
    return Math.floor(this.canvasHeight / FIELD_Y_MAX * y);
  }

  clearCanvas(): void {
    this.context.clearRect(0, 0, this.canvasWidth, this.canvasHeight);
  }

  drawBall(ball: Ball): void {
    this.context.beginPath();
    this.context.arc(this.getCanvasPointX(ball.position.x), this.getCanvasPointY(ball.position.y),
      BALL_RADIUS_PX, 0, 2 * Math.PI);
    this.context.fillStyle = COLOR_BALL;
    this.context.fill();
  }

  drawTeam(team: Player[], side: Side): void {
    for (const player of team) {
      this.context.beginPath();
      this.context.arc(this.getCanvasPointX(player.position.x), this.getCanvasPointY(player.position.y),
        PLAYER_RADIUS_PX, 0, 2 * Math.PI);
      this.context.fillStyle = side === Side.LEFT ? COLOR_LEFT_SIDE : COLOR_RIGHT_SIDE;
      this.context.fill();
    }
  }

  updateField(): void {
    this.clearCanvas();
    this.drawTeam(this.match.users[0].team, Side.LEFT);
    this.drawTeam(this.match.users[1].team, Side.RIGHT);
    this.drawBall(this.match.ball);
  }

  onMatchStateUpdated(message): void {
    try {
      const body = JSON.parse(message.body);
      this.match = body.match;
      this.updateField();
    } catch (err) {
      console.error(err);
      this.router.navigateByUrl('/error');
    }
  }

  setCanvasSize(): void {
    BALL_RADIUS_PX = window.innerWidth > 1000 ? 7 : 5;
    PLAYER_RADIUS_PX = window.innerWidth > 1000 ? 10 : 7;
    const FIELD_MARGIN_PX = Math.floor(window.innerWidth / 2.5);
    this.canvas.nativeElement.width = window.innerWidth - FIELD_MARGIN_PX;
    this.canvas.nativeElement.height = Math.floor((window.innerWidth - FIELD_MARGIN_PX) / FIELD_IMG_RATIO);
  }






  //// Debug functions

  _getFieldPointX(canvasX: number) {
    const rect = this.canvas.nativeElement.getBoundingClientRect();
    canvasX -= rect.left;
    return canvasX / this.canvasWidth * FIELD_X_MAX;
  }

  _getFieldPointY(canvasY: number) {
    const rect = this.canvas.nativeElement.getBoundingClientRect();
    canvasY -= rect.top;
    return canvasY / this.canvasHeight * FIELD_Y_MAX;
  }

  _getUserBySide(side: Side): User {
    for (const user of this.match.users) {
      if (user.side === side) {
        return user;
      }
    }
  }

  _getNearestPlayer(clickX: number, clickY: number) {
    const side = this.sessionStorageService.getSession().side;
    const user = this._getUserBySide(side);
    const distances: { player: Player, distance: number }[] = [];

    for (const player of user.team) {
      distances.push({
        player,
        distance: Math.sqrt(
          Math.pow(player.position.x - this._getFieldPointX(clickX), 2)
          + Math.pow(player.position.y - this._getFieldPointY(clickY), 2))
      });
    }

    distances.sort((a, b) => a.distance - b.distance);
    return distances[0].player;
  }

  _handleSingleClick(event) {
    const canvasX = event.clientX;
    const canvasY = event.clientY;

    if (!this._selectedPlayer) {
      this._selectedPlayer = this._getNearestPlayer(canvasX, canvasY);
      return;
    }

    this.socketService.sendMoveCommand({
      playerId: this._selectedPlayer.id,
      destination: {
        x: this._getFieldPointX(canvasX),
        y: this._getFieldPointY(canvasY)
      }
    });
    this._selectedPlayer = null;
  }

  _handleKick(x: number, y: number) {
    this.socketService.sendKickCommand({
      destination: { x, y },
      forceOfKick: 1.0
    });
  }

  _handleDoubleClick(event) {
    clearTimeout(this._clickTimer);
    const x = this._getFieldPointX(event.clientX);
    const y = this._getFieldPointY(event.clientY);
    this._handleKick(x, y);
  }

  _handleClick(event) {
    if (event.detail === 1) {
      this._clickTimer = setTimeout(() => {
        this._handleSingleClick(event);
      }, 200);
    }
  }


}
