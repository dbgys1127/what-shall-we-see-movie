package shallwe.movie.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import shallwe.movie.TestConfig;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.entity.Member;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
@ActiveProfiles("test")
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void insertData() {
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

    @DisplayName("1. email을 통해 해당 멤버를 가져올 수 있다.")
    @Test
    void find_member_by_email() {
        //given
        //when
        Optional<Member> optionalMember = memberRepository.findByEmail("test1@gmail.com");
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_CANNOT_FIND));

        //then
        Assertions.assertThat(findMember.getEmail()).isEqualTo("test1@gmail.com");
    }

    @DisplayName("2. 전체 회원조회 시 가입최신순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void get_member_pagination_sortBy_memberId() {
        //given
        //when
        Page<Member> pageInfo = memberRepository
                .findAllMemberWithPaging("@", PageRequest.of(0,10, Sort.by("memberId").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getEmail()).isEqualTo("test21@gmail.com");
    }

    @DisplayName("3. 전체 회원조회 시 경고 개수 기준 오름차순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void get_member_pagination_sortBy_warningCard() {
        //given
        //when
        Page<Member> pageInfo = memberRepository
                .findAllMemberWithPaging("@", PageRequest.of(0,10, Sort.by("warningCard").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getWarningCard()).isEqualTo(20);
    }

    @DisplayName("4. 전체 회원조회 시 회원 차단 여부로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void get_member_pagination_sortBy_memberStatus() {
        //given
        //when
        Page<Member> pageInfo = memberRepository
                .findAllMemberWithPaging("@", PageRequest.of(0,10, Sort.by("memberStatus").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getMemberStatus()).isEqualTo(Member.MemberStatus.차단);
    }

    @DisplayName("5. 정렬기준이 없으면 NullPointException 발생")
    @Test
    void without_sort_NullPointException() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(()->memberRepository
                        .findAllMemberWithPaging("@", PageRequest.of(0, 10, Sort.by("m").descending())))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("6. email로 검색 될때도 해당 멤버들이 페이지네이션 적용된다.")
    @Test
    void search_member_by_email_with_pagination() {
        //given
        //when
        Page<Member> pageInfo = memberRepository
                .findAllMemberWithPaging("test2", PageRequest.of(0,10, Sort.by("memberId").descending()));

        //then
        Assertions.assertThat(pageInfo.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(pageInfo.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(pageInfo.getContent().get(0).getEmail()).isEqualTo("test21@gmail.com");

    }
    @DisplayName("7. 전체 관리자조회 시 가입최신순으로 적용되어 데이터를 불러올 수 있다.")
    @Test
    void get_admin_pagination_sortBy_memberId() {
        //given
        //when
        Page<Member> pageInfo = memberRepository
                .findAllAdminWithPaging("@", PageRequest.of(0,10, Sort.by("memberId").descending()));

        //then
        Assertions.assertThat(pageInfo.getContent().get(0).getEmail()).isEqualTo("admin@gmail.com");
    }
}
