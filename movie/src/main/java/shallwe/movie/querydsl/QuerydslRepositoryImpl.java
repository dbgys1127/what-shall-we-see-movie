package shallwe.movie.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;

import java.util.List;

import static shallwe.movie.comment.entity.QComment.comment;
import static shallwe.movie.member.entity.QMember.member;
import static shallwe.movie.movie.entity.QMovie.movie;
import static shallwe.movie.sawmovie.entity.QSawMovie.sawMovie;
import static shallwe.movie.wantmovie.entity.QWantMovie.wantMovie;

@RequiredArgsConstructor
public class QuerydslRepositoryImpl implements QuerydslRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Member> findAllMemberWithPaging(String email,Pageable pageable) {
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(member.email.contains(email)
                                .and(member.roles.size().eq(1)))
                .orderBy(memberSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(member)
                .where(member.email.contains(email)
                                .and(member.roles.size().eq(1)))
                .fetchCount();
        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<Member> findAllAdminWithPaging(String email, Pageable pageable) {
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(member.email.contains(email)
                        .and(member.roles.size().eq(2)))
                .orderBy(memberSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(member)
                .where(member.email.contains(email)
                        .and(member.roles.size().eq(2)))
                .fetchCount();
        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<Movie> findMovieByTitleWithPaging(String title, Pageable pageable) {
        List<Movie> content = queryFactory
                .selectFrom(movie)
                .where(movie.movieTitle.contains(title))
                .orderBy(movieSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(movie)
                .where(movie.movieTitle.contains(title))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Movie> findMovieByGenreWithPaging(String movieGenre, Pageable pageable) {
        List<Movie> content = queryFactory
                .selectFrom(movie)
                .where(movie.movieGenre.eq(Movie.MovieGenre.valueOf(movieGenre)))
                .orderBy(movieSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(movie)
                .where(movie.movieGenre.eq(Movie.MovieGenre.valueOf(movieGenre)))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public SawMovie findSawMovieByMemberAndMovie(Member member, Movie movie) {
        return queryFactory
                .selectFrom(sawMovie)
                .where(sawMovie.member.eq(member)
                        .and(sawMovie.movie.eq(movie)))
                .fetchOne();
    }

    @Override
    public Page<SawMovie> findSawMoviesByMemberWithPaging(Member member, Pageable pageable) {
        List<SawMovie> content= queryFactory
                .selectFrom(sawMovie)
                .where(sawMovie.member.eq(member))
                .orderBy(memberSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.selectFrom(sawMovie)
                .where(sawMovie.member.eq(member))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public WantMovie findWantMovieByMemberAndMovie(Member member, Movie movie) {
        return queryFactory
                .selectFrom(wantMovie)
                .where(wantMovie.member.eq(member)
                        .and(wantMovie.movie.eq(movie)))
                .fetchOne();
    }

    @Override
    public Page<WantMovie> findWantMoviesByMemberWithPaging(Member member, Pageable pageable) {
        List<WantMovie> content= queryFactory
                .selectFrom(wantMovie)
                .where(wantMovie.member.eq(member))
                .orderBy(memberSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.selectFrom(wantMovie)
                .where(wantMovie.member.eq(member))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Comment> findCommentByMemberWithPaging(Member member, Pageable pageable) {
        List<Comment> content= queryFactory
                .selectFrom(comment)
                .where(comment.member.eq(member))
                .orderBy(memberSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.selectFrom(comment)
                .where(comment.member.eq(member))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> movieSort(Pageable page) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "movieId":
                        return new OrderSpecifier(direction, movie.movieId);
                    case "movieOpenDate":
                        return new OrderSpecifier<>(direction, movie.movieOpenDate);
                }
            }
        }
        return null;
    }

    private OrderSpecifier<?> memberSort(Pageable page) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "memberId":
                        return new OrderSpecifier(direction, member.memberId);
                    case "warningCard":
                        return new OrderSpecifier(direction, member.warningCard);
                    case "memberStatus":
                        return new OrderSpecifier(Order.ASC, member.memberStatus);
                    case "movieSawCount":
                        return new OrderSpecifier(direction, sawMovie.movieSawCount);
                    case "createdAtForWantMovie":
                        return new OrderSpecifier(direction, wantMovie.createdAt);
                    case "createdAtForComment":
                        return new OrderSpecifier(direction, comment.createdAt);
                }
            }
        }
        return null;
    }
}
