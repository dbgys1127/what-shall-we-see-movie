package shallwe.movie.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.comment.repository.CommentRepository;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.wantmovie.entity.WantMovie;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    public void saveMovieComment(Member member, Movie movie, CommentDto.Post commentDto) {
        Comment comment = Comment.builder()
                .commentDetail(commentDto.getCommentDetail()).build();

        setMemberRelation(member,comment);
        setMovieRelation(movie,comment);

        commentRepository.save(comment);
    }

    public void setMemberRelation(Member member, Comment comment) {
        comment.setMember(member);
        member.getComments().add(comment);
    }

    public void setMovieRelation(Movie movie, Comment comment) {
        comment.setMovie(movie);
        movie.getComments().add(comment);
    }
}
