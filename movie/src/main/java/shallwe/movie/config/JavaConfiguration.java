package shallwe.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.member.service.MemberServiceImpl;
import shallwe.movie.security.service.CustomAuthorityUtils;

@Configuration
public class JavaConfiguration {
    @Bean
    public MemberService memberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, CustomAuthorityUtils authorityUtils) {
        return new MemberServiceImpl(memberRepository, passwordEncoder, authorityUtils);
    }
}
