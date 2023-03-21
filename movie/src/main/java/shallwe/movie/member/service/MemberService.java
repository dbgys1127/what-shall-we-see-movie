package shallwe.movie.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.dto.PagingResponseDto;
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

    // ============================ 일반 유저 요청 처리 메소드 ==============================
    // 1. 회원가입 처리 메소드
    public MemberDto.Response createMember(MemberDto.Post memberDto) {
        verifyExistsEmail(memberDto.getEmail());
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());//aop로 관심사 분리 필요
        List<String> roles = authorityUtils.createRoles(memberDto.getEmail());

        Member member = Member.builder().email(memberDto.getEmail()).password(encryptedPassword).roles(roles).build();
        Member savedMember = memberRepository.save(member);

        MemberDto.Response memberRepDto = getMemberRepDto(savedMember);

        return memberRepDto;
    }

    // 2. 회원 개인 이미지 수정시 메소드
    public MemberDto.Response updateMemberImage(MultipartFile multipartFile, String email) throws IOException {
        Member findMember = is_exist_member(email);
        isUpdateImage(multipartFile, findMember);
        MemberDto.Response memberRepDto = getMemberRepDto(findMember);

        return memberRepDto;
    }

    // 3. 회원 비밀번호 수정시 메소드
    public MemberDto.Response updateMemberPassword(MemberDto.Patch memberDto, String email) {
        Member findMember = is_exist_member(email);
        findMember.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        MemberDto.Response memberRepDto = getMemberRepDto(findMember);

        return memberRepDto;
    }


    // ============================ 관리자 요청 처리 메소드 ==============================
    /** 1. 회원 목록 조회
     * [검색이 없는 회원 목록 조회 시]
     * email -> 전체 검색 가능 하도록 "@"를 전달 받음
     * sort -> 가입일, 시청영화순, 경고수, 차단여부
     * [검색이 있는 회원 목록 조회 시]
     * email -> 검색된 회원만 조회 하도록 직접 입력 받음
     * sort -> 가입일 순
      */
    public PagingResponseDto<MemberDto.Response> searchMember(String email,int page,String sort) {
        Page<Member> pageInfo = memberRepository.findAllMemberWithPaging(email,PageRequest.of(page,10,Sort.by(sort).descending()));
        List<Member> members = pageInfo.getContent();
        List<MemberDto.Response> memberRepDtoList = getMemberRepList(members);
        return new PagingResponseDto<>(memberRepDtoList,pageInfo,email);
    }

    /** 2. 관리자 목록 조회
     *  1번 회원 목록 조회와 기능은 같으나 조회 대상이 ADMIN인 점이 차이
      */
    public PagingResponseDto<MemberDto.Response> searchAdmin(String email,int page,String sort) {
        Page<Member> pageInfo = memberRepository.findAllAdminWithPaging(email,PageRequest.of(page,10,Sort.by(sort).descending()));
        List<Member> members = pageInfo.getContent();
        List<MemberDto.Response> memberRepDtoList = getMemberRepList(members);
        return new PagingResponseDto<>(memberRepDtoList,pageInfo,email);
    }

    // 2. 관리자가 관리하고 싶은 회원을 선택할때 사용됨
    public MemberDto.Response pickMember(String email) {
        Member member = is_exist_member(email);
        MemberDto.Response memberRepDto = getAdminRepDto(member);
        return memberRepDto;
    }

    // 3. 댓글 신고가 접수된 회원을 admin이 경고를 주거나 차단하는 메서드
    public MemberDto.Response giveWarning(String email,String warning, String block) {
        Member member = is_exist_member(email);
        if (warning.equals("on")) {
            member.setWarningCard(member.getWarningCard()+1);
        }
        if (block.equals("on")) {
            member.setMemberStatus(Member.MemberStatus.차단);
        } else {
            member.setMemberStatus(Member.MemberStatus.활성);
        }
        MemberDto.Response memberRepDto = getAdminRepDto(member);
        return memberRepDto;
    }

    // 4. 관리자 추가 처리 메소드
    public MemberDto.Response createAdmin(MemberDto.Post memberDto) {
        verifyExistsEmail(memberDto.getEmail());
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());//aop로 관심사 분리 필요
        List<String> roles = authorityUtils.createRoles(true);

        Member member = Member.builder().email(memberDto.getEmail()).password(encryptedPassword).roles(roles).build();
        Member savedMember = memberRepository.save(member);

        MemberDto.Response memberRepDto = getMemberRepDto(savedMember);

        return memberRepDto;
    }


    //================================= 중복 제거용 메소드 ================================
    //1. 회원 가입시 동일 이메일 중복 가입 방지용 메서드
    public void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_YOUR_EMAIL);
        }
    }

    //2. 회원 단건 조회 용
    public Member is_exist_member(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email); // DB에서 회원 조회
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_CANNOT_FIND));
        return findMember;
    }

    //3. 회원이 개인정보 수정시 이미지를 등록하지 않으면 s3에 이미지를 업로드 하지 않게 한다.
    public void isUpdateImage(MultipartFile multipartFile, Member findMember) throws IOException {
        String url = s3UploadService.upload(multipartFile);
        if (!url.equals("")) {
            findMember.setMemberImage(url);
        }
    }

    //4. 일반 회원의 회원정보에 대한 응답용 Dto 생성 메서드
    public static MemberDto.Response getMemberRepDto(Member findMember) {
        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .memberImage(findMember.getMemberImage())
                .email(findMember.getEmail())
                .password(findMember.getPassword())
                .roles(findMember.getRoles())
                .build();

        return memberRepDto;
    }

    //5. 관리자 회원의 회원정보에 대한 응답용 Dto 생성 메서드
    public static MemberDto.Response getAdminRepDto(Member member) {
        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .warningCard(member.getWarningCard())
                .memberStatus(member.getMemberStatus())
                .build();
        return memberRepDto;
    }

    //6. 회원정보에 대해 list로 반환할때 Dto 생성 메서드
    public static List<MemberDto.Response> getMemberRepList(List<Member> members) {
        List<MemberDto.Response> memberRepDtoList = new ArrayList<>();
        for (Member member : members) {
            MemberDto.Response memberRepDto = getAdminRepDto(member);
            memberRepDtoList.add(memberRepDto);
        }
        return memberRepDtoList;
    }

}
