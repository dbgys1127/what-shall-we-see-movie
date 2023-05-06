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

    /**
     * 답변 등록
     * @param inquiry : 문의 조회시 답변도 함께 조회되기 위해 연관관계 맺기용
     * @param answerDto : 답변 내용
     */
    public Answer saveAnswer(Inquiry inquiry, AnswerDto.Post answerDto) {
        Answer answer = Answer.builder().answerDescription(answerDto.getAnswerDescription()).build();
        answer.setInquiry(inquiry);
        inquiry.getAnswers().add(answer);
        return answerRepository.save(answer);
    }

    /**
     * 답변 수정
     * @param answerId : 수정 대상 판단값
     * @param answerDto : 수정할 내용
     */
    public void patchAnswer(Long answerId, AnswerDto.Patch answerDto) {
        Answer answer = is_exist_answer(answerId);
        answer.setAnswerDescription(answerDto.getAnswerDescription());
    }

    /**
     * 답변 삭제
     * @param answerId : 삭제할 대상
     */
    public void deleteAnswer(Long answerId) {
        Answer answer = is_exist_answer(answerId);
        answerRepository.delete(answer);
    }

    /**
     * 답변 존재유무 파악용
     * @param answerId : 검색 대상
     */
    public Answer is_exist_answer(Long answerId) {
        Optional<Answer> optionalAnswer =answerRepository.findById(answerId);
        Answer findAnswer = optionalAnswer.orElseThrow(() -> new RuntimeException());
        return findAnswer;
    }
}
