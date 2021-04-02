import { Injectable } from '@angular/core';
import { Side } from '../core/models/enum/side';
import { Position } from '../core/models/position';

/*
 * 2 plus 2 is 4 minus 1 is 3 ... quick math!
 */

export interface Sector {
  sectorStart: Position;
  sectorEnd: Position;
}

@Injectable({
  providedIn: 'root'
})
export class GeometryService {

  constructor() { }

  /**
   * Checks if the given point is inside the given circle's given sector.
   * @author Oren Trutner (https://stackoverflow.com/a/13675772/8691998)
   */
  public isInsideSector(point: Position, center: Position, sectorEnd: Position, sectorStart: Position, radius: number): boolean {
    const radiusSquared = radius * radius;
    const relPoint = {
      x: point.x - center.x,
      y: point.y - center.y
    };

    return !this.areClockwise(sectorStart, relPoint) &&
      this.areClockwise(sectorEnd, relPoint) &&
      this.isWithinRadius(relPoint, radiusSquared);
  }

  public getSectors(point: Position, radius: number, half: Side, sectorCount: number = 4): Sector[] {
    if (!sectorCount) {
      return [];
    }

    const delta = Math.PI / 2;
    const radiansPerSector = Math.PI / sectorCount; // 2 * Math.PI / 2 * sectorCount, as we work with a half circle
    const startRadians = (half.valueOf() === Side.RIGHT.valueOf() ? 0 : Math.PI) + delta;
    const sectors: Sector[] = [];

    for (let i = 0; i < sectorCount; i++) {
      // x_0 + r * cos(theta), y_0 + r * sin(theta)
      sectors.push({
        sectorStart: {
          x: point.x + radius * Math.cos(startRadians - i * radiansPerSector),
          y: point.y + radius * Math.sin(startRadians - i * radiansPerSector)
        },
        sectorEnd: {
          x: point.x + radius * Math.cos(startRadians - (i + 1) * radiansPerSector),
          y: point.y + radius * Math.sin(startRadians - (i + 1) * radiansPerSector)
        }
      });
    }

    return sectors;
  }

  public getMidPoint(pointA: Position, pointB: Position): Position {
    return {
      x: (pointA.x + pointB.x) / 2,
      y: (pointA.y + pointB.y) / 2
    };
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
