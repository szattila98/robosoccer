import { Side } from './enum/side';
import { Position } from './position';

export interface Player {
    id: number;
    side: Side;
    position: Position;
}
