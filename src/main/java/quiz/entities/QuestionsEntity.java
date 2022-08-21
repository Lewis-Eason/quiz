package quiz.entities;

import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Table(name = "questions")
@Entity
public class QuestionsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "question_group", nullable = false)
    private String questionGroup;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column(name = "entered_answer")
    private String enteredAnswer;

    @OneToMany(
            mappedBy ="questionsEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<AnswersEntity> answersEntityList;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(final Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionGroup() {
        return questionGroup;
    }

    public void setQuestionGroup(final String questionGroup) {
        this.questionGroup = questionGroup;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(final String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getEnteredAnswer() {
        return enteredAnswer;
    }

    public void setEnteredAnswer(String enteredAnswer) {
        this.enteredAnswer = enteredAnswer;
    }

    public List<AnswersEntity> getAnswersEntityList() {
        return answersEntityList;
    }

    public void setAnswersEntityList(List<AnswersEntity> answersEntityList) {
        this.answersEntityList = answersEntityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionsEntity that = (QuestionsEntity) o;
        return Objects.equals(questionId, that.questionId) && Objects.equals(questionGroup, that.questionGroup) && Objects.equals(question, that.question) && Objects.equals(correctAnswer, that.correctAnswer) && Objects.equals(enteredAnswer, that.enteredAnswer) && Objects.equals(answersEntityList, that.answersEntityList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, questionGroup, question, correctAnswer, enteredAnswer, answersEntityList);
    }
}
