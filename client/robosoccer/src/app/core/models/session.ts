import { Side } from "./enum/side";

export interface Session {
    sessionId: string;
    name: string;
    side: Side;
}
