package shallwe.movie.movie.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.TestConfig;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.service.MovieService;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.repository.SawMovieRepository;
import shallwe.movie.sawmovie.service.SawMovieService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("testDB")
@Slf4j
public class MovieNPlusOneRepositoryTest {

    @Autowired
    private SawMovieService sawMovieService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SawMovieRepository sawMovieRepository;

    private Movie movie;
    private Member member;
    private SawMovie sawMovie;

    private List<SawMovie> sawMovies = new ArrayList<>();
    private List<Movie> movies = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;


    @BeforeEach
    void insertData() {

        for (int i = 0; i < 10; i++) {
            sawMovie = SawMovie.builder()
                    .movieSawCount(i)
                    .build();
            sawMovies.add(sawMovie);
        }
        sawMovieRepository.saveAll(sawMovies);

        for (int i = 0; i < 7; i++) {
            movie = Movie.builder()
                    .moviePoster("img")
                    .movieDescription("description")
                    .movieTitle("movie1")
                    .movieGenre(Movie.MovieGenre.공포)
                    .movieOpenDate(LocalDate.of(2021, 01, 01))
                    .movieRunningTime(90)
                    .build();
            movie.setSawMovies(sawMovies);
            movies.add(movie);
        }
        movieRepository.saveAll(movies);
        entityManager.clear();
    }

    @AfterEach
    void deleteData() {
        movieRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Transactional
    @DisplayName("영화를 조회하면 N+1 문제가 발생한다.")
    @Test
    void getMoviesHaveNPlusOne() {

        log.info("--------------------------- n+1 start------------------------");
        List<Movie> movieList = movieRepository.findAll() ;
        log.info("--------------------------- n+1 stop ------------------------");

    }
}
