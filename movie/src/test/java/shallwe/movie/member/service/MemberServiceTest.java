package shallwe.movie.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shallwe.movie.TestConfig;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @DisplayName("1. 회원가입이 정확하게 되는지와 비밀번호가 암호화되는가?")
    @Test
    void is_save_member() {
        Member member = new Member(1L,"dbgys@gmail.com","1234","이미지",1);
        Member savedMember = memberService.createMember(member);
        System.out.println(savedMember.getPassword());
        assertThat(savedMember.getEmail()).isEqualTo("dbgys@gmail.com");
    }

    @DisplayName("2. 메일이 이미 존재할때 존재한다는 예외가 나오는지 테스트")
    @Test
    void is_exist_member() {
        Member member = new Member(1L,"dbgys@gmail.com","1234","이미지",1);
        Member savedMember1 = memberService.createMember(member);
        Member member1 = new Member(2L, "dbgys@gmail.com", "1234", "이미지", 1);
        assertThatThrownBy(() -> memberService.createMember(member1))
                .isInstanceOf(BusinessLogicException.class);
    }

}