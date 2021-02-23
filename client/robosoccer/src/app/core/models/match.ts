import { Ball } from './ball';
import { RoundStatus } from './enum/round-status';
import { User } from './user';

export interface Match {
    users: User[];
    ball: Ball;
    roundNo: number;
    roundStatusType: RoundStatus;
}
