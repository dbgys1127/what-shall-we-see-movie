package shallwe.movie.movie.repository;

import org.assertj.core.api.Assertions;
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

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
@ActiveProfiles("test")
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void insertData() {
        for (int i = 1; i < 21; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .moviePoster("image")
                    .movieDescription("movie description" + i)
                    .movieOpenDate(LocalDate.of(2023, 1, i))
                    .movieGenre(Movie.MovieGenre.드라마)
                    .movieRunningTime(90)
                    .build();
            movieRepository.save(movie);
        }
    }
    @DisplayName("전체 영화조회 시 등록일순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void get_movie_pagination_sortBy_movieId() {
        //given
        String title = "movie20";

        //when
        Page<Movie> pageInfo = movieRepository
                .findAll(PageRequest.of(0,10, Sort.by("movieId").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getMovieTitle()).isEqualTo(title);
    }

    @DisplayName("영화조회 검색 시 등록일순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void search_movie_by_title_with_pagination() {
        //given
        String title = "movie2";

        //when
        Page<Movie> pageInfo = movieRepository
                .findMovieByTitleWithPaging(title,PageRequest.of(0,10, Sort.by("movieId").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getMovieTitle()).isEqualTo("movie20");
        Assertions.assertThat(pageInfo.getContent().size()).isEqualTo(2);
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
