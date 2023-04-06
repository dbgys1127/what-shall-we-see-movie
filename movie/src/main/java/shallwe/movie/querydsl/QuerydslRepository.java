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
    Page<Member> findAllMemberWithPaging(String email,Pageable pageable);
    Page<Member> findAllAdminWithPaging(String email,Pageable pageable);

    Page<Movie> findMovieByTitleWithPaging(String title, Pageable pageable);

    Page<Movie> findMovieByGenreWithPaging(String movieGenre, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_READ)
    SawMovie findSawMovieByMemberAndMovie(Member member, Movie movie);

    Page<SawMovie> findSawMoviesByMemberWithPaging(Member member, Pageable pageable);

    WantMovie findWantMovieByMemberAndMovie(Member member, Movie movie);

    Page<WantMovie> findWantMoviesByMemberWithPaging(Member member, Pageable pageable);

    Page<Comment> findCommentByMemberWithPaging(String email, Pageable pageable);

    Page<Inquiry> findInquiryByMemberWithPaging(String email, Pageable pageable);
}
