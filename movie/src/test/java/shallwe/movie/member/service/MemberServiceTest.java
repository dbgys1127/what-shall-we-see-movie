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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.TestConfig;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.security.service.CustomAuthorityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class MemberServiceTest {
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


    @DisplayName("1.회원 등록 테스트")
    @Test
    void createMember() {
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
        MemberDto.Response memberRepDto = memberService.createMember(memberPostDto);

        //then
        Assertions.assertThat(memberRepDto.getEmail()).isEqualTo(memberPostDto.getEmail());
    }

    @DisplayName("2.비밀번호를 수정할 수 있다.")
    @Test
    void updateMemberPassword() {
        //given
        String email = "test@gmail.com";
        MemberDto.Patch memberPatchDto = new MemberDto.Patch();
        memberPatchDto.setPassword("1234!abc");
        Member member = Member.builder()
                .email("test@gmail.com")
                .password("abc!1234")
                .build();
        Optional<Member> memberOp = Optional.of(member);

        //stub
        given(memberRepository.findByEmail(email)).willReturn(memberOp);

        //when
        Member findMember = memberService.is_exist_member("test@gmail.com");
        MemberDto.Response memberRepDto = memberService.updateMemberPassword(memberPatchDto, email);

        //then
        Assertions.assertThat(memberRepDto.getPassword()).isEqualTo(findMember.getPassword());
    }


    @DisplayName("3.이미 등록된 같은 이메일 회원이 존재하면 예외가 발생한다.")
    @Test
    void verifyExistsEmail() {
        //given
        Member member = Member.builder().email("test@gmail.com").build();
        Optional<Member> memberOp = Optional.of(member);

        //stub
        given(memberRepository.findByEmail("test@gmail.com")).willReturn(memberOp);

        //then
        Assertions.assertThatThrownBy(() -> memberService.verifyExistsEmail("test@gmail.com"))
                .isInstanceOf(BusinessLogicException.class);
    }
    @DisplayName("4.찾는 이메일 회원이 있으면 해당 멤버가 반환된다.")
    @Test
    void is_exist_member() {
        //given
        Member member = Member.builder().email("test@gmail.com").build();
        Optional<Member> memberOp = Optional.of(member);

        //stub
        given(memberRepository.findByEmail("test@gmail.com")).willReturn(memberOp);

        //when
        Member findMember = memberService.is_exist_member("test@gmail.com");

        //then
        Assertions.assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("5.찾는 이메일 회원이 존재하지 않으면 예외가 발생한다.")
    @Test
    void is_not_exist_member() {
        //given

        //stub
        given(memberRepository.findByEmail("test@gmail.com")).willReturn(Optional.empty());

        //then
        Assertions.assertThatThrownBy(() -> memberService.is_exist_member("test@gmail.com"))
                .isInstanceOf(BusinessLogicException.class);
    }

    @DisplayName("6.일반회원 요청에 대한 응답은 memberImage와 email을 갖고 있는 MemberDto.Response 객체가 반환된다.")
    @Test
    void getMemberRepDto() {
        //given
        Member member = Member.builder().email("test@gmail.com").build();

        //stub
        //when
        MemberDto.Response memberRepDto = MemberService.getMemberRepDto(member);

        //then
        Assertions.assertThat(memberRepDto.getEmail()).isEqualTo("test@gmail.com");
        Assertions.assertThat(memberRepDto.getMemberImage()).isEqualTo("https://movie-project-bucket.s3.ap-northeast-2.amazonaws.com/4648c38d-87ef-4d66-93ee-15ac8cc373fb-%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%89%E1%85%A1%E1%84%8C%E1%85%B5%E1%86%AB.png");
        Assertions.assertThat(memberRepDto.getMemberStatus()).isNull();
    }

    @DisplayName("7.관리자 요청에 대한 응답은 memberImage, email, memberStatus를 갖고 있는 MemberDto.Response 객체가 반환된다.")
    @Test
    void getAdminRepDto() {
        //given
        Member member = Member.builder().email("test@gmail.com").build();

        //stub
        //when
        MemberDto.Response memberRepDto = MemberService.getAdminRepDto(member);

        //then
        Assertions.assertThat(memberRepDto.getEmail()).isEqualTo("test@gmail.com");
        Assertions.assertThat(memberRepDto.getMemberStatus()).isEqualTo(Member.MemberStatus.활성);
    }
}
