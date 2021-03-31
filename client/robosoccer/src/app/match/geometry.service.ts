import { Injectable } from '@angular/core';
import { Side } from '../core/models/enum/side';
import { Position } from '../core/models/position';

/*
 * 2 plus 2 is 4 minus 1 is 3 ... quick math!
 */

@Injectable({
  providedIn: 'root'
})
export class GeometryService {

  constructor() { }

  // https://stackoverflow.com/a/13675772/8691998
  isInsideSector(point: Position, center: Position, sectorStart: Position, sectorEnd: Position, radius: number): boolean {
    // center: actual player
    // point: other player
    // sectorStart, sectorEnd

    const radiusSquared = radius * radius;
    const relPoint = {
      x: point.x - center.x,
      y: point.y - center.y
    };

    return !this.areClockwise(sectorStart, relPoint) &&
      this.areClockwise(sectorEnd, relPoint) &&
      this.isWithinRadius(relPoint, radiusSquared);
  }

  public getHalfCircleSectorBoundaries(point: Position, radius: number, half: Side,
    sectorCount: number = 4): Position[] {
    // TODO
    return [{ x: 0, y: 0 }];
  }

  public getDistance(pointA: Position, pointB: Position): number {
    return Math.sqrt(Math.pow(pointA.x - pointB.x, 2) + Math.pow(pointA.y - pointB.y, 2));
  }

  private areClockwise(v1: Position, v2: Position): boolean {
    return -v1.x * v2.y + v1.y * v2.x > 0;
  }

  private isWithinRadius(v: Position, radiusSquared: number): boolean {
    return v.x * v.x + v.y * v.y <= radiusSquared;
  }
}
