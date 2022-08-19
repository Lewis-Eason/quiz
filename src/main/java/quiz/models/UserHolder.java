package quiz.models;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Data
public class UserHolder {

    private Integer questionNumber;
    private Map<Integer, String> answersMap = new HashMap<>();

    private final static UserHolder INSTANCE = new UserHolder();

    private UserHolder() {}

    public static UserHolder getInstance() {
        return INSTANCE;
    }

}
