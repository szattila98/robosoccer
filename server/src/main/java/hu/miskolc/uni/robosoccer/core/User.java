package hu.miskolc.uni.robosoccer.core;

import hu.miskolc.uni.robosoccer.core.enums.SideType;
import hu.miskolc.uni.robosoccer.core.exceptions.PlayerNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Player> team;
    private int points;
    private boolean ready;

    public User(String sessionId, String name) {
        this.sessionId = sessionId;
        this.name = name;
        this.team = new ArrayList<>();
        this.points = 0;
        this.ready = false;
    }

    public void fillTeam() {
        Position[] positions = new Position[]{  // soccer pitch is 140 * 100, (0,0) is at the upper left corner
                new Position(10, 50),      // goalkeeper
                new Position(20, 20),     // defenders
                new Position(25, 30),
                new Position(25, 70),
                new Position(20, 80),
                new Position(50, 30),     // midfielders
                new Position(55, 50),
                new Position(50, 70),
                new Position(100, 30),    // attackers
                new Position(110, 50),
                new Position(100, 70),
        };

        if (this.side == SideType.LEFT) {
            for (int i = 0; i < positions.length; i++) {
                this.team.add(new Player(i, positions[i], this.side));
            }
        } else {
            for (int i = 0; i < positions.length; i++) {
                this.team.add(new Player(i, positions[i].invert(), this.side));
            }
        }

    }

    public Player getPlayerById(int playerId) throws PlayerNotFoundException {
        return this.team.stream()
                .filter(player -> player.getId() == playerId)
                .findAny()
                .orElseThrow(PlayerNotFoundException::new);
    }

    public void incrementPoints() {
        this.points++;
    }

    public void setSide(SideType side) {
        this.side = side;
    }

    public void toggleReady() {
        this.ready = !this.ready;
    }

}
