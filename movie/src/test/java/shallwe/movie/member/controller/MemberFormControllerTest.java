package shallwe.movie.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberFormControllerTest {
    @Autowired
    MockMvc mockMvc;

    @DisplayName("1.login-form으로 접근시 login.jsp로 흘러간다.")
    @Test
    void loginform_access() throws Exception {
        mockMvc.perform(get("/login-form"))
                .andExpect(view().name("member/login"));
    }

    @DisplayName("2.access-denied로 접근시 accessdenied.jsp로 흘러간다.")
    @Test
    void access_denied() throws Exception {
        mockMvc.perform(get("/access-denied"))
                .andExpect(view().name("member/accessdenied"));
    }

    @DisplayName("3.logout-form으로 접근시 logout.jsp로 흘러간다.")
    @Test
    void logout_form() throws Exception {
        mockMvc.perform(get("/logout-form"))
                .andExpect(view().name("member/logout"));
    }

    @DisplayName("4.join-form으로 접근시 join-form.jsp로 흘러간다.")
    @Test
    void join_form() throws Exception {
        mockMvc.perform(get("/join-form"))
                .andExpect(view().name("member/join-form"));
    }

    @DisplayName("5.로그인한 일반 회원은 mypage에 접근할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "USER")
    void login_member_can_access_my_page() throws Exception {
        mockMvc.perform(get("/mypage"))
                .andExpect(view().name("member/mypage"));
    }

    @DisplayName("6.로그인 하지 않는 멤버가 마이 페이지에 접근하려 하면 로그인 창으로 이동된다.")
    @Test
    void not_login_member_cannot_access_my_page() throws Exception {
        mockMvc.perform(get("/mypage"))
                .andExpect(redirectedUrl("http://localhost/login-form"));
    }

    @DisplayName("7.로그인한 일반 회원은 myImage에 접근할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "USER")
    void login_member_can_access_myImage_page() throws Exception {
        mockMvc.perform(get("/my-info/myImage"))
                .andExpect(view().name("member/myImage"));
    }

    @DisplayName("8.로그인한 일반 회원은 myPassword에 접근할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "USER")
    void login_member_can_access_myPassword_page() throws Exception {
        mockMvc.perform(get("/my-info/myPassword"))
                .andExpect(view().name("member/myPassword"));
    }

    @DisplayName("9.접근권한이 없는 멤버가 접근시 접근금지 페이지가 렌더링 된다.")
    @Test
    @WithMockUser(username = "test",roles = "USER")
    void unAuthorized_member_cannot_access_some_page() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(forwardedUrl("/access-denied"));
    }

    @DisplayName("10.접근권한이 있는 멤버가 접근시 특정 페이지가 렌더링 된다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void Authorized_member_can_access_some_page() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(view().name("member/admin"));
    }
}
