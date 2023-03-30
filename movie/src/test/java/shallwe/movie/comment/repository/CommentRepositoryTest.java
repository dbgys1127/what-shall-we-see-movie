package shallwe.movie.comment.repository;

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
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.repository.WantMovieRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
@ActiveProfiles("test")
@Slf4j
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void insertData() {

        Member member=memberRepository.save(Member.builder()
                .email("test1@gmail.com")
                .password("1234!abc")
                .warningCard(0)
                .build());

        for (int i = 1; i < 21; i++) {
            Comment comment = Comment.builder()
                    .commentDetail("comment"+i)
                    .member(member)
                    .claimCount(21-i)
                    .build();
            commentRepository.save(comment);
        }
    }
    @AfterEach
    void deleteData() {
        commentRepository.deleteAll();
    }

    @DisplayName("회원은 등록한 댓글 내역을 댓글 생성일자에 맞게 볼 수 있다.")
    @Test
    void findCommentByMemberWithPagingSortByCreatedAt() {
        //given
        String email = "test1@gmail.com";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAtForComment").descending());

        //when
        Page<Comment> pageInfo = commentRepository.findCommentByMemberWithPaging(email, pageable);

        //then
        Assertions.assertThat(pageInfo.getContent().size()).isEqualTo(10);
        Assertions.assertThat(pageInfo.getContent().get(0).getCommentDetail()).isEqualTo("comment20");
    }
    @DisplayName("회원은 등록한 댓글 내역을 신고횟수에 맞게 볼 수 있다.")
    @Test
    void findCommentByMemberWithPagingSortByClaimCount() {
        //given
        String email = "test1@gmail.com";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("claimCount").descending());

        //when
        Page<Comment> pageInfo = commentRepository.findCommentByMemberWithPaging(email, pageable);

        //then
        Assertions.assertThat(pageInfo.getContent().size()).isEqualTo(10);
        Assertions.assertThat(pageInfo.getContent().get(0).getCommentDetail()).isEqualTo("comment1");
    }
}
