package shallwe.movie.answer.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import shallwe.movie.answer.dto.AnswerDto;
import shallwe.movie.answer.entity.Answer;
import shallwe.movie.answer.repository.AnswerRepository;
import shallwe.movie.inquiry.entity.Inquiry;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class AnswerServiceTest {
    @InjectMocks
    AnswerService answerService;

    @Mock
    AnswerRepository answerRepository;

    @DisplayName("관리자는 답변을 등록할 수 있다.")
    @Test
    void saveAnswer() {
        //given
        AnswerDto.Post answerPostDto = AnswerDto.Post.builder().answerDescription("answer").build();
        Inquiry inquiry = Inquiry.builder().inquiryTitle("inquiry").inquiryDescription("inquiry description")
                .answers(new ArrayList<>())
                .build();

        //when
        Answer findAnswer = answerService.saveAnswer(inquiry, answerPostDto);

    }
}
