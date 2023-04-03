package shallwe.movie.sawmovie.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shallwe.movie.audit.TimeAudit;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_member_movie",columnList = "member_id,movie_id"),
                    @Index(name = "idx_member",columnList = "member_id")})
public class SawMovie extends TimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sawMovieId;

    private int movieSawCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    public SawMovie(Long sawMovieId, int movieSawCount, Member member, Movie movie) {
        this.sawMovieId = sawMovieId;
        this.movieSawCount = movieSawCount;
        this.member = member;
        this.movie = movie;
    }
}
