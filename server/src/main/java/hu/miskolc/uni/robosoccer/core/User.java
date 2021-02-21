package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.ReadyType;
import hu.miskolc.uni.robosoccer.core.enums.SideType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a connected user.
 *
 * @author Attila Szőke
 */
@Getter
@ToString
@EqualsAndHashCode
public class User {

    private final String sessionId;
    private final String name;
    private SideType side;
    private final Map<Integer, Player> team;
    private int points;
    private ReadyType readyType;

    public User(String sessionId, String name) {
        this.sessionId = sessionId;
        this.name = name;
        this.team = new HashMap<>();
        this.points = 0;
        this.readyType = ReadyType.WAIT;
    }

    public void fillTeam() {
        Position[] positions = new Position[]{  // soccer pitch is 140 * 100, coordinates are based on that fact
                new Position(60, 0),      // goalkeeper
                new Position(40, 40),     // defenders
                new Position(45, 20),
                new Position(45, -20),
                new Position(40, -40),
                new Position(15, 25),     // midfielders
                new Position(10, 0),
                new Position(15, -25),
                new Position(-35, 25),    // attackers
                new Position(-40, 0),
                new Position(-35, -25),
        };

        for (int i = 0; i < positions.length; i++) {
            if (side == SideType.LEFT) {
                this.team.put(i, new Player(i, positions[i].invert()));
            } else {
                this.team.put(i, new Player(i, positions[i]));
            }
        }
    }


    public void incrementPoints() {
        this.points++;
    }

    public void setSide(SideType side) {
        this.side = side;
    }

    public void setReadyType(ReadyType readyType) {
        this.readyType = readyType;
    }

}
