package shallwe.movie.movie.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.s3.S3UploadService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class MemberMovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private S3UploadService s3UploadService;

    @Mock
    private AmazonS3 amazonS3;

    private List<Movie> movies = new ArrayList<>();

    private Page<Movie> pageInfo;

    @BeforeEach
    void insertData() {
        for (int i = 1; i < 11; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .movieGenre(Movie.MovieGenre.드라마)
                    .build();
            movies.add(movie);
        }
        for (int i = 11; i < 21; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .movieGenre(Movie.MovieGenre.코미디)
                    .build();
            movies.add(movie);
        }
        for (int i = 21; i < 31; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .movieGenre(Movie.MovieGenre.액션)
                    .build();
            movies.add(movie);
        }
    }

    @DisplayName("장르별로 영화 목록을 조회할 수 있다.")
    @Test
    void searchMovieByGenre() {
        //given
        int page = 0;
        String sort = "movieId";
        String genre = "코미디";
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        pageInfo = new PageImpl<>(movies, pageable, movies.size());

        //stub
        given(movieRepository.findMovieByGenreWithPaging(genre, pageable)).willReturn(pageInfo);

        //when
        PagingResponseDto<MovieDto.Response> pagingResponseDto = movieService.searchMovieByGenre(genre, page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo(sort);
        Assertions.assertThat(pagingResponseDto.getEndPage()).isEqualTo(3);
    }
}
