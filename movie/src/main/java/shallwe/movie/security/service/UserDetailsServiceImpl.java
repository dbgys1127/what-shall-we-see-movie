package shallwe.movie.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username); // DB에서 회원 조회
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_CANNOT_FIND));
        return new CustomUserDetails(findMember);
    }

    private class CustomUserDetails extends Member implements UserDetails {
        public CustomUserDetails(Member member) {
            setId(member.getId());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setMemberImage("이미지");
            setWarningCard(0);
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
