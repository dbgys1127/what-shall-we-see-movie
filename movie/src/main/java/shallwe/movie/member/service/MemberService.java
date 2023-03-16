package shallwe.movie.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.security.service.CustomAuthorityUtils;

import java.io.IOException;
import java.util.ArrayList;
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
    private final S3UploadService s3UploadService;

    public MemberDto.Response createMember(MemberDto.Post memberDto) {
        verifyExistsEmail(memberDto.getEmail());
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());//aop로 관심사 분리 필요
        List<String> roles = authorityUtils.createRoles(memberDto.getEmail());

        Member member = Member.builder().email(memberDto.getEmail()).password(encryptedPassword).roles(roles).build();
        Member savedMember = memberRepository.save(member);

        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .memberImage(member.getMemberImage())
                .email(member.getEmail()).build();

        return memberRepDto;
    }

    public MemberDto.Response updateMemberImage(MultipartFile multipartFile, String email) throws IOException {
        Member findMember = is_exist_member(email);
        isUpdateImage(multipartFile, findMember);
        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .memberImage(findMember.getMemberImage())
                .build();

        return memberRepDto;
    }
    public MemberDto.Response updateMemberPassword(MemberDto.Patch memberDto, String email) {
        Member findMember = is_exist_member(email);
        findMember.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .memberImage(findMember.getMemberImage())
                .build();

        return memberRepDto;
    }
    private void isUpdateImage(MultipartFile multipartFile, Member findMember) throws IOException {
        String url = s3UploadService.upload(multipartFile);
        if (!url.equals("")) {
            findMember.setMemberImage(url);
        }
    }
    public List<MemberDto.Response> searchMember(String email) {
        List<Member> members = memberRepository.findMemberBySearch(email);
        List<MemberDto.Response> memberRepDtoList = new ArrayList<>();
        for (Member member : members) {
            MemberDto.Response memberRepDto = MemberDto.Response.builder()
                    .email(member.getEmail())
                    .createdAt(member.getCreatedAt())
                    .warningCard(member.getWarningCard())
                    .build();
            memberRepDtoList.add(memberRepDto);
        }
        return memberRepDtoList;
    }
    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_YOUR_EMAIL);
        }
    }

    private Member is_exist_member(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email); // DB에서 회원 조회
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_CANNOT_FIND));
        return findMember;
    }



}
