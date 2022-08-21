package quiz.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quiz.configurations.Router;
import quiz.entities.QuestionsEntity;
import quiz.models.TableData;
import quiz.models.UserHolder;
import quiz.services.QuestionsService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("/fxml/answers-controller.fxml")
public class AnswersController implements Initializable {

    @FXML
    private Label scoreLabel;

    @FXML
    private TableColumn<TableData, String> questionCol, answerCol, correctAnswerCol;

    @FXML
    private TableColumn<TableData, ImageView> evaluateCol;

    @FXML
    private TableView<TableData> answersTable;

    private final QuestionsService questionsService;

    private final ObservableList<TableData> answersList;

    private final Router router;

    private final UserHolder userHolder;

    @Autowired
    public AnswersController(final QuestionsService questionsService,
                             final Router router,
                             final UserHolder userHolder) {
        this.router = router;
        this.userHolder = userHolder;
        this.answersList = FXCollections.observableArrayList();
        this.questionsService = questionsService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumnProperties();
        populateTable();
        questionsService.resetAnsweredQuestions();
    }

    private void populateTable() {
        answersList.clear();
        final List<QuestionsEntity> questionsEntityList =
                questionsService.getAllQuestions();
        int totalScore = 0;
        for (final QuestionsEntity questionsEntity : questionsEntityList) {
            final ImageView imageView = new ImageView();
            imageView.setFitWidth(20.0);
            imageView.setFitHeight(20.0);
            Image evaluationImage = new Image(
                    getClass().getResource("/images/red-cross.png").toExternalForm());
            if (questionsEntity.getCorrectAnswer().equals(questionsEntity.getEnteredAnswer())) {
                evaluationImage = new Image(
                        getClass().getResource("/images/green-tick.png").toExternalForm());
                totalScore++;
            }
            imageView.setImage(evaluationImage);
            answersList.add(new TableData(
                    questionsEntity.getQuestion(),
                    questionsEntity.getEnteredAnswer(),
                    questionsEntity.getCorrectAnswer(),
                    imageView
                    )
            );
        }
        answersTable.setItems(answersList);
        final int percentage = (totalScore * 100) / questionsEntityList.size();
        final String correctAnswers = String.format("%s/%s", totalScore, questionsEntityList.size());
        scoreLabel.setText("You scored " + correctAnswers + " - " + percentage + "%");
    }

    private void setColumnProperties() {
        questionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        answerCol.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));
        correctAnswerCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        evaluateCol.setCellValueFactory(new PropertyValueFactory<>("evaluation"));
    }

    @FXML
    public void startAgain(ActionEvent event) {
        userHolder.setQuestionNumber(null);
        router.navigate(QuizController.class, event);
    }
}
