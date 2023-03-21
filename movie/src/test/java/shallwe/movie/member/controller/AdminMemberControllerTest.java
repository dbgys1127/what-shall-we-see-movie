package shallwe.movie.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AdminMemberControllerTest {
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
        for (int i = 1; i <= 10; i++) {
            Member member = Member.builder()
                    .email("test" + i + "@gmail.com")
                    .password("1234!abc")
                    .warningCard(21 - i)
                    .roles(List.of("USER"))
                    .build();
            memberRepository.save(member);
        }
        for (int i = 11; i <= 21; i++) {
            Member member = Member.builder()
                    .email("test" + i + "@gmail.com")
                    .password("1234!abc")
                    .warningCard(21 - i)
                    .roles(List.of("USER"))
                    .build();
            member.setMemberStatus(Member.MemberStatus.차단);
            memberRepository.save(member);
        }
        Member member = Member.builder()
                .email("admin@gmail.com")
                .password("1234!abc")
                .warningCard(0)
                .roles(List.of("USER", "ADMIN"))
                .build();
        memberRepository.save(member);
    }

    @AfterEach
    void delete_data() {
        memberRepository.deleteAll();
    }

    @DisplayName("1.관리자는 회원에 대해 경고를 줄 수 있다.")
    @Test
    @WithMockUser(username = "test", roles = "ADMIN")
    void adminPatchWarning() throws Exception {
        //given
        String email = "test1@gmail.com";

        //when
        mockMvc.perform(post("/admin/member/warning")
                .param("email", email)
                .param("warning", "on")).andExpect(view().name("member/warning"));
        Member member=memberService.is_exist_member(email);

        //then
        Assertions.assertThat(member.getWarningCard()).isEqualTo(21);
        Assertions.assertThat(member.getMemberStatus()).isEqualTo(Member.MemberStatus.활성);
    }

    @DisplayName("2.관리자는 회원을 차단할 수 있다.")
    @Test
    @WithMockUser(username = "test", roles = "ADMIN")
    void adminPatchBlock() throws Exception {
        //given
        String email = "test1@gmail.com";

        //when
        mockMvc.perform(post("/admin/member/warning")
                .param("email", email)
                .param("block", "on")).andExpect(view().name("member/warning"));
        Member member=memberService.is_exist_member(email);

        //then
        Assertions.assertThat(member.getWarningCard()).isEqualTo(20);
        Assertions.assertThat(member.getMemberStatus()).isEqualTo(Member.MemberStatus.차단);
    }

    @DisplayName("3.관리자는 페이징 처리된 회원 목록을 볼수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void adminGetMembers() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/member")
                        .param("page", "1"))
                .andExpect(view().name("member/member"));
    }

    @DisplayName("4.관리자는 검색된 회원 목록을 페이징 처리된 목록으로 볼수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void getMemberBySearch() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/member/search")
                        .param("page", "1")
                        .param("email","test"))
                .andExpect(view().name("member/memberSearchResult"));
    }

    @DisplayName("5.관리자는 페이징 처리된 관리자 목록을 볼수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void adminGetAdmin() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/admin/administrator")
                        .param("page", "1"))
                .andExpect(view().name("member/admins"));
    }

    @DisplayName("6.관리자는 관리자를 추가할 수 있다.")
    @Test
    @WithMockUser(username = "test",roles = "ADMIN")
    void adminAddAdmin() throws Exception {
        //given
        MemberDto.Post memberPostDto = MemberDto.Post.builder()
                .email("test@gmail.com")
                .password("1234!abc")
                .build();

        //when
        //then
        mockMvc.perform(post("/admin/administrator/add")
                        .param("email",memberPostDto.getEmail())
                        .param("password",memberPostDto.getPassword()))
                .andExpect(model().attribute("email","test@gmail.com"))
                .andExpect(view().name("member/join"));
    }
}
