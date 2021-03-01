import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Ball } from 'src/app/core/models/ball';
import { Side } from 'src/app/core/models/enum/side';
import { Match } from 'src/app/core/models/match';
import { Player } from 'src/app/core/models/player';
import { SocketService } from 'src/app/core/services/socket.service';
import { FieldService } from './field.service';

// TODO: config file or object

const FIELD_IMG_RATIO = 2873 / 1844;
const FIELD_MARGIN_PX = Math.floor(window.innerWidth / 3);
const FIELD_X_MAX = 140;
const FIELD_Y_MAX = 100;

const BALL_RADIUS_PX = window.innerWidth > 1000 ? 7 : 5;
const PLAYER_RADIUS_PX = window.innerWidth > 1000 ? 10 : 7;

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

  match: Match;

  constructor(
    private fieldService: FieldService,
    private router: Router,
    private socketService: SocketService) { }

  async ngAfterViewInit(): Promise<any> {
    this.canvas.nativeElement.width = window.innerWidth - FIELD_MARGIN_PX;
    this.canvas.nativeElement.height = Math.floor((window.innerWidth - FIELD_MARGIN_PX) / FIELD_IMG_RATIO);

    this.context = this.canvas.nativeElement.getContext('2d');

    try {
      this.match = await this.fieldService.getMatchData();
    } catch (err) {
      this.router.navigateByUrl('/error');
    }

    this.drawBall(this.match.ball);
    this.drawTeam(this.match.users[0].team, Side.LEFT);
    this.drawTeam(this.match.users[1].team, Side.RIGHT);
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

}
