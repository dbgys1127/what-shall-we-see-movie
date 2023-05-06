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

    /**
     * 나의 댓글 목록 조회
     * @param email : 조회하는 회원의 이메일
     * @param pageable : 페이징 정보
     */
    public Page<Comment> getCommentList(String email, Pageable pageable) {
        return commentRepository.findCommentByMemberWithPaging(email,pageable);
    }

    /**
     * 영화 댓글 등록
     * @param member : 나의 댓글 목록에서 보기 위해 회원객체 사용
     * @param movie : 영화 상세페이지 내 댓글 목록을 띄우기 위한 영화객체
     * @param commentDto : 댓글 내용
     */
    public Comment saveMovieComment(Member member, Movie movie, CommentDto.Post commentDto) {
        Comment comment = Comment.builder()
                .commentDetail(commentDto.getCommentDetail()).build();
        setMemberRelation(member,comment);
        setMovieRelation(movie,comment);
        log.info("댓글 등록 완료 -> 회원 이메일 : {}, 영화 제목 : {}",member.getEmail(),movie.getMovieTitle());
        return commentRepository.save(comment);
    }

    /**
     * 영화 수정 댓글
     * @param commentId : 수정 대상을 찾기 위한 값
     * @param commentDto : 수정 내용
     */
    public void patchMovieComment(Long commentId, CommentDto.Patch commentDto) {
        Comment comment = is_exist_comment(commentId);
        comment.setCommentDetail(commentDto.getCommentDetail());
    }

    /**
     * 댓글 신고
     * @param commentId : 신고 대상의 기본키
     * 메소드가 호출될때마다 댓글의 신고회수가 증가됨
     */
    public void addMovieCommentClaim(Long commentId) {
        Comment comment = is_exist_comment(commentId);
        int claimCount = comment.getClaimCount();
        claimCount++;
        comment.setClaimCount(claimCount);
        log.info("댓글 신고 등록 완료 -> 신고 대상 댓글 : {}, 누적 신고 횟수 : {}",commentId,comment.getClaimCount());
    }

    /**
     * 댓글 삭제
     * @param commentId : 삭제 대상의 기본키
     */
    public void deleteMovieComment(Long commentId) {
        commentRepository.delete(is_exist_comment(commentId));
        log.info("댓글 삭제 완료 -> 삭제된 댓글 : {}",commentId);
    }

    /**
     * 나의 댓글 조회시 member 객체에서 댓글이 추가되게 하는 용도
     * @param member : 댓글 작성자 객체
     * @param comment : 추가될 댓글
     */
    public void setMemberRelation(Member member, Comment comment) {
        comment.setMember(member);
        member.getComments().add(comment);
    }

    /**
     * 영화 상세페이지 조회시 movie 객체에 댓글이 추가되게 하는 용도
     * @param movie : 댓글이 작성된 영화 객체
     * @param comment : 추가될 댓글
     */
    public void setMovieRelation(Movie movie, Comment comment) {
        comment.setMovie(movie);
        movie.getComments().add(comment);
    }

    /**
     * 댓글 존재 유무 및 찾는 댓글 객체 반환용
     * @param commentId : 대상 댓글 기본키
     */
    public Comment is_exist_comment(Long commentId) {
        Optional<Comment> optionalComment =commentRepository.findById(commentId);
        Comment findComment = optionalComment.orElseThrow(() -> new RuntimeException());
        return findComment;
    }
}
