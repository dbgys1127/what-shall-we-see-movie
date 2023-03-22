package shallwe.movie.movie.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shallwe.movie.audit.TimeAudit;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie extends TimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    @Column(nullable = false,length = 20)
    private String movieTitle;

    @Column(nullable = false,columnDefinition = "MEDIUMTEXT")
    private String moviePoster;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String movieDescription;

    @Column(nullable = false)
    private int movieRunningTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieGenre movieGenre;

    @Column(nullable = false)
    private LocalDate movieOpenDate;


    public enum MovieGenre {
        코미디, 액션, 범죄, 드라마, SF, 공포, 로맨스;
    }

    @Builder
    public Movie(String movieTitle, String moviePoster, String movieDescription, int movieRunningTime, MovieGenre movieGenre, LocalDate movieOpenDate) {
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
        this.movieDescription = movieDescription;
        this.movieRunningTime = movieRunningTime;
        this.movieGenre = movieGenre;
        this.movieOpenDate = movieOpenDate;
    }
}
