package hu.miskolc.uni.robosoccer.core.messages;

import hu.miskolc.uni.robosoccer.core.Match;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * Message which carries information about the state of the match;
 *
 * @author Tamás Sólyom
 */
@Getter
@ToString
@EqualsAndHashCode
public class MatchStateMessage {

    private final Match match;
    private final Date date;

    public MatchStateMessage(Match match) {
        this.match = match;
        this.date = new Date();
    }
}
