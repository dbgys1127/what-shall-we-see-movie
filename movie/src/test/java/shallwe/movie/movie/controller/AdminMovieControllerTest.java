package shallwe.movie.movie.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AdminMovieControllerTest {
    @Autowired
    MockMvc mockMvc;

    @DisplayName("관리자는 페이징 처리된 영화 목록을 볼수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void adminGetMembers() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/movie")
                        .param("page", "1"))
                .andExpect(view().name("movie/movie"));
    }
}
