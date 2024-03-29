package shallwe.movie.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;

import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    void init_data() {
            Member member = Member.builder()
                    .email("test1@gmail.com")
                    .password("1234!abc")
                    .warningCard(0)
                    .roles(List.of("USER"))
                    .build();
            memberRepository.save(member);
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
        MemberDto.Post member = MemberDto.Post.builder().email("dbgys1127@gmail.com").password("1234!abc!").build();

        //when
        //then
        mockMvc.perform(post("/join")
                .param("email",member.getEmail())
                .param("password",member.getPassword()))
                .andExpect(model().attribute("email","dbgys1127@gmail.com"))
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

    @DisplayName("9.회원 비밀번호는 수정될 수 있다.")
    @WithMockUser(username = "test1@gmail.com",roles = "USER",password = "1234!abc")
    @Test
    void patchMyPassword() throws Exception {
        //given
        MemberDto.Patch memberPatchDto = new MemberDto.Patch();
        memberPatchDto.setPassword("abc!1234");

        //when
        mockMvc.perform(post("/mypage/myPassword")
                .param("password", memberPatchDto.getPassword())).andReturn();

        Member member=memberService.is_exist_member("test1@gmail.com");

        //then
        assertThat(passwordEncoder.matches("abc!1234", member.getPassword())).isTrue();
    }

    @DisplayName("10.회원은 시청횟수를 등록한 영화 목록내 더보기를 통해 페이징 처리된 영화 목록을 볼 수 있다.")
    @Test
    @WithMockUser(username = "test1@gmail.com",roles = "USER")
    void getMySawMovieList() throws Exception {
        //given
        Member member = Member.builder()
                .email("test1@gmail.com")
                .password("1234!abc")
                .warningCard(0)
                .roles(List.of("USER"))
                .build();

        //when
        //then
        mockMvc.perform(get("/mypage/saw-movie")
                        .param("page", "1")
                        .param("email","test"))
                .andExpect(view().name("member/sawMovieList"));
    }

    @DisplayName("11.회원은 시청횟수를 등록한 영화 목록내 더보기를 통해 페이징 처리된 영화 목록을 볼 수 있다.")
    @Test
    @WithMockUser(username = "test1@gmail.com",roles = "USER")
    void getMyWantMovieList() throws Exception {
        //given
        Member member = Member.builder()
                .email("test1@gmail.com")
                .password("1234!abc")
                .warningCard(0)
                .roles(List.of("USER"))
                .build();

        //when
        //then
        mockMvc.perform(get("/mypage/want-movie")
                        .param("page", "1")
                        .param("email","test"))
                .andExpect(view().name("member/wantMovieList"));
    }
    @DisplayName("12.회원은 등록했던 댓글을 영화 목록내 더보기를 통해 페이징 처리된 댓글 목록을 볼 수 있다.")
    @Test
    @WithMockUser(username = "test1@gmail.com",roles = "USER")
    void getMyCommentList() throws Exception {
        //given
        Member member = Member.builder()
                .email("test1@gmail.com")
                .password("1234!abc")
                .warningCard(0)
                .roles(List.of("USER"))
                .build();

        //when
        //then
        mockMvc.perform(get("/mypage/comment")
                        .param("page", "1")
                        .param("sort","claimCount"))
                .andExpect(view().name("member/commentList"));
    }
    //주석처리 후 마지막에 풀기
//    @DisplayName("10.회원 이미지는 수정될 수 있다.")
//    @WithMockUser(username = "test1@gmail.com",roles = "USER",password = "1234!abc")
//    @Test
//    void patchMyImage() throws Exception {
//        //given
//        MockMultipartFile file = new MockMultipartFile("myImage",
//                "test.png",
//                "text/plain",
//                "MyImage".getBytes());
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Member member = new Member();
//        member.setEmail(email);
//
//        //when
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.multipart("/my-info/myImage")
//                .file(file)
//                .flashAttr("member",member);
//
//        mockMvc.perform(request).andExpect(status().isOk());
//    }

}
