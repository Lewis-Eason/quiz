package quiz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quiz.entities.QuestionsEntity;

import java.util.Optional;

@Repository
public interface QuestionsRepository extends JpaRepository<QuestionsEntity, Long> {

    Optional<QuestionsEntity> findByQuestionGroup(final String questionGroup);

    QuestionsEntity findEnteredAnswerByQuestionGroup(final String questionGroup);

}
