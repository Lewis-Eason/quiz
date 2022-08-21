package quiz.models;

import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableData {

    private String question;
    private String yourAnswer;
    private String correctAnswer;
    private ImageView evaluation;
}
