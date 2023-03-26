package shallwe.movie.movie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.s3.S3UploadService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class AdminMovieServiceTest {
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
        for (int i = 1; i <= 31; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .build();
            movies.add(movie);
        }
    }

    @DisplayName("영화 등록 테스트")
    @Test
    void createMovie() throws IOException {
        //given
        MovieDto.Post moviePostDto = MovieDto.Post.builder()
                .movieTitle("movie")
                .movieRunningTime(90)
                .movieDescription("description")
                .movieGenre(Movie.MovieGenre.드라마)
                .movieOpenDate(LocalDate.of(2023,1,1))
                .build();
        Movie movie = Movie.builder()
                .movieTitle(moviePostDto.getMovieTitle())
                .moviePoster("이미지")
                .movieRunningTime(moviePostDto.getMovieRunningTime())
                .movieDescription(moviePostDto.getMovieDescription())
                .movieGenre(moviePostDto.getMovieGenre())
                .movieOpenDate(moviePostDto.getMovieOpenDate())
                .build();
        MockMultipartFile multipartFile = new MockMultipartFile("movie", "test.png", "text/plain", "movie".getBytes());

        //stub
        given(movieRepository.save(any())).willReturn(movie);
        given(s3UploadService.upload(any())).willReturn("movie");

        //when
        MovieDto.Response movieRepDto = movieService.createMovie(multipartFile,moviePostDto);

        //then
        Assertions.assertThat(movieRepDto.getMovieTitle()).isEqualTo("movie");
        Assertions.assertThat(movieRepDto.getMovieRunningTime()).isEqualTo(90);
    }

    @DisplayName("이미 동일한 이름으로 영화가 등록되었다면 예외가 발생한다.")
    @Test
    void verifyExistsTitle() throws IOException {
        //given
        MovieDto.Post moviePostDto = MovieDto.Post.builder()
                .movieTitle("movie")
                .movieRunningTime(90)
                .movieDescription("description")
                .movieGenre(Movie.MovieGenre.드라마)
                .movieOpenDate(LocalDate.of(2023,1,1))
                .build();
        Movie movie = Movie.builder()
                .movieTitle(moviePostDto.getMovieTitle())
                .moviePoster("이미지")
                .movieRunningTime(moviePostDto.getMovieRunningTime())
                .movieDescription(moviePostDto.getMovieDescription())
                .movieGenre(moviePostDto.getMovieGenre())
                .movieOpenDate(moviePostDto.getMovieOpenDate())
                .build();

        //stub
        given(movieRepository.findByMovieTitle(any())).willReturn(Optional.of(movie));

        //when
        //then
        Assertions.assertThatThrownBy(() -> movieService.verifyExistsTitle("movie")).isInstanceOf(BusinessLogicException.class);
    }

    @DisplayName("영화 목록 조회시 영화를 등록한 순으로 페이징 처리된 데이터를 불러온다.")
    @Test
    void findAllMovie() {
        //given
        int page = 1;
        String sort = "movieId";
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sort).descending());
        pageInfo = new PageImpl<>(movies, pageable, movies.size());

        //stub
        given(movieRepository.findAll(PageRequest.of(page, 10, Sort.by(sort).descending()))).willReturn(pageInfo);


        //when
        PagingResponseDto<MovieDto.Response> pagingResponseDto = movieService.findAllMovie(page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo(sort);
        Assertions.assertThat(pagingResponseDto.getEndPage()).isEqualTo(4);
    }

    @DisplayName("영화 검색시 영화를 등록한 순으로 페이징 처리된 데이터를 불러온다.")
    @Test
    void adminSearchMovie() {
        //given
        int page = 0;
        String sort = "movieId";
        String title = "movie1";
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        pageInfo = new PageImpl<>(movies, pageable, movies.size());

        //stub
        given(movieRepository.findMovieByTitleWithPaging(title,pageable)).willReturn(pageInfo);

        //when
        PagingResponseDto<MovieDto.Response> pagingResponseDto = movieService.searchMovieByTitle(title,page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo(sort);
        Assertions.assertThat(pagingResponseDto.getEndPage()).isEqualTo(4);
    }

    @DisplayName("영화 제목으로 데이터를 불러올 수 있다.")
    @Test
    void pickMovie() {
        //given
        String title = "movie1";
        Optional<Movie> movieOptional = Optional.of(movies.get(0));

        //stub
        given(movieRepository.findByMovieTitle(any())).willReturn(movieOptional);

        //when
        MovieDto.Response movieRepDto = movieService.pickMovie(title);

        //then
        Assertions.assertThat(movieRepDto.getMovieTitle()).isEqualTo("movie1");
    }

    @DisplayName("영화 제목으로 검색된 결과가 없으면 예외가 발생한다.")
    @Test
    void pickNoMovie() {
        //given
        String title = "영화";
        Optional<Movie> movieOptional = Optional.of(movies.get(0));

        //stub
        given(movieRepository.findByMovieTitle(any())).willReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(()->movieService.pickMovie(title)).isInstanceOf(BusinessLogicException.class);
    }

    @DisplayName("영화 속성을 수정할 수 있다.")
    @Test
    void updateMovie() throws IOException {
        //given
        MovieDto.Patch moviePatchDto = MovieDto.Patch.builder()
                .movieTitle("movieUpdate")
                .movieRunningTime(90)
                .movieDescription("description")
                .movieGenre(Movie.MovieGenre.드라마)
                .movieOpenDate(LocalDate.of(2023,1,1))
                .build();
        Movie movie = Movie.builder()
                .movieTitle("movie")
                .moviePoster("이미지")
                .movieRunningTime(moviePatchDto.getMovieRunningTime())
                .movieDescription(moviePatchDto.getMovieDescription())
                .movieGenre(moviePatchDto.getMovieGenre())
                .movieOpenDate(moviePatchDto.getMovieOpenDate())
                .build();
        Optional<Movie> movieOptional = Optional.of(movie);
        MockMultipartFile multipartFile = new MockMultipartFile("movie", "test.png", "text/plain", "movie".getBytes());

        //stub
        given(movieRepository.findByMovieTitle(any())).willReturn(movieOptional);
        given(s3UploadService.upload(any())).willReturn("movie");

        //when
        MovieDto.Response movieRepDto = movieService.updateMovie(multipartFile,moviePatchDto);

        //then
        Assertions.assertThat(movieRepDto.getMovieTitle()).isEqualTo("movieUpdate");
        Assertions.assertThat(movieRepDto.getMovieRunningTime()).isEqualTo(90);
    }
}
