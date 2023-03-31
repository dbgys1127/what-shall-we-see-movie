package shallwe.movie.inquiry.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import shallwe.movie.TestConfig;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.comment.repository.CommentRepository;
import shallwe.movie.inquiry.entity.Inquiry;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
@ActiveProfiles("test")
@Slf4j
public class InquiryRepositoryTest {
    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void insertData() {

        Member member=memberRepository.save(Member.builder()
                .email("test1@gmail.com")
                .password("1234!abc")
                .warningCard(0)
                .build());

        for (int i = 1; i < 11; i++) {
            Inquiry inquiry = Inquiry.builder()
                    .inquiryDescription("inquiry description"+i)
                    .member(member)
                    .inquiryTitle("inquiry"+i)
                    .build();
            inquiry.setInquiryStatus(Inquiry.InquiryStatus.대기);
            inquiryRepository.save(inquiry);
        }
        for (int i = 11; i < 21; i++) {
            Inquiry inquiry = Inquiry.builder()
                    .inquiryDescription("inquiry description"+i)
                    .member(member)
                    .inquiryTitle("inquiry"+i)
                    .build();
            inquiry.setInquiryStatus(Inquiry.InquiryStatus.처리);
            inquiryRepository.save(inquiry);
        }
    }
    @AfterEach
    void deleteData() {
        inquiryRepository.deleteAll();
    }

    @DisplayName("회원은 등록한 문의 내역을 생성일자에 맞게 볼 수 있다.")
    @Test
    void findInquiryByMemberWithPagingSortByCreatedAt() {
        //given
        String email = "test1@gmail.com";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        //when
        Page<Inquiry> pageInfo = inquiryRepository.findInquiryByMemberWithPaging(email, pageable);

        //then
        Assertions.assertThat(pageInfo.getContent().size()).isEqualTo(10);
        Assertions.assertThat(pageInfo.getContent().get(0).getInquiryTitle()).isEqualTo("inquiry20");
    }

    @DisplayName("회원은 등록한 문의 내역을 처리여부에 맞게 볼 수 있다.")
    @Test
    void findInquiryByMemberWithPagingSortByInquiryStatus() {
        //given
        String email = "test1@gmail.com";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("inquiryStatus").descending());

        //when
        Page<Inquiry> pageInfo = inquiryRepository.findInquiryByMemberWithPaging(email, pageable);

        //then
        Assertions.assertThat(pageInfo.getContent().size()).isEqualTo(10);
        Assertions.assertThat(pageInfo.getContent().get(0).getInquiryStatus()).isEqualTo(Inquiry.InquiryStatus.대기);
    }
}
