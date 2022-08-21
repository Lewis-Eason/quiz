package quiz.models;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserHolder {

    private Integer questionNumber;

    private final static UserHolder INSTANCE = new UserHolder();

    private UserHolder() {}

    public static UserHolder getInstance() {
        return INSTANCE;
    }

}
