import { Player } from './player';
import { Position } from './position';

export interface Ball {
    player: Player;
    position: Position;
}
