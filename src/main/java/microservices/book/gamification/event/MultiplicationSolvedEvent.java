package microservices.book.gamification.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class MultiplicationSolvedEvent implements Serializable {

    private static final long serialVersionUID = -4578315289680708860L;

    private final Long multiplicationResultAttemptId;
    private final Long userId;
    private final boolean correct;

    /**
     * Constructor for JSON Serializer
     */
    MultiplicationSolvedEvent(){
        this(null, null, false);
    }

}
