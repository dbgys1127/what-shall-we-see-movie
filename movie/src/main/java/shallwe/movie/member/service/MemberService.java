package shallwe.movie.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.security.service.CustomAuthorityUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public MemberDto.Response createMember(MemberDto.Post memberDto) {
        verifyExistsEmail(memberDto.getEmail());
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());
        List<String> roles = authorityUtils.createRoles(memberDto.getEmail());

        Member member = Member.builder().email(memberDto.getEmail()).password(encryptedPassword).roles(roles).build();
        Member savedMember = memberRepository.save(member);

        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .memberImage(member.getMemberImage())
                .email(member.getEmail()).build();

        return memberRepDto;
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_YOUR_EMAIL);
        }
    }
}
