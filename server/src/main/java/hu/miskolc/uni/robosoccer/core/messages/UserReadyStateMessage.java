package hu.miskolc.uni.robosoccer.core.messages;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Message which carries information about the user ready state.
 *
 * @author Tamás Sólyom
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserReadyStateMessage {

    private final User user;
    private final Boolean ready;
    private final RoundStatusType roundStatus;

}
