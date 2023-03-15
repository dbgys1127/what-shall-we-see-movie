package shallwe.movie.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shallwe.movie.TestConfig;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.member.dto.MemberDto;
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
        MemberDto.Post memberDto = new MemberDto.Post();
        memberDto.setEmail("test@gmail.com");
        memberDto.setPassword("1234!abc");
        Member savedMember =memberService.createMember(memberDto);
        assertThat(savedMember.getEmail()).isEqualTo(memberDto.getEmail());
        assertThat(savedMember.getRoles().get(0)).isEqualTo("USER");
    }

    @DisplayName("2. 메일이 이미 존재할때 존재한다는 예외가 나오는지 테스트")
    @Test
    void is_exist_member() {
        MemberDto.Post memberDto = new MemberDto.Post();
        memberDto.setEmail("test@gmail.com");
        memberDto.setPassword("1234!abc");
        Member savedMember =memberService.createMember(memberDto);
        MemberDto.Post memberDto1 = new MemberDto.Post();
        memberDto1.setEmail("test@gmail.com");
        memberDto1.setPassword("1234!abc");
        assertThatThrownBy(() -> memberService.createMember(memberDto1))
                .isInstanceOf(BusinessLogicException.class);
    }
}