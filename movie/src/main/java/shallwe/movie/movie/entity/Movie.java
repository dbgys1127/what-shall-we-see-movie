package shallwe.movie.movie.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import shallwe.movie.audit.TimeAudit;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {@Index(name="idx_movie_title",columnList = "movieTitle")})
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

    private double avgSawCount;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<SawMovie> sawMovies = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<WantMovie> wantMovies = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public enum MovieGenre {
        코미디, 액션, 범죄, 드라마, SF, 공포, 로맨스;
    }

    @Builder

    public Movie(Long movieId, String movieTitle,
                 String moviePoster, String movieDescription,
                 int movieRunningTime, MovieGenre movieGenre,
                 LocalDate movieOpenDate, double avgSawCount,
                 List<SawMovie> sawMovies) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;
        this.movieDescription = movieDescription;
        this.movieRunningTime = movieRunningTime;
        this.movieGenre = movieGenre;
        this.movieOpenDate = movieOpenDate;
        this.avgSawCount = avgSawCount;
        this.sawMovies = sawMovies;
    }
}
