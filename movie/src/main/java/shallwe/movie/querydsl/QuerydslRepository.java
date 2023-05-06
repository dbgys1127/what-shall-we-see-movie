package shallwe.movie.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.inquiry.entity.Inquiry;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;

import javax.persistence.LockModeType;
import java.util.List;

public interface QuerydslRepository {

    // 페이징된 회원 목록 조회
    Page<Member> findAllMemberWithPaging(String email,Pageable pageable);

    // 페이징된 관리자 목록 조회
    Page<Member> findAllAdminWithPaging(String email,Pageable pageable);

    // 검색 키워드에 대해 페이징된 값 조회
    Page<Movie> findMovieByTitleWithPaging(String title, Pageable pageable);

    // 장르 검색에 대해 페이징된 값 조회
    Page<Movie> findMovieByGenreWithPaging(String movieGenre, Pageable pageable);

    // 회원과 영화가 일치하는 SawMovie 객체 조회
    SawMovie findSawMovieByMemberAndMovie(Member member, Movie movie);

    // 내가 시청횟수를 등록한 영화 목록 조회
    Page<SawMovie> findSawMoviesByMemberWithPaging(Member member, Pageable pageable);

    // 회원과 영화가 일치하는 WantMovie 객체 조회
    WantMovie findWantMovieByMemberAndMovie(Member member, Movie movie);

    // 내가 찜 등록한 영화 목록 조회
    Page<WantMovie> findWantMoviesByMemberWithPaging(Member member, Pageable pageable);

    // 내가 등록한 댓글 조회
    Page<Comment> findCommentByMemberWithPaging(String email, Pageable pageable);

    // 내가 등록한 문의 조회
    Page<Inquiry> findInquiryByMemberWithPaging(String email, Pageable pageable);
}
