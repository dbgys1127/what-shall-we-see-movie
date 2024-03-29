package shallwe.movie.movie.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import shallwe.movie.TestConfig;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.repository.SawMovieRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
@ActiveProfiles("test")
@Slf4j
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SawMovieRepository sawMovieRepository;

    private List<Movie> movies = new ArrayList<>();
    private List<SawMovie> sawMovies = new ArrayList<>();

    @BeforeEach
    void insertData() {
        for (int i = 1; i < 21; i++) {
            SawMovie sawMovie = SawMovie.builder()
                    .movieSawCount(i)
                    .build();
            sawMovies.add(sawMovie);
        }
        sawMovieRepository.saveAll(sawMovies);
        for (int i = 1; i < 21; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .moviePoster("image")
                    .movieDescription("movie description" + i)
                    .movieOpenDate(LocalDate.of(2023, 1, i))
                    .movieGenre(Movie.MovieGenre.드라마)
                    .movieRunningTime(90)
                    .sawMovies(sawMovies)
                    .build();
            movies.add(movie);
        }
        for (int i = 21; i < 31; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .moviePoster("image")
                    .movieDescription("movie description" + i)
                    .movieOpenDate(LocalDate.of(2023, 1, i))
                    .movieGenre(Movie.MovieGenre.코미디)
                    .movieRunningTime(90)
                    .sawMovies(sawMovies)
                    .build();
            movies.add(movie);
        }

        movieRepository.saveAll(movies);
    }
    @AfterEach
    void deleteData() {
        movieRepository.deleteAll();
    }
    @DisplayName("전체 영화조회 시 개봉일순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void get_movie_pagination_sortBy_movieId() {
        //given
        String title = "movie30";

        //when
        Page<Movie> pageInfo = movieRepository
                .findAll(PageRequest.of(0,10, Sort.by("movieId").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getMovieTitle()).isEqualTo(title);
    }

    @DisplayName("영화 검색 시 등록일순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void search_movie_by_title_with_pagination() {
        //given
        String title = "movie2";

        //when
        Page<Movie> pageInfo = movieRepository
                .findMovieByTitleWithPaging(title,PageRequest.of(0,10, Sort.by("movieOpenDate").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getMovieTitle()).isEqualTo("movie29");
        Assertions.assertThat(pageInfo.getContent().size()).isEqualTo(10);
    }

    @DisplayName("영화 장르별로 검색할 수 있다.")
    @Test
    void search_movie_by_genre_with_pagination() {
        //given
        String genre = "코미디";

        //when
        Page<Movie> pageInfo = movieRepository
                .findMovieByGenreWithPaging(genre, PageRequest.of(0, 10, Sort.by("movieId").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getMovieTitle()).isEqualTo("movie30");
        Assertions.assertThat(pageInfo.getContent().size()).isEqualTo(10);
        Assertions.assertThat(pageInfo.getContent().get(0).getMovieGenre()).isEqualTo(Movie.MovieGenre.코미디);
    }

    @DisplayName("영화 제목을 기준으로 데이터를 불러올 수 있다.")
    @Test
    void findMovieByMovieTitle() {
        //given
        String title = "movie1";

        //when
        Optional<Movie> movieOptional = movieRepository.findByMovieTitle(title);

        //then
        Assertions.assertThat(movieOptional).isPresent();
    }

    @DisplayName("영화 제목을 기준으로 데이터를 삭제할 수 있다.")
    @Test
    void deleteByMovieTitle() {
        //given
        String title = "movie1";

        //when
        movieRepository.deleteByMovieTitle(title);
        Optional<Movie> movieOptional = movieRepository.findByMovieTitle(title);

        //then
        Assertions.assertThat(movieOptional).isEmpty();
    }

    @DisplayName("정렬기준이 없으면 NullPointException 발생")
    @Test
    void without_sort_NullPointException() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(()->movieRepository
                        .findMovieByTitleWithPaging("movie", PageRequest.of(0, 10, Sort.by("m").descending())))
                .isInstanceOf(NullPointerException.class);
    }

}
