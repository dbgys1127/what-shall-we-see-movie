package shallwe.movie.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.comment.service.CommentService;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.service.SawMovieService;
import shallwe.movie.security.service.CustomAuthorityUtils;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.service.WantMovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final S3UploadService s3UploadService;
    private final SawMovieService sawMovieService;
    private final WantMovieService wantMovieService;
    private final CommentService commentService;

    // ============================ 일반 유저 요청 처리 메소드 ==============================
    // 1. 회원가입 처리 메소드
    public MemberDto.Response createMember(MemberDto.Post memberDto) {
        log.info("회원 가입 시도 -> 가입 예정 이메일 : {}",memberDto.getEmail());
        verifyExistsEmail(memberDto.getEmail());
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());//aop로 관심사 분리 필요
        List<String> roles = authorityUtils.createRoles(memberDto.getEmail());

        Member member = Member.builder().email(memberDto.getEmail()).password(encryptedPassword).roles(roles).build();
        Member savedMember = memberRepository.save(member);

        MemberDto.Response memberRepDto = getMemberRepDto(savedMember);
        log.info("회원 가입 성공 -> 가입 이메일 : {}",savedMember.getEmail());
        return memberRepDto;
    }

    // 2. 회원 개인 이미지 수정시 메소드
    public MemberDto.Response updateMemberImage(MultipartFile multipartFile, Member member) throws IOException {
        log.info("회원 이미지 수정 시도 -> 수정 예정 회원 이메일 : {}",member.getEmail());
        isUpdateImage(multipartFile, member);
        MemberDto.Response memberRepDto = getMemberRepDto(member);

        return memberRepDto;
    }

    // 3. 회원 비밀번호 수정시 메소드
    public MemberDto.Response updateMemberPassword(MemberDto.Patch memberDto, Member member) {
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        MemberDto.Response memberRepDto = getMemberRepDto(member);
        return memberRepDto;
    }

    public MemberDto.Response getMyInfo(Member member) {
        log.info("마이페이지 조회 -> 조회 대상 이메일 : {}",member.getEmail());
        MemberDto.Response memberRepDto = getMemberRepDto(member);
        memberRepDto.setSawMovies(MemberDto.getMemberSawMovieResponseDtoList(member.getSawMovies()));
        memberRepDto.setWantMovies(MemberDto.getMemberWantMovieResponseDtoList(member.getWantMovies()));
        memberRepDto.setComments(MemberDto.getMemberCommentResponseDtoList(member.getComments())
                .stream()
                .sorted(Comparator.comparing(MemberDto.MemberCommentResponseDto::getCommentId).reversed())
                .collect(Collectors.toList())
        );
        return memberRepDto;
    }
    @Cacheable(value = "mySawMovie",key = "#member.email.concat('-').concat(#page)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.MemberSawMovieResponseDto> findMySawMovieList(int page, Member member) {
        log.info("마이페이지 나의 시청영화 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}",member.getEmail(),page);
        Page<SawMovie> pageInfo = sawMovieService.getSawMovieList(member, PageRequest.of(page, 10, Sort.by("movieSawCount").descending()));
        List<SawMovie> sawMovies = pageInfo.getContent();
        List<MemberDto.MemberSawMovieResponseDto> sawMovieResponseDtoList = MemberDto.getMemberSawMovieResponseDtoList(sawMovies);
        return new PagingResponseDto<>(sawMovieResponseDtoList,pageInfo);
    }

    @Cacheable(value = "myWantMovie",key = "#member.email.concat('-').concat(#page)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.MemberWantMovieResponseDto> findMyWantMovieList(int page, Member member) {
        log.info("마이페이지 나의 찜영화 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}",member.getEmail(),page);
        Page<WantMovie> pageInfo = wantMovieService.getWantMovieList(member, PageRequest.of(page, 10, Sort.by("createdAtForWantMovie").descending()));
        List<WantMovie> wantMovies = pageInfo.getContent();
        List<MemberDto.MemberWantMovieResponseDto> wantMovieResponseDtoList = MemberDto.getMemberWantMovieResponseDtoList(wantMovies);
        return new PagingResponseDto<>(wantMovieResponseDtoList,pageInfo);
    }

    @Cacheable(value = "myComment",key = "#email.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.MemberCommentResponseDto> findMyCommentList(int page, String email, String sort) {
        log.info("마이페이지 나의 댓글 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}",email,page);
        Page<Comment> pageInfo = commentService.getCommentList(email, PageRequest.of(page, 10, Sort.by(sort).descending()));
        List<Comment> comments = pageInfo.getContent();
        List<MemberDto.MemberCommentResponseDto> commentResponseDtoList = MemberDto.getMemberCommentResponseDtoList(comments);
        return new PagingResponseDto<>(commentResponseDtoList, pageInfo);
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
    @Cacheable(value = "searchMember",key = "#email.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.Response> searchMember(String email,int page,String sort) {
        log.info("회원 검색 시도 -> 검색어 : {}",email);
        Page<Member> pageInfo = memberRepository.findAllMemberWithPaging(email,PageRequest.of(page,10,Sort.by(sort).descending()));
        List<Member> members = pageInfo.getContent();
        List<MemberDto.Response> memberRepDtoList = getMemberRepList(members);
        log.info("회원 검색 결과 -> 검색어 : {}, 검색된 결과 : {}",email,pageInfo.getTotalElements());
        return new PagingResponseDto<>(memberRepDtoList,pageInfo,email,"");
    }

    /** 2. 관리자 목록 조회
     *  1번 회원 목록 조회와 기능은 같으나 조회 대상이 ADMIN인 점이 차이
      */
    @Cacheable(value = "searchAdmin",key = "#email.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.Response> searchAdmin(String email,int page,String sort) {
        log.info("관리자 검색 시도 -> 검색어 : {}",email);
        Page<Member> pageInfo = memberRepository.findAllAdminWithPaging(email,PageRequest.of(page,10,Sort.by(sort).descending()));
        List<Member> members = pageInfo.getContent();
        List<MemberDto.Response> memberRepDtoList = getMemberRepList(members);
        log.info("관리자 검색 결과 -> 검색어 : {}, 검색된 결과 : {}",email,pageInfo.getTotalElements());
        return new PagingResponseDto<>(memberRepDtoList,pageInfo,email,"");
    }

    // 2. 관리자가 관리하고 싶은 회원을 선택할때 사용됨
    public MemberDto.Response pickMember(String email) {
        log.info("회원 상세조회 시도 -> 조회 대상 : {}",email);
        Member member = is_exist_member(email);
        MemberDto.Response memberRepDto = getAdminRepDto(member);
        log.info("회원 상세조회 성공 -> 조회 대상 : {}",memberRepDto.getEmail());
        return memberRepDto;
    }

    // 3. 댓글 신고가 접수된 회원을 admin이 경고를 주거나 차단하는 메서드
    public MemberDto.Response giveWarning(String email,String warning, String block) {
        Member member = is_exist_member(email);
        log.info("회원 경고/상태 수정 시도 -> 대상 회원 : {}",email);
        if (warning.equals("on")) {
            member.setWarningCard(member.getWarningCard()+1);
            log.info("회원 경고 완료 -> 경고 대상 회원 : {}, 대상 회원 누적 경고수 : {}",email,member.getWarningCard());
        }
        if (block.equals("on")) {
            member.setMemberStatus(Member.MemberStatus.차단);
            log.info("회원 차단 등록 완료 -> 대상 회원 : {}",email);
        } else {
            member.setMemberStatus(Member.MemberStatus.활성);
            log.info("회원 활성 등록 완료 -> 대상 회원 : {}",email);
        }
        MemberDto.Response memberRepDto = getAdminRepDto(member);
        return memberRepDto;
    }

    // 4. 관리자 추가 처리 메소드
    public MemberDto.Response createAdmin(MemberDto.Post memberDto) {
        log.info("관리자 가입 시도 -> 가입 예정 이메일 : {}",memberDto.getEmail());
        verifyExistsEmail(memberDto.getEmail());
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());//aop로 관심사 분리 필요
        List<String> roles = authorityUtils.createRoles(true);

        Member member = Member.builder().email(memberDto.getEmail()).password(encryptedPassword).roles(roles).build();
        Member savedMember = memberRepository.save(member);

        MemberDto.Response memberRepDto = getMemberRepDto(savedMember);
        log.info("관리자 가입 성공 -> 가입 이메일 : {}",savedMember.getEmail());
        return memberRepDto;
    }

    public PagingResponseDto<MemberDto.Response> deleteAdmin(String email) {
        log.info("관리자 삭제 시도 -> 삭제 예정 이메일 : {}",email);
        memberRepository.deleteByEmail(email);
        log.info("관리자 삭제 완료 -> 삭제 완 이메일 : {}",email);
        return searchAdmin("@",0,"memberId");
    }

    //================================= 중복 제거용 메소드 ================================
    //1. 회원 가입시 동일 이메일 중복 가입 방지용 메서드
    public void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        log.info("회원 DB 중복 조회 시도 -> 회원 이메일 : {}",email);
        if (member.isPresent()) {
            log.error("회원 DB 중복 확인 -> 회원 이메일 : {}",email);
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_YOUR_EMAIL);
        }
    }

    //2. 회원 단건 조회 용
    public Member is_exist_member(String email) {
        log.info("회원 DB 조회 시도 -> 회원 이메일 : {}",email);
        Optional<Member> optionalMember = memberRepository.findByEmail(email); // DB에서 회원 조회
        if (optionalMember.isEmpty()) {
            log.error("회원 DB 조회 실패 -> 회원 이메일 : {}",email);
        }
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_CANNOT_FIND));
        return findMember;
    }

    //3. 회원이 개인정보 수정시 이미지를 등록하지 않으면 s3에 이미지를 업로드 하지 않게 한다.
    public void isUpdateImage(MultipartFile multipartFile, Member findMember) throws IOException {
        String url = s3UploadService.upload(multipartFile);
        if (!url.equals("")) {
            findMember.setMemberImage(url);
            log.info("회원 이미지 수정 성공 -> 수정 완료 이메일 : {}, 이미지 url : {}", findMember.getEmail(), url);
        } else {
            log.info("회원 이미지 수정 실패 -> 수정 시도 이메일 : {}, 이미지 url : {}", findMember.getEmail(), url);
        }
    }

    //4. 일반 회원의 회원정보에 대한 응답용 Dto 생성 메서드
    public static MemberDto.Response getMemberRepDto(Member findMember) {
        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .memberImage(findMember.getMemberImage())
                .email(findMember.getEmail())
                .password(findMember.getPassword())
                .roles(findMember.getRoles())
                .sawMoviesTotalCount(findMember.getSawMovies().size())
                .wantMoviesTotalCount(findMember.getWantMovies().size())
                .commentCount(findMember.getComments().size())
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
