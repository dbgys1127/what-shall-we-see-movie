package shallwe.movie.wantmovie.entity;

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
public class WantMovie extends TimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wantMovieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    public WantMovie(Long wantMovieId, Member member, Movie movie) {
        this.wantMovieId = wantMovieId;
        this.member = member;
        this.movie = movie;
    }
}
