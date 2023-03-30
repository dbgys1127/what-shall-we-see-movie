package shallwe.movie.answer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.answer.dto.AnswerDto;
import shallwe.movie.answer.entity.Answer;
import shallwe.movie.answer.repository.AnswerRepository;
import shallwe.movie.inquiry.entity.Inquiry;

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
}
