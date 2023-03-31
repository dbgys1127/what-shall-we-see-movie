package shallwe.movie.inquiry.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import shallwe.movie.answer.dto.AnswerDto;
import shallwe.movie.answer.entity.Answer;
import shallwe.movie.answer.repository.AnswerRepository;
import shallwe.movie.answer.service.AnswerService;
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.inquiry.dto.InquiryDto;
import shallwe.movie.inquiry.entity.Inquiry;
import shallwe.movie.inquiry.repository.InquiryRepository;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class InquiryServiceTest {
    @InjectMocks
    InquiryService inquiryService;
    @Mock
    MemberService memberService;
    @Mock
    AnswerService answerService;
    @Mock
    InquiryRepository inquiryRepository;

    @Mock
    AnswerRepository answerRepository;

    @DisplayName("멤버는 문의를 등록할 수 있다.")
    @Test
    void saveInquiry() {
        //given
        String email = "test@gmail.com";
        Member member = Member.builder()
                .email(email)
                .build();
        InquiryDto.Post inquiryPostDto = InquiryDto.Post.builder()
                .inquiryTitle("inquiry")
                .inquiryDescription("inquiryDescription")
                .build();
        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle(inquiryPostDto.getInquiryTitle())
                .inquiryDescription(inquiryPostDto.getInquiryDescription())
                .build();
        inquiry.setMember(member);

        //stub
        given(memberService.is_exist_member(email)).willReturn(member);
        given(inquiryRepository.save(any())).willReturn(inquiry);

        //when
        Inquiry findInquiry = inquiryService.saveInquiry(email, inquiryPostDto);

        //then
        Assertions.assertThat(member.getInquiries().get(0).getInquiryTitle()).isEqualTo("inquiry");
        Assertions.assertThat(findInquiry.getMember().getEmail()).isEqualTo(email);
    }

    @DisplayName("멤버는 자신이 등록한 문의 목록을 볼 수 있다.")
    @Test
    void getInquiryList() {
        //given
        String email = "test@gmail.com";
        int page =0;
        String sort = "createdAt";
        Member member = Member.builder()
                .email(email)
                .build();
        List<Inquiry> inquiries = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Inquiry inquiry = Inquiry.builder()
                    .inquiryTitle("inquiry"+i)
                    .inquiryDescription("inquiry description"+i)
                    .build();
            inquiry.setMember(member);
            inquiries.add(inquiry);
        }
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        Page<Inquiry> pageInfo = new PageImpl<>(inquiries, pageable, inquiries.size());

        //stub
        given(inquiryRepository.findInquiryByMemberWithPaging(email, pageable)).willReturn(pageInfo);

        //when
        PagingResponseDto<InquiryDto.Response> pagingResponseDto = inquiryService.getInquiryList(email, page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo(sort);
        Assertions.assertThat(pagingResponseDto.getEndPage()).isEqualTo(3);
    }

    @DisplayName("멤버는 자신이 등록한 문의의 상세내용을 조회할 수 있다.")
    @Test
    void getInquiry() {
        //given
        Long inquiryId = 1L;
        List<Answer> answerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Answer answer = Answer.builder()
                    .answerId((long) i)
                    .answerDescription("answer" + i)
                    .build();
            answer.setCreatedBy("test@gmail.com");
            answerList.add(answer);
        }
        Inquiry inquiry = Inquiry.builder()
                .inquiryId(inquiryId)
                .inquiryTitle("inquiry")
                .inquiryDescription("inquiry description")
                .answers(answerList)
                .build();

        //stub
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.of(inquiry));

        //when
        InquiryDto.Response inquiryRepDto = inquiryService.getInquiry(inquiryId);

        //then
        Assertions.assertThat(inquiryRepDto.getInquiryTitle()).isEqualTo("inquiry");
    }

    @DisplayName("관리자는 문의에 대한 답변 등록할 수 있다.")
    @Test
    void saveAnswer() {
        //given
        Long inquiryId = 1L;
        String inquiryStatus = "on";
        AnswerDto.Post answerPostDto = AnswerDto.Post.builder()
                .answerDescription("answer description").build();

        Answer answer = Answer.builder()
                .answerId(1L)
                .answerDescription("answer")
                .build();

        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle("inquiry")
                .inquiryDescription("inquiry description")
                .build();

        //stub
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.of(inquiry));
        given(answerService.saveAnswer(inquiry, answerPostDto)).willReturn(answer);

        //when
        inquiryService.saveAnswer(inquiryId,inquiryStatus,answerPostDto);

        //then
        Assertions.assertThat(inquiry.getInquiryStatus()).isEqualTo(Inquiry.InquiryStatus.처리);
    }
    @DisplayName("관리자는 문의에 대한 답변 상태를 변경할 수 있다.")
    @Test
    void saveAnswerAndInquiryStatus() {
        //given
        Long inquiryId = 1L;
        String inquiryStatus = "off";
        AnswerDto.Post answerPostDto = AnswerDto.Post.builder()
                .answerDescription("answer description").build();

        Answer answer = Answer.builder()
                .answerId(1L)
                .answerDescription("answer")
                .build();

        Inquiry inquiry = Inquiry.builder()
                .inquiryTitle("inquiry")
                .inquiryDescription("inquiry description")
                .build();

        //stub
        given(inquiryRepository.findById(inquiryId)).willReturn(Optional.of(inquiry));
        given(answerService.saveAnswer(inquiry, answerPostDto)).willReturn(answer);

        //when
        inquiryService.saveAnswer(inquiryId,inquiryStatus,answerPostDto);

        //then
        Assertions.assertThat(inquiry.getInquiryStatus()).isEqualTo(Inquiry.InquiryStatus.대기);
    }
}
