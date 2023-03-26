package shallwe.movie.movie.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.movie.service.MovieService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MemberMovieControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MovieService movieService;

    @MockBean
    MovieRepository movieRepository;

    @DisplayName("로그인을 하지 않아도 영화 목록을 볼 수 있다.")
    @Test
    void getMovies() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/movie")
                        .param("page", "1")
                        .param("sort", "movieOpenDate"))
                .andExpect(view().name("movie/allMovie"));

    }

    @DisplayName("영화 상세페이지는 로그인하지 않으면 접근할 수 없다.")
    @Test
    void getMovieWithNoLogin() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/movie/detail")
                        .param("movieTitle", "movie1"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("영화 상세페이지는 로그인한 멤버만 접근할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void getMovieWithLogin() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/movie/detail")
                        .param("movieTitle", "movie1"))
                .andExpect(view().name("movie/movieDetail"));
    }

    @DisplayName("로그인하지 않은 멤버는 검색할 수 없다.")
    @Test
    void searchMovieByTitleWithNoLogin() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/movie/search/title")
                        .param("page", "1")
                        .param("movieTitle","movie"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("로그인한 멤버는 검색할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void searchMovieByTitleWithLogin() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/movie/search/title")
                        .param("page", "1")
                        .param("movieTitle","movie"))
                .andExpect(view().name("movie/memberMovieSearchResult"));
    }

    @DisplayName("로그인하지 않은 멤버는 장르별 검색을 할 수 없다.")
    @Test
    void searchMovieByGenreWithNoLogin() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/movie/search/genre")
                        .param("page", "1")
                        .param("movieGenre","코미디"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("로그인한 멤버는 장르별 검색을 할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void searchMovieByGenreWithLogin() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/movie/search/genre")
                        .param("page", "1")
                        .param("movieGenre","코미디"))
                .andExpect(view().name("movie/memberMovieSearchResult"));
    }
}
