import { Side } from "./enum/side";
import { Player } from "./player";

export interface User {
    sessionId: string;
    name: string;
    side: Side;
    team: Player[];
    points: number;
    ready: boolean;
}
