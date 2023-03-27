package shallwe.movie.sawmovie.entity;

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
}
