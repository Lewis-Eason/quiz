package quiz.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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
import java.util.concurrent.atomic.AtomicInteger;

@Component
@FxmlView("/fxml/quiz-controller.fxml")
public class QuizController implements Initializable {

    @FXML
    private Label question, timerLabel;

    @FXML
    private VBox vbox;

    @FXML
    private Button submitButton, backButton;

    @FXML
    private AnchorPane anchorPane;

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
        countdownTimer();
        try {
            setUpData();
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    private void setUpData() throws CouldNotFindQuestionEntityException {
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
            if (questionsEntity.getEnteredAnswer().equals(radioButton.getText())) {
                radioButton.setSelected(true);
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
                saveTimer();
                router.navigate(QuizController.class, event);
            } else {
                router.navigate(AnswersController.class, event);
            }
        });
    }

    private void setBackButtonAction(final Integer questionNumber) {
        backButton.setOnAction(event -> {
            saveTimer();
            holder.setQuestionNumber(questionNumber - 1);
            router.navigate(QuizController.class, event);
        });
    }

    private void countdownTimer() {
        final List<QuestionsEntity> questionsEntities = questionsService.getAllQuestions();
        int startTime = 60 * questionsEntities.size();
        AtomicInteger timerText = new AtomicInteger(setTimerText(startTime, true));
        Timeline timeline = new Timeline();
        timeline.setCycleCount(timerText.get());
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), event -> {
                    timerText.getAndDecrement();
                    setTimerText(timerText.get(), false);
                }));
        timeline.setOnFinished(event -> router.navigate(AnswersController.class, anchorPane));
        timeline.playFromStart();
    }

    private int setTimerText(final int startTime, final boolean fetchSavedTimer) {
        int time = startTime;
        if (fetchSavedTimer && holder.getTime() != null) {
            time = holder.getTime();
        }
        int minutes = time / 60;
        int seconds = time % 60;
        final String timer = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(timer);
        return time;
    }

    private void saveTimer() {
        this.holder.setTime(getCurrentTime());
    }

    private int getCurrentTime() {
        final String[] split = timerLabel.getText().split(":");
        final int seconds = Integer.parseInt(split[1]);
        final int minutes = Integer.parseInt(split[0]);
        return (60 * minutes) + seconds;
    }
}
