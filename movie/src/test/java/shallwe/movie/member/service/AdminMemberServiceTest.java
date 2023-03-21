package shallwe.movie.member.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.security.service.CustomAuthorityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class AdminMemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    CustomAuthorityUtils authorityUtils;

    @Mock
    S3UploadService s3UploadService;

    @Mock
    private MemberRepository memberRepository;

    private List<Member> members = new ArrayList<>();

    @BeforeEach
    void insertData() {
        for (int i = 1; i <= 10; i++) {
            Member member = Member.builder()
                    .email("test" + i + "@gmail.com")
                    .password("1234!abc")
                    .warningCard(21 - i)
                    .roles(List.of("USER"))
                    .build();
            members.add(member);
        }
        for (int i = 11; i <= 21; i++) {
            Member member = Member.builder()
                    .email("test" + i + "@gmail.com")
                    .password("1234!abc")
                    .warningCard(21 - i)
                    .roles(List.of("USER"))
                    .build();
            members.add(member);
        }
        Member member = Member.builder()
                .email("admin@gmail.com")
                .password("1234!abc")
                .warningCard(0)
                .roles(List.of("USER", "ADMIN"))
                .build();
        members.add(member);
    }

    @DisplayName("1.email로 회원을 조회할 수 있다.")
    @Test
    void pickMember() {
        //given
        String email = "test1@gmail.com";
        Member member = Member.builder()
                .email("test1@gmail.com")
                .password("abc!1234")
                .build();

        //stub
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        //when
        MemberDto.Response memberRepDto = memberService.pickMember(email);

        //then
        Assertions.assertThat(memberRepDto.getEmail()).isEqualTo(email);
    }

    @DisplayName("2.전체 회원조회 시 가입최신순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void searchMember() {
        //given
        String email = "@";
        int page = 1;
        String sort = "memberId";
        Pageable pageable=PageRequest.of(page-1, 10, Sort.by(sort).descending());
        Page<Member> pageInfo = new PageImpl<>(members, pageable, members.size());

        //stub
        given(memberRepository.findAllMemberWithPaging(email, PageRequest.of(page,10, Sort.by(sort).descending()))).willReturn(pageInfo);

        //when
        PagingResponseDto<MemberDto.Response> pagingResponseDto = memberService.searchMember(email, page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo("memberId");
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo("memberId");
        Assertions.assertThat(pagingResponseDto.getData().get(0).getEmail()).isEqualTo(members.get(0).getEmail());
    }

    @DisplayName("3.회원에 대해 관리자는 경고를 줄 수 있다.")
    @Test
    void giveWarning() {
        //given
        String email = "test1@gmail.com";
        Member member = Member.builder()
                .email("test1@gmail.com")
                .password("abc!1234")
                .build();

        //stub
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        //when
        MemberDto.Response memberRepDto = memberService.giveWarning("test1@gmail.com", "on", "off");

        //then
        Assertions.assertThat(memberRepDto.getWarningCard()).isEqualTo(1);
        Assertions.assertThat(memberRepDto.getMemberStatus()).isEqualTo(Member.MemberStatus.활성);

    }

    @DisplayName("4.회원에 대해 관리자는 차단할 수 있다.")
    @Test
    void memberBlock() {
        //given
        String email = "test1@gmail.com";
        Member member = Member.builder()
                .email("test1@gmail.com")
                .password("abc!1234")
                .build();

        //stub
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        //when
        MemberDto.Response memberRepDto = memberService.giveWarning("test1@gmail.com", "off", "on");

        //then
        Assertions.assertThat(memberRepDto.getWarningCard()).isEqualTo(0);
        Assertions.assertThat(memberRepDto.getMemberStatus()).isEqualTo(Member.MemberStatus.차단);
    }

    @DisplayName("5.전체 관리자조회 시 가입최신순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void searchAdmin() {
        //given
        String email = "@";
        int page = 1;
        String sort = "memberId";
        Pageable pageable=PageRequest.of(page-1, 10, Sort.by(sort).descending());
        Page<Member> pageInfo = new PageImpl<>(members, pageable, members.size());

        //stub
        given(memberRepository.findAllAdminWithPaging(email, PageRequest.of(page,10, Sort.by(sort).descending()))).willReturn(pageInfo);

        //when
        PagingResponseDto<MemberDto.Response> pagingResponseDto = memberService.searchAdmin(email, page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo("memberId");
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo("memberId");
        Assertions.assertThat(pagingResponseDto.getData().get(21).getEmail()).isEqualTo(members.get(21).getEmail());
    }

    @DisplayName("6.관리자 추가 테스트")
    @Test
    void addAdmin() {
        //given
        MemberDto.Post memberPostDto = MemberDto.Post.builder()
                .email("test@gmail.com")
                .password("1234!abc")
                .build();
        Member member = Member.builder()
                .email(memberPostDto.getEmail())
                .password(memberPostDto.getPassword())
                .build();

        //stub
        when(memberRepository.save(any())).thenReturn(member);

        //when
        MemberDto.Response memberRepDto = memberService.createAdmin(memberPostDto);

        //then
        Assertions.assertThat(memberRepDto.getEmail()).isEqualTo(memberPostDto.getEmail());
    }
}
