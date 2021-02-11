package hu.miskolc.uni.robosoccer.model;

import lombok.*;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
public class OutputMessage {

    private String from;
    private String text;
    private String time;
}
