package shallwe.movie.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init_data() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .password("1234!abc")
                .memberImage("이미지")
                .warningCard(1)
                .id(1L)
                .build();
        memberService.createMember(member);
    }
    @AfterEach
    void delete_data() {
        memberRepository.deleteAll();
    }
    @DisplayName("1. 올바른 회원정보 입력시 테스트 성공")
    @Test
    void is_right_member() throws Exception {
        mockMvc.perform(formLogin("/process_login").user("test@gmail.com").password("1234!abc"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }
    @DisplayName("2. 틀린 회원정보 입력시 인증 실패")
    @Test
    void is_wrong_member() throws Exception {
        mockMvc.perform(formLogin("/process_login").user("test1@gmail.com").password("1234!abc"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login-form?error"));

    }
}
