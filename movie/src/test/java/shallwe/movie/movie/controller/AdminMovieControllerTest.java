package shallwe.movie.movie.controller;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.movie.service.MovieService;
import shallwe.movie.s3.S3UploadService;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AdminMovieControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    S3UploadService s3UploadService;

    @MockBean
    MovieService movieService;

    @MockBean
    MovieRepository movieRepository;



    @DisplayName("관리자는 영화를 등록할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void postMovie() throws Exception {
        //given
        String movieTitle = "movie";
        String movieRunningTime = "90";
        String movieDescription = "영화 설명";
        String movieGenre = "드라마";
        String movieOpenDate = "2023-01-01";

        Movie movie = Movie.builder()
                .movieTitle(movieTitle)
                .moviePoster("moviePoster")
                .movieRunningTime(90)
                .movieGenre(Movie.MovieGenre.드라마)
                .movieDescription(movieDescription)
                .movieOpenDate(LocalDate.of(2023, 1, 1)).build();

        MovieDto.Response movieRepDto = MovieDto.Response.builder()
                .movieTitle(movieTitle)
                .moviePoster("moviePoster")
                .movieRunningTime(90)
                .movieGenre(Movie.MovieGenre.드라마)
                .movieDescription(movieDescription)
                .movieOpenDate(LocalDate.of(2023, 1, 1)).build();

        MockMultipartFile file = new MockMultipartFile("moviePoster",
                "test.png",
                "text/plain",
                "MyImage".getBytes());

        //stub
        given(movieRepository.save(any())).willReturn(movie);
        given(movieRepository.findByMovieTitle(any())).willReturn(Optional.empty());
        given(movieService.createMovie(any(), any())).willReturn(movieRepDto);

        //when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/admin/movie")
                .file(file)
                .param("movieTitle", movieTitle)
                .param("movieRunningTime", movieRunningTime)
                .param("movieDescription", movieDescription)
                .param("movieGenre", movieGenre)
                .param("movieOpenDate", movieOpenDate);

        //then
        mockMvc.perform(request).andExpect(view().name("movie/movie"));
    }

    @DisplayName("영화를 등록중 모든 요소를 기입하지 않으면 오류가 발생한다..")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void postMovieWithException() throws Exception {
        //given
        String movieTitle = "movie";
        String movieRunningTime = "90";
        String movieDescription = "영화 설명";
        String movieGenre = "드라마";
        String movieOpenDate = "2023-01-01";

        Movie movie = Movie.builder()
                .movieTitle(movieTitle)
                .moviePoster("moviePoster")
                .movieRunningTime(90)
                .movieGenre(Movie.MovieGenre.드라마)
                .movieDescription(movieDescription)
                .movieOpenDate(LocalDate.of(2023, 1, 1)).build();

        MovieDto.Response movieRepDto = MovieDto.Response.builder()
                .movieTitle(movieTitle)
                .moviePoster("moviePoster")
                .movieRunningTime(90)
                .movieGenre(Movie.MovieGenre.드라마)
                .movieDescription(movieDescription)
                .movieOpenDate(LocalDate.of(2023, 1, 1)).build();

        MockMultipartFile file = new MockMultipartFile("moviePoster",
                "test.png",
                "text/plain",
                "MyImage".getBytes());

        //stub
        given(movieRepository.save(any())).willReturn(movie);
        given(movieRepository.findByMovieTitle(any())).willReturn(Optional.empty());
        given(movieService.createMovie(any(), any())).willReturn(movieRepDto);

        //when
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/admin/movie")
                .file(file)
                .param("movieRunningTime", movieRunningTime)
                .param("movieDescription", movieDescription)
                .param("movieGenre", movieGenre)
                .param("movieOpenDate", movieOpenDate);

        //then
        MvcResult result=mockMvc.perform(request).andExpect(view().name("movie/movie")).andReturn();

        Assertions.assertThat(result).isNotNull();
    }

    @DisplayName("관리자는 페이징 처리된 영화 목록을 볼수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void adminGetMembers() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/movie")
                        .param("page", "1"))
                .andExpect(view().name("movie/movies"));
    }

    @DisplayName("관리자는 페이징 처리된 영화검색 목록을 볼수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void getMemberBySearch() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/movie/search")
                        .param("movieTitle","movie")
                        .param("page", "1"))
                .andExpect(view().name("movie/movieSearchResult"));
    }
}
