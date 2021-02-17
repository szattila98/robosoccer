package hu.miskolc.uni.robosoccer.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a connected user.
 *
 * @author Attila Szőke
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

    private final String sessionId;
    private final String name;
    private final Map<Integer, Player> players;
    private int points;

    public User(String sessionId, String name) {
        this.sessionId = sessionId;
        this.name = name;
        this.players = new HashMap<>();
        this.points = 0;
    }

    public void fillTeam() { // TODO better positions when there is a field
        for (int i = 1; i < 12; i++) {
            this.players.put(i, new Player(i, new Position(0, 0)));
        }
    }
}
