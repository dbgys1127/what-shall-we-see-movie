package shallwe.movie.answer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.answer.dto.AnswerDto;
import shallwe.movie.answer.entity.Answer;
import shallwe.movie.answer.repository.AnswerRepository;
import shallwe.movie.inquiry.entity.Inquiry;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AnswerService {
    private final AnswerRepository answerRepository;
    public Answer saveAnswer(Inquiry inquiry, AnswerDto.Post answerDto) {
        Answer answer = Answer.builder().answerDescription(answerDto.getAnswerDescription()).build();
        answer.setInquiry(inquiry);
        inquiry.getAnswers().add(answer);
        return answerRepository.save(answer);
    }

    public void deleteAnswer(Long answerId) {
        Answer answer = is_exist_answer(answerId);
        answerRepository.delete(answer);
    }
    public Answer is_exist_answer(Long answerId) {
        Optional<Answer> optionalAnswer =answerRepository.findById(answerId);
        Answer findAnswer = optionalAnswer.orElseThrow(() -> new RuntimeException());
        return findAnswer;
    }
}
