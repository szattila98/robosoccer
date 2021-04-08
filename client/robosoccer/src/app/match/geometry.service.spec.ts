
import { Side } from '../core/models/enum/side';
import { Position } from '../core/models/position';
import { GeometryService, Sector } from './geometry.service';

fdescribe('GeometryService', () => {
  let service: GeometryService;

  const R = 5;
  const center: Position = { x: 0.1, y: 0.1 };

  const floatingPointPrecisionDigits = 2;

  beforeEach(() => {
    service = new GeometryService();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('#isInsideSector should calculate that point is inside a sector (right side, 1)', () => {
    const actual = service.isInsideSector({ x: 1, y: 3 }, center, { x: 0, y: 5 }, { x: 3.54, y: 3.54 }, R);
    expect(actual).toBeTrue();
  });

  it('#isInsideSector should calculate that point is inside a sector (right side, 2)', () => {
    const actual = service.isInsideSector({ x: 2.41, y: 1.17 }, center, { x: 3.54, y: 3.54 }, { x: 5, y: 0 }, R);
    expect(actual).toBeTrue();
  });

  it('#isInsideSector should calculate that point is inside a sector (left side 1)', () => {
    const actual = service.isInsideSector({ x: -3.17, y: 1.39 }, center, { x: -5, y: 0 }, { x: -3.54, y: 3.54 }, R);
    expect(actual).toBeTrue();
  });

  it('#isInsideSector should calculate that point is inside a sector (left side 2)', () => {
    const actual = service.isInsideSector({ x: -1.33, y: 3.3 }, center, { x: -3.54, y: 3.54 }, { x: 5, y: 0 }, R);
    expect(actual).toBeTrue();
  });

  it('#isInsideSector should calculate that point is not inside a sector (right side 1)', () => {
    const actual = service.isInsideSector({ x: 2.75, y: 4.4 }, center, { x: 0, y: 5 }, { x: 3.54, y: 3.54 }, R);
    expect(actual).toBeFalse();
  });

  it('#isInsideSector should calculate that point is not inside a sector (right side 2)', () => {
    const actual = service.isInsideSector({ x: 2.75, y: 4.4 }, center, { x: 3.54, y: 3.54 }, { x: 5, y: 0 }, R);
    expect(actual).toBeFalse();
  });

  it('#isInsideSector should calculate that point is not inside a sector (left side 1)', () => {
    const actual = service.isInsideSector({ x: -5.8, y: 2.98 }, center, { x: -5, y: 0 }, { x: -3.54, y: 3.54 }, R);
    expect(actual).toBeFalse();
  });

  it('#isInsideSector should calculate that point is not inside a sector (left side 2)', () => {
    const actual = service.isInsideSector({ x: -3.17, y: 1.39 }, center, { x: -3.54, y: 3.54 }, { x: 5, y: 0 }, R);
    expect(actual).toBeFalse();
  });

  it('#getMidPoint should calculate midpoint of two points', () => {
    const actual = service.getMidPoint({ x: 0, y: 0 }, { x: 3, y: 3 });
    expect(actual.x).toBeCloseTo(1.5, floatingPointPrecisionDigits);
    expect(actual.y).toBeCloseTo(1.5, floatingPointPrecisionDigits);
  });

  it('#getSectors should calculate sectors for circle\'s right side', () => {
    const circleCenter: Position = { x: 2.2, y: 2.2 };
    const expectedSectors: Sector[] = [
      {
        sectorStart: { x: 2.2, y: 7.2 },
        sectorEnd: { x: 5.74, y: 5.74 }
      },
      {
        sectorStart: { x: 5.74, y: 5.74 },
        sectorEnd: { x: 7.2, y: 2.2 }
      },
      {
        sectorStart: { x: 7.2, y: 2.2 },
        sectorEnd: { x: 5.74, y: -1.34 }
      },
      {
        sectorStart: { x: 5.74, y: -1.34 },
        sectorEnd: { x: 2.2, y: -2.8 }
      }
    ];

    const actualSectors = service.getSectors(circleCenter, R, Side.RIGHT, 4);

    expect(actualSectors).toHaveSize(expectedSectors.length);

    for (let i = 0; i < actualSectors.length; i++) {
      expect(actualSectors[i].sectorStart.x).toBeCloseTo(expectedSectors[i].sectorStart.x, floatingPointPrecisionDigits);
      expect(actualSectors[i].sectorStart.y).toBeCloseTo(expectedSectors[i].sectorStart.y, floatingPointPrecisionDigits);
      expect(actualSectors[i].sectorEnd.x).toBeCloseTo(expectedSectors[i].sectorEnd.x, floatingPointPrecisionDigits);
      expect(actualSectors[i].sectorEnd.y).toBeCloseTo(expectedSectors[i].sectorEnd.y, floatingPointPrecisionDigits);
    }
  });

  it('#getSectors should calculate sectors for circle\'s left side', () => {
    const circleCenter: Position = { x: 2.2, y: 2.2 };
    const expectedSectors: Sector[] = [
      {
        sectorStart: { x: 2.2, y: -2.8 },
        sectorEnd: { x: -1.34, y: -1.34 }
      },
      {
        sectorStart: { x: -1.34, y: -1.34 },
        sectorEnd: { x: -2.8, y: 2.2 }
      },
      {
        sectorStart: { x: -2.8, y: 2.2 },
        sectorEnd: { x: -1.34, y: 5.74 }
      },
      {
        sectorStart: { x: -1.34, y: 5.74 },
        sectorEnd: { x: 2.2, y: 7.2 }
      }
    ];

    const actualSectors = service.getSectors(circleCenter, R, Side.LEFT, 4);

    expect(actualSectors).toHaveSize(expectedSectors.length);

    for (let i = 0; i < expectedSectors.length; i++) {
      expect(actualSectors[i].sectorStart.x).toBeCloseTo(expectedSectors[i].sectorStart.x, floatingPointPrecisionDigits);
      expect(actualSectors[i].sectorStart.y).toBeCloseTo(expectedSectors[i].sectorStart.y, floatingPointPrecisionDigits);
      expect(actualSectors[i].sectorEnd.x).toBeCloseTo(expectedSectors[i].sectorEnd.x, floatingPointPrecisionDigits);
      expect(actualSectors[i].sectorEnd.y).toBeCloseTo(expectedSectors[i].sectorEnd.y, floatingPointPrecisionDigits);
    }
  });
});
