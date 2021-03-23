import { Position } from '../position';

export interface MoveCommand {
    playerId: number;
    destination: Position;
}
