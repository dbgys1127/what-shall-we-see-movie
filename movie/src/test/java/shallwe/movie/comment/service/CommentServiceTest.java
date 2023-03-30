package shallwe.movie.comment.service;

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
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.comment.repository.CommentRepository;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.wantmovie.entity.WantMovie;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @DisplayName("사용자는 댓글을 등록할 수 있다.")
    @Test
    void saveMovieComment() {
        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        Movie movie = Movie.builder()
                .movieTitle("movie")
                .build();
        CommentDto.Post comment = CommentDto.Post.builder()
                .commentDetail("comment")
                .build();

        //when
        commentService.saveMovieComment(member,movie,comment);

        //then
        Assertions.assertThat(member.getComments().size()).isEqualTo(1);
        Assertions.assertThat(movie.getComments().size()).isEqualTo(1);
    }
    @DisplayName("댓글은 삭제 될 수 있다.")
    @Test
    void deleteWantMovie() {
        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        member.setWantMovies(new ArrayList<>());
        Movie movie = Movie.builder()
                .movieTitle("movie")
                .build();
        movie.setWantMovies(new ArrayList<>());

        Comment comment = Comment.builder()
                .member(member)
                .movie(movie)
                .build();

        //stub
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        //when
        commentService.deleteMovieComment(1L);

        //then
        Assertions.assertThat(member.getComments()).isEmpty();
        Assertions.assertThat(movie.getComments()).isEmpty();
    }
    @DisplayName("회원들은 부적절한 댓글을 신고할 수 있다.")
    @Test
    void addMovieCommentClaim() {
        //given
        Long commentId = 1L;

        Comment comment = Comment.builder()
                .build();

        //stub
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        //when
        commentService.addMovieCommentClaim(commentId);

        //then
        Assertions.assertThat(comment.getClaimCount()).isEqualTo(1);
    }
}
