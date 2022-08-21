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
import quiz.configurations.Router;
import quiz.entities.AnswersEntity;
import quiz.entities.QuestionsEntity;
import quiz.exceptions.CouldNotFindQuestionEntityException;
import quiz.exceptions.CouldNotUpdateQuestionEntityException;
import quiz.models.UserHolder;
import quiz.services.QuestionsService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("/fxml/quiz-controller.fxml")
public class QuizController implements Initializable {

    @FXML
    private Label question;

    @FXML
    private VBox vbox;

    @FXML
    private Button submitButton, backButton;

    private final UserHolder holder;

    private final Router router;

    private final QuestionsService questionsService;

    @Autowired
    public QuizController(final UserHolder holder,
                          final Router router,
                          final QuestionsService questionsService) {
        this.holder = holder;
        this.router = router;
        this.questionsService = questionsService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setUpData();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    private void setUpData() throws CouldNotFindQuestionEntityException, CouldNotUpdateQuestionEntityException {
        final Integer questionNumber = holder.getQuestionNumber() != null ? holder.getQuestionNumber() : 1;
        final List<RadioButton> radioButtonList = new ArrayList<>();
        final QuestionsEntity questionsEntity =
                questionsService.retrieveQuestionsByQuestionGroup(String.valueOf(questionNumber));
        question.setText(questionsEntity.getQuestion());
        for (final AnswersEntity answersEntity : questionsEntity.getAnswersEntityList()) {
            radioButtonList.add(new RadioButton(String.valueOf(answersEntity.getAnswer())));
        }
        final ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonList.forEach(radioButton -> {
            if (questionsEntity.getEnteredAnswer() != null) {
                if (questionsEntity.getEnteredAnswer().equals(radioButton.getText())) {
                    radioButton.setSelected(true);
                }
            }
            radioButton.setToggleGroup(toggleGroup);
        });
        vbox.getChildren().addAll(radioButtonList);
        final List<QuestionsEntity> questionsEntities = questionsService.getAllQuestions();
        setSubmitButtonAction(questionsEntities, toggleGroup, questionNumber);
        setBackButtonAction(questionNumber);
        displayBackButton(questionNumber, questionsEntities);
    }

    private boolean validateInputs(final ToggleGroup toggleGroup) {
        return toggleGroup.selectedToggleProperty().isNotNull().getValue();
    }

    private void displayBackButton(final Integer questionNumber, final List<QuestionsEntity> questionsEntities) {
        if (questionNumber == 1 || questionNumber > questionsEntities.size()) {
            backButton.setVisible(false);
        }
    }

    private void setSubmitButtonAction(final List<QuestionsEntity> questionsEntities,
                                       final ToggleGroup toggleGroup,
                                       final Integer questionNumber) {
        submitButton.setOnAction(event -> {
            if (validateInputs(toggleGroup)) {
                final String answer = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                try {
                    questionsService.updateAnsweredQuestion(answer, questionNumber.toString());
                } catch (CouldNotUpdateQuestionEntityException e) {
                    e.printStackTrace();
                }
            }
            if (questionNumber + 1 <= questionsEntities.size()) {
                holder.setQuestionNumber(questionNumber + 1);
                router.navigate(QuizController.class, event);
            } else {
                router.navigate(AnswersController.class, event);
            }
        });
    }

    private void setBackButtonAction(final Integer questionNumber) {
        backButton.setOnAction(event -> {
            holder.setQuestionNumber(questionNumber - 1);
            router.navigate(QuizController.class, event);
        });
    }
}
