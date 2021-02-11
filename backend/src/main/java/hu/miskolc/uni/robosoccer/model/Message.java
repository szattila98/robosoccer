package hu.miskolc.uni.robosoccer.model;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
public class Message {

    private String from;
    private String text;
}
