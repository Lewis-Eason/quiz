package quiz.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quiz.alerts.AlertBoxes;
import quiz.configurations.Router;
import quiz.entities.AnswersEntity;
import quiz.entities.QuestionsEntity;
import quiz.exceptions.CouldNotFindQuestionEntityException;
import quiz.models.UserHolder;
import quiz.repositories.QuestionsRepository;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
@FxmlView("/fxml/quiz-controller.fxml")
public class QuizController implements Initializable {

    @FXML
    private Label question;

    @FXML
    private VBox vbox;

    @FXML
    private Button submitButton, backButton;

    private final QuestionsRepository questionsRepository;

    private final UserHolder holder;

    private final Router router;

    @Autowired
    public QuizController(final QuestionsRepository questionsRepository,
                          final UserHolder holder,
                          final Router router) {
        this.questionsRepository = questionsRepository;
        this.holder = holder;
        this.router = router;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setUpData();
        } catch (final CouldNotFindQuestionEntityException e) {
            e.printStackTrace();
        }
    }

    private void setUpData() throws CouldNotFindQuestionEntityException {
        final Integer questionNumber = holder.getQuestionNumber() != null ? holder.getQuestionNumber() : 1;
        final List<RadioButton> radioButtonList = new ArrayList<>();
        final QuestionsEntity questionsEntity =
                questionsRepository.findByQuestionGroup(String.valueOf(questionNumber))
                        .orElseThrow(() -> new CouldNotFindQuestionEntityException("Oops"));
        question.setText(questionsEntity.getQuestion());
        for (final AnswersEntity answersEntity : questionsEntity.getAnswersEntityList()) {
            radioButtonList.add(new RadioButton(String.valueOf(answersEntity.getAnswer())));
        }
        final ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonList.forEach(radioButton -> radioButton.setToggleGroup(toggleGroup));
        vbox.getChildren().addAll(radioButtonList);
        submitButton.setOnAction(event -> {
                if (validateInputs(toggleGroup)) {
                    final String answer = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                    final Map<Integer, String> map = holder.getAnswersMap();
                    map.put(questionNumber, answer);
                    holder.setAnswersMap(map);
                }
                holder.setQuestionNumber(questionNumber + 1);
                router.navigate(QuizController.class, event);
        });
        backButton.setOnAction(event -> {
            holder.setQuestionNumber(questionNumber - 1);
            router.navigate(QuizController.class, event);
        });
        displayBackButton(questionNumber, questionsEntity);
    }

    private boolean validateInputs(final ToggleGroup toggleGroup) {
        return toggleGroup.selectedToggleProperty().isNotNull().getValue();
    }

    private void displayBackButton(final Integer questionNumber, final QuestionsEntity questionsEntity) {
        if (questionNumber == 1 || questionNumber > questionsEntity.getAnswersEntityList().size()) {
            backButton.setVisible(false);
        }
    }
}
