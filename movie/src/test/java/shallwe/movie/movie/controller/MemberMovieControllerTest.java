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
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.movie.service.MovieService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @DisplayName("멤버는 영화 시청횟수를 수정할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void postSawCount() throws Exception {
        //given
        String movieTitle = "movie";
        String email = "test@gmail.com";
        int movieSawCount = 2;

        MovieDto.Response movieRepDto = MovieDto.Response.builder().build();

        //stub
        //when
        //then
        mockMvc.perform(post("/movie/saw-movie")
                        .param("movieTitle", movieTitle)
                        .param("movieSawCount", String.valueOf(movieSawCount)))
                .andExpect(view().name("redirect:/movie/detail"));

    }

    @DisplayName("멤버는 찜 상태를 수정할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void postWantMovie() throws Exception {
        //given
        String movieTitle = "movie";
        String email = "test@gmail.com";
        String isWant = "on";

        MovieDto.Response movieRepDto = MovieDto.Response.builder().build();

        //stub
        //when
        //then
        mockMvc.perform(post("/movie/want-movie")
                        .param("movieTitle", movieTitle)
                        .param("wantMovie", String.valueOf(isWant)))
                .andExpect(view().name("redirect:/movie/detail"));

    }

    @DisplayName("멤버는 댓글을 등록할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void postComment() throws Exception {
        //given
        String movieTitle = "movie";
        CommentDto.Post comment = CommentDto.Post.builder()
                .commentDetail("comment").build();

        //stub
        //when
        //then
        mockMvc.perform(post("/movie/comment")
                        .param("movieTitle", movieTitle)
                        .param("commentDetail", comment.getCommentDetail()))
                .andExpect(view().name("redirect:/movie/detail"));

    }

    @DisplayName("멤버는 댓글을 신고할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void postCommentClaim() throws Exception {
        //given
        String movieTitle = "movie";
        Long commentId =1L;

        //stub
        //when
        //then
        mockMvc.perform(post("/movie/comment/claim")
                        .param("movieTitle", movieTitle)
                        .param("commentId", String.valueOf(commentId)))
                .andExpect(view().name("redirect:/movie/detail"));
    }

    @DisplayName("멤버는 댓글을 삭제할 수 있다.")
    @WithMockUser(username = "test",roles = "USER")
    @Test
    void deleteComment() throws Exception {
        //given
        String movieTitle = "movie";
        Long commentId =1L;

        //stub
        //when
        //then
        mockMvc.perform(post("/movie/comment/delete")
                        .param("movieTitle", movieTitle)
                        .param("commentId", String.valueOf(commentId)))
                .andExpect(view().name("redirect:/movie/detail"));
    }
}
