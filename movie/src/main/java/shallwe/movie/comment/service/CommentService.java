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
    public Comment saveMovieComment(Member member, Movie movie, CommentDto.Post commentDto) {
        Comment comment = Comment.builder()
                .commentDetail(commentDto.getCommentDetail()).build();

        setMemberRelation(member,comment);
        setMovieRelation(movie,comment);
        log.info("댓글 등록 완료 -> 회원 이메일 : {}, 영화 제목 : {}",member.getEmail(),movie.getMovieTitle());
        return commentRepository.save(comment);
    }


    public void addMovieCommentClaim(Long commentId) {
        Comment comment = is_exist_comment(commentId);
        int claimCount = comment.getClaimCount();
        claimCount++;
        comment.setClaimCount(claimCount);
        log.info("댓글 신고 등록 완료 -> 신고 대상 댓글 : {}, 누적 신고 횟수 : {}",commentId,comment.getClaimCount());
    }
    public void deleteMovieComment(Long commentId) {
        commentRepository.delete(is_exist_comment(commentId));
        log.info("댓글 삭제 완료 -> 삭제된 댓글 : {}",commentId);
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
