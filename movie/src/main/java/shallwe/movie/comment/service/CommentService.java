package shallwe.movie.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.comment.repository.CommentRepository;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public Page<Comment> getCommentList(String email, Pageable pageable) {
        return commentRepository.findCommentByMemberWithPaging(email,pageable);
    }
    public void saveMovieComment(Member member, Movie movie, CommentDto.Post commentDto) {
        Comment comment = Comment.builder()
                .commentDetail(commentDto.getCommentDetail()).build();

        setMemberRelation(member,comment);
        setMovieRelation(movie,comment);

        commentRepository.save(comment);
    }


    public void addMovieCommentClaim(Long commentId) {
        Comment comment = is_exist_comment(commentId);
        int claimCount = comment.getClaimCount();
        claimCount++;
        log.info("countClaim = {}",claimCount);
        comment.setClaimCount(claimCount);
    }
    public void deleteMovieComment(Long commentId) {
        commentRepository.delete(is_exist_comment(commentId));
    }

    public void setMemberRelation(Member member, Comment comment) {
        comment.setMember(member);
        member.getComments().add(comment);
    }

    public void setMovieRelation(Movie movie, Comment comment) {
        comment.setMovie(movie);
        movie.getComments().add(comment);
    }

    public Comment is_exist_comment(Long commentId) {
        Optional<Comment> optionalComment =commentRepository.findById(commentId);
        Comment findComment = optionalComment.orElseThrow(() -> new RuntimeException());
        return findComment;
    }
}
