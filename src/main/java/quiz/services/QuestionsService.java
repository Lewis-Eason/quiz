package quiz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.entities.QuestionsEntity;
import quiz.exceptions.CouldNotFindQuestionEntityException;
import quiz.exceptions.CouldNotUpdateQuestionEntityException;
import quiz.repositories.QuestionsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionsService {

    private final QuestionsRepository questionsRepository;

    @Autowired
    public QuestionsService(final QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    public QuestionsEntity retrieveQuestionsByQuestionGroup(final String questionGroup)
            throws CouldNotFindQuestionEntityException {
        return this.questionsRepository.findByQuestionGroup(questionGroup)
                .orElseThrow(() -> new CouldNotFindQuestionEntityException(
                        String.format("Could not find question entity for question group %s", questionGroup)));
    }

    public List<QuestionsEntity> getAllQuestions() {
        return this.questionsRepository.findAll();
    }

    @Transactional
    public void resetAnsweredQuestions() {
        final List<QuestionsEntity> questionsEntities =
                this.questionsRepository.findAll();
        questionsEntities
                .forEach(questionsEntity ->
                        questionsEntity.setEnteredAnswer(null));
    }

    @Transactional
    public void updateAnsweredQuestion(final String updatedAnswer, final String questionGroup)
            throws CouldNotUpdateQuestionEntityException {
        final QuestionsEntity questionsEntity =
                this.questionsRepository.findByQuestionGroup(questionGroup)
                        .orElseThrow(() -> new CouldNotUpdateQuestionEntityException(
                                String.format("Could not update question entity for question group %s", questionGroup)));
        questionsEntity.setEnteredAnswer(updatedAnswer);
        questionsRepository.saveAndFlush(questionsEntity);
    }
}
