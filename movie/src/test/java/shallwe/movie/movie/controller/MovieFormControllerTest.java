package shallwe.movie.movie.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MovieFormControllerTest {

    @Autowired
    MockMvc mockMvc;

    @DisplayName("관리자 계정은 영화등록 폼에 접근할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void adminAddMovie() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/movie/add-movie-form"))
                .andExpect(view().name("movie/addMovie"));
    }

    @DisplayName("사용자 계정은 영화등록 폼에 접근할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "USER")
    void adminAddMovie_fail() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/movie/add-movie-form"))
                .andExpect(forwardedUrl("/access-denied"));
    }
}
