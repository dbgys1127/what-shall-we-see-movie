package shallwe.movie.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init_data() {

    }

    @AfterEach
    void delete_data() {
        memberRepository.deleteAll();
    }
    @DisplayName("1.회원 가입시 이메일과 비밀번호를 공란으로 두면 예외 발생")
    @Test
    void email_and_password_cannot_be_blanked() throws Exception {
        //given
        MemberDto.Post member = MemberDto.Post.builder().email("").password("").build();

        //when
        MvcResult result=mockMvc.perform(post("/join")
                .param("email",member.getEmail())
                .param("password",member.getPassword())).andReturn();

        String error = result.getResolvedException().getMessage();

        //then
        assertThat(error).contains("이메일 주소를 다시 기입하세요."
                ,"최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요."
                ,"이메일은 공백일 수 없습니다.","비밀번호는 공백일 수 없습니다.");

    }
    @DisplayName("2.회원 가입시 이메일을 공란으로 두면 예외 발생")
    @Test
    void email_cannot_be_blanked() throws Exception {
        //given
        MemberDto.Post member = MemberDto.Post.builder().email("").password("1234!abc").build();


        //when
        MvcResult result = mockMvc.perform(post("/join")
                .param("email",member.getEmail())
                .param("password",member.getPassword())).andReturn();

        String error = result.getResolvedException().getMessage();

        //then
        assertThat(error).doesNotContain("최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요."
                ,"비밀번호는 공백일 수 없습니다.");
    }
    @DisplayName("3.회원 가입시 비밀번호를 공란으로 두면 예외 발생")
    @Test
    void password_cannot_be_blanked() throws Exception {
        //given
        MemberDto.Post member = MemberDto.Post.builder().email("dbgys@gmail.com").password("").build();

        //when
        MvcResult result=mockMvc.perform(post("/join")
                .param("email",member.getEmail())
                .param("password",member.getPassword())).andReturn();

        String error = result.getResolvedException().getMessage();

        //then
        assertThat(error).doesNotContain("이메일 주소를 다시 기입하세요.","이메일은 공백일 수 없습니다.");
    }
    @DisplayName("4.회원 가입시 이메일 양식에 어긋나면 예외 발생")
    @Test
    void with_incorrect_email() throws Exception {
        //given
        MemberDto.Post member = MemberDto.Post.builder().email("dbgysgmail.com").password("1234!abc").build();

        //when
        MvcResult result=mockMvc.perform(post("/join")
                .param("email",member.getEmail())
                .param("password",member.getPassword())).andReturn();

        String error = result.getResolvedException().getMessage();

        //then
        assertThat(error).doesNotContain("최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요."
                ,"이메일은 공백일 수 없습니다.","비밀번호는 공백일 수 없습니다.");
    }
    @DisplayName("5.회원 가입시 비밀번호 양식에 어긋나면 예외 발생")
    @Test
    void with_incorrect_password() throws Exception {
        //given
        MemberDto.Post member = MemberDto.Post.builder().email("dbgys@gmail.com").password("123abc").build();

        //when
        MvcResult result=mockMvc.perform(post("/join")
                .param("email",member.getEmail())
                .param("password",member.getPassword())).andReturn();

        String error = result.getResolvedException().getMessage();

        //then
        assertThat(error).doesNotContain("이메일 주소를 다시 기입하세요."
                ,"이메일은 공백일 수 없습니다.","비밀번호는 공백일 수 없습니다.");
    }
    @DisplayName("6.이메일과 비밀번호를 올바르게 입력시 유효성 검증 성공")
    @Test
    void with_correct_password_email() throws Exception {
        //given
        MemberDto.Post member = MemberDto.Post.builder().email("dbgys@gmail.com").password("1234!abc").build();

        //when
        mockMvc.perform(post("/join")
                .param("email",member.getEmail())
                .param("password",member.getPassword()))
                .andExpect(model().attribute("email","dbgys@gmail.com"))
                .andExpect(view().name("member/join"));
    }
    @DisplayName("7.올바른 회원정보 입력시 테스트 성공")
    @Test
    void is_right_member() throws Exception {
        MemberDto.Post memberDto = MemberDto.Post.builder().email("test@gmail.com").password("1234!abc").build();
        memberService.createMember(memberDto);
        mockMvc.perform(formLogin("/process_login").user("test@gmail.com").password("1234!abc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @DisplayName("8.틀린 회원정보 입력시 인증 실패")
    @Test
    void is_wrong_member() throws Exception {
        MemberDto.Post memberDto = MemberDto.Post.builder().email("test@gmail.com").password("123!abc").build();
        memberService.createMember(memberDto);
        mockMvc.perform(formLogin("/process_login").user("test1@gmail.com").password("1234!abc"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login-form?error"));

    }

    @DisplayName("9.로그인 하지 않는 멤버가 마이 페이지에 접근하려 하면 로그인 창으로 이동된다.")
    @Test
    void not_login_member_cannot_access_my_page() throws Exception {
        mockMvc.perform(get("/mypage"))
                .andExpect(redirectedUrl("http://localhost/login-form"));
    }

    @DisplayName("10.로그인한 일반 회원은 mypage에 접근할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "USER")
    void login_member_can_access_my_page() throws Exception {
        mockMvc.perform(get("/mypage"))
                .andExpect(view().name("member/mypage"));
    }

    @DisplayName("11.접근권한이 없는 멤버가 접근시 접근금지 페이지가 렌더링 된다.")
    @Test
    @WithMockUser(username = "test",roles = "USER")
    void unAuthorized_member_cannot_access_some_page() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(forwardedUrl("/access-denied"));
    }

    @DisplayName("12.접근권한이 있는 멤버가 접근시 특정 페이지가 렌더링 된다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void Authorized_member_can_access_some_page() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(view().name("member/admin"));
    }

    @DisplayName("13.login-form으로 접근시 login.jsp로 흘러간다.")
    @Test
    void loginform_access() throws Exception {
        mockMvc.perform(get("/login-form"))
                .andExpect(view().name("member/login"));
    }
    @DisplayName("14.access-denied로 접근시 accessdenied.jsp로 흘러간다.")
    @Test
    void access_denied() throws Exception {
        mockMvc.perform(get("/access-denied"))
                .andExpect(view().name("member/accessdenied"));
    }
    @DisplayName("15.logout-form으로 접근시 logout.jsp로 흘러간다.")
    @Test
    void logout_form() throws Exception {
        mockMvc.perform(get("/logout-form"))
                .andExpect(view().name("member/logout"));
    }
    @DisplayName("16.join-form으로 접근시 join-form.jsp로 흘러간다.")
    @Test
    void join_form() throws Exception {
        mockMvc.perform(get("/join-form"))
                .andExpect(view().name("member/join-form"));
    }
}
