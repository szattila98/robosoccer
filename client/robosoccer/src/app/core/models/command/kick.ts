import { Position } from '../position';

export interface KickCommand {
    destination: Position;
    forceOfKick: number;
}
