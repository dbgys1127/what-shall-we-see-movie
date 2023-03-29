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
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.service.SawMovieService;
import shallwe.movie.security.service.CustomAuthorityUtils;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.service.WantMovieService;

import java.util.ArrayList;
import java.util.List;
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
    SawMovieService sawMovieService;

    @Mock
    WantMovieService wantMovieService;

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
        given(passwordEncoder.encode(any())).willReturn("1234!abc");
        given(memberRepository.save(any())).willReturn(member);

        //when
        MemberDto.Response memberRepDto = memberService.updateMemberPassword(memberPatchDto, email);

        //then
        Assertions.assertThat(memberRepDto.getPassword()).isEqualTo("1234!abc");
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

    @DisplayName("8.관리자 페이징 List로 반환된다")
    @Test
    void getMemberRepList() {
        //given
        Member member1 = Member.builder().email("test1@gmail.com").build();
        Member member2 = Member.builder().email("test2@gmail.com").build();
        Member member3 = Member.builder().email("test3@gmail.com").build();

        List<Member> members = new ArrayList<>();

        members.add(member1);
        members.add(member2);
        members.add(member3);

        //stub
        //when
        List<MemberDto.Response> memberRepDtoList = MemberService.getMemberRepList(members);

        //then
        Assertions.assertThat(memberRepDtoList.size()).isEqualTo(3);
    }

    @DisplayName("9.멤버는 마이페이지에서 자신이 시청횟수를 등록한 영화 목록을 볼 수 있다.")
    @Test
    void getMyInfoMySawMovies() {
        //given
        String email = "test@gmail.com";
        Member member = Member.builder()
                .email(email)
                .warningCard(0)
                .build();

        List<SawMovie> sawMovies = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Movie movie = Movie.builder()
                    .moviePoster("이미지")
                    .movieTitle("movie" + i)
                    .build();
            SawMovie sawMovie = SawMovie.builder()
                    .movieSawCount(i)
                    .movie(movie)
                    .build();
            sawMovies.add(sawMovie);
        }

        member.setSawMovies(sawMovies);

        //stub
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        MemberDto.Response memberRepDto = memberService.getMyInfo(email);

        //then
        Assertions.assertThat(memberRepDto.getSawMovies().size()).isEqualTo(10);
    }

    @DisplayName("10.멤버는 마이페이지 영화 목록에서 더보기를 누르면 시청횟수를 등록한 페이징 처리된 영화 목록 볼 수 있다.")
    @Test
    void findMySawMovieList() {
        //given
        int page = 0;
        String email = "test@gmail.com";
        Member member = Member.builder()
                .email(email)
                .warningCard(0)
                .build();

        List<SawMovie> sawMovies = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Movie movie = Movie.builder()
                    .moviePoster("이미지")
                    .movieTitle("movie" + i)
                    .build();
            SawMovie sawMovie = SawMovie.builder()
                    .movieSawCount(i)
                    .movie(movie)
                    .build();
            sawMovies.add(sawMovie);
        }

        member.setSawMovies(sawMovies);

        Pageable pageable = PageRequest.of(page, 10, Sort.by("avgSawCount").descending());
        Page<SawMovie> pageInfo = new PageImpl<>(sawMovies, pageable, sawMovies.size());

        //stub
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(sawMovieService.getSawMovieList(member, PageRequest.of(page, 10, Sort.by("avgSawCount").descending())))
                .willReturn(pageInfo);

        //when
        PagingResponseDto<MemberDto.MemberSawMovieResponseDto> result = memberService.findMySawMovieList(page,email);

        //then
        Assertions.assertThat(result.getData().size()).isEqualTo(10);
        Assertions.assertThat(result.getSort()).isEqualTo("avgSawCount");
        Assertions.assertThat(result.getNowPage()).isEqualTo(1);
    }

    @DisplayName("11.멤버는 마이페이지에서 자신이 찜 등록한 영화 목록을 볼 수 있다.")
    @Test
    void getMyInfoMyWantMovie() {
        //given
        String email = "test@gmail.com";
        Member member = Member.builder()
                .email(email)
                .warningCard(0)
                .build();

        List<SawMovie> sawMovies = new ArrayList<>();
        List<WantMovie> wantMovies = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Movie movie = Movie.builder()
                    .moviePoster("이미지")
                    .movieTitle("movie" + i)
                    .build();
            SawMovie sawMovie = SawMovie.builder()
                    .movieSawCount(i)
                    .movie(movie)
                    .build();
            WantMovie wantMovie = WantMovie.builder()
                    .wantMovieId((long) i)
                    .movie(movie)
                    .build();
            sawMovies.add(sawMovie);
            wantMovies.add(wantMovie);
        }

        member.setSawMovies(sawMovies);
        member.setWantMovies(wantMovies);

        //stub
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        //when
        MemberDto.Response memberRepDto = memberService.getMyInfo(email);

        //then
        Assertions.assertThat(memberRepDto.getSawMovies().size()).isEqualTo(10);
        Assertions.assertThat(memberRepDto.getWantMovies().size()).isEqualTo(10);
    }

    @DisplayName("12.멤버는 마이페이지 찜영화 목록에서 더보기를 누르면 찜을 등록한 페이징 처리된 영화 목록 볼 수 있다.")
    @Test
    void findMyWantMovieList() {
        //given
        int page = 0;
        String email = "test@gmail.com";
        Member member = Member.builder()
                .email(email)
                .warningCard(0)
                .build();

        List<SawMovie> sawMovies = new ArrayList<>();
        List<WantMovie> wantMovies = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Movie movie = Movie.builder()
                    .moviePoster("이미지")
                    .movieTitle("movie" + i)
                    .build();
            SawMovie sawMovie = SawMovie.builder()
                    .movieSawCount(i)
                    .movie(movie)
                    .build();
            WantMovie wantMovie = WantMovie.builder()
                    .wantMovieId((long) i)
                    .movie(movie)
                    .build();
            sawMovies.add(sawMovie);
            wantMovies.add(wantMovie);
        }

        member.setSawMovies(sawMovies);
        member.setWantMovies(wantMovies);

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<WantMovie> pageInfo = new PageImpl<>(wantMovies, pageable, wantMovies.size());

        //stub
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(wantMovieService.getWantMovieList(member, PageRequest.of(page, 10, Sort.by("createdAt").descending())))
                .willReturn(pageInfo);

        //when
        PagingResponseDto<MemberDto.MemberWantMovieResponseDto> result = memberService.findMyWantMovieList(page,email);

        //then
        Assertions.assertThat(result.getData().size()).isEqualTo(10);
        Assertions.assertThat(result.getSort()).isEqualTo("createdAt");
        Assertions.assertThat(result.getNowPage()).isEqualTo(1);
    }
}
