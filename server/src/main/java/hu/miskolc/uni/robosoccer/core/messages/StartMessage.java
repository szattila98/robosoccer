package hu.miskolc.uni.robosoccer.core.messages;

import hu.miskolc.uni.robosoccer.core.User;
import hu.miskolc.uni.robosoccer.core.enums.ReadyType;
import hu.miskolc.uni.robosoccer.core.enums.RoundStatusType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StartMessage {

    private User user;
    private ReadyType readyType;
    private RoundStatusType roundStatusType;

}
