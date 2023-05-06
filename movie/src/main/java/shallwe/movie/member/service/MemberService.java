package shallwe.movie.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import java.time.format.DateTimeFormatter;
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

    /**
     * 회원가입 처리 메소드
     */
    @Caching(evict = {
            @CacheEvict(value = "searchMember",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchAdmin",allEntries = true,cacheManager = "contentCacheManager")
    })
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

    /**
     * 회원 이미지 수정용
     * 회원이 수정할 이미지 등록 없이 수정 버튼을 누르면 기존 이미지가 유지된다.
     */
    public MemberDto.Response updateMemberImage(MultipartFile multipartFile, Member member) throws IOException {
        log.info("회원 이미지 수정 시도 -> 수정 예정 회원 이메일 : {}",member.getEmail());
        isUpdateImage(multipartFile, member);
        MemberDto.Response memberRepDto = getMemberRepDto(member);

        return memberRepDto;
    }

    /**
     * 회원 비밀번호 수정용
     */
    public MemberDto.Response updateMemberPassword(MemberDto.Patch memberDto, Member member) {
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        MemberDto.Response memberRepDto = getMemberRepDto(member);
        return memberRepDto;
    }

    /**
     * 마이 페이지 조회용
     * 댓글, 시청한영화, 찜영화 목록은 각각의 객체를 List로 변경하는 작업을 거친다.
     */
    public MemberDto.Response getMyInfo(Member member) {
        log.info("마이페이지 조회 -> 조회 대상 이메일 : {}",member.getEmail());
        MemberDto.Response memberRepDto = getMemberRepDto(member);
        memberRepDto.setSawMovies(MemberDto.getMemberSawMovieResponseDtoList(member.getSawMovies(),5));
        memberRepDto.setWantMovies(MemberDto.getMemberWantMovieResponseDtoList(member.getWantMovies(),5));
        memberRepDto.setComments(MemberDto.getMemberCommentResponseDtoList(member.getComments(),5)
                .stream()
                .sorted(Comparator.comparing(MemberDto.MemberCommentResponseDto::getCommentId).reversed())
                .collect(Collectors.toList())
        );
        return memberRepDto;
    }

    /**
     * 마이페이지에서는 나의 시청한 영화가 5개만 보인다. 더보고 싶을때, 사용됨
     */
    @Cacheable(value = "mySawMovie",key = "#member.email.concat('-').concat(#page)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.MemberSawMovieResponseDto> findMySawMovieList(int page, Member member) {
        log.info("마이페이지 나의 시청영화 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}",member.getEmail(),page);
        Page<SawMovie> pageInfo = sawMovieService.getSawMovieList(member, PageRequest.of(page, 10, Sort.by("movieSawCount").descending()));
        List<SawMovie> sawMovies = pageInfo.getContent();
        List<MemberDto.MemberSawMovieResponseDto> sawMovieResponseDtoList = MemberDto.getMemberSawMovieResponseDtoList(sawMovies,10);
        return new PagingResponseDto<>(sawMovieResponseDtoList,pageInfo);
    }

    /**
     * 마이페이지에서는 나의 찜한 영화가 5개만 보인다. 더보고 싶을때, 사용됨
     */
    @Cacheable(value = "myWantMovie",key = "#member.email.concat('-').concat(#page)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.MemberWantMovieResponseDto> findMyWantMovieList(int page, Member member) {
        log.info("마이페이지 나의 찜영화 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}",member.getEmail(),page);
        Page<WantMovie> pageInfo = wantMovieService.getWantMovieList(member, PageRequest.of(page, 10, Sort.by("createdAtForWantMovie").descending()));
        List<WantMovie> wantMovies = pageInfo.getContent();
        List<MemberDto.MemberWantMovieResponseDto> wantMovieResponseDtoList = MemberDto.getMemberWantMovieResponseDtoList(wantMovies,10);
        return new PagingResponseDto<>(wantMovieResponseDtoList,pageInfo);
    }

    /**
     * 마이페이지에서는 나의 댓글은 5개만 보인다. 더보고 싶을때, 사용됨
     */
    @Cacheable(value = "myComment",key = "#email.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MemberDto.MemberCommentResponseDto> findMyCommentList(int page, String email, String sort) {
        log.info("마이페이지 나의 댓글 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}",email,page);
        Page<Comment> pageInfo = commentService.getCommentList(email, PageRequest.of(page, 10, Sort.by(sort).descending()));
        List<Comment> comments = pageInfo.getContent();
        List<MemberDto.MemberCommentResponseDto> commentResponseDtoList = MemberDto.getMemberCommentResponseDtoList(comments,10);
        return new PagingResponseDto<>(commentResponseDtoList, pageInfo);
    }

    /**
     * 회원 목록 조회
     * @param email : 검색 없이 조회 시 전체 검색 가능 하도록 "@"를 전달 받음, 검색어가 있으면 검색된 회원만 조회 하도록 직접 입력 받음
     * @param sort : 정렬은 가입일 순, 경고수, 차단여부에 따라 정렬된다.
     * @return
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

    /**
     * 관리자 목록 조회
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

    /**
     * 관리자가 경고나 차단하고 싶은 회원을 선택할때 사용됨
     */
    @Cacheable(value = "member",key="#email",cacheManager = "contentCacheManager")
    public MemberDto.Response pickMember(String email) {
        log.info("회원 상세조회 시도 -> 조회 대상 : {}",email);
        Member member = is_exist_member(email);
        MemberDto.Response memberRepDto = getAdminRepDto(member);
        log.info("회원 상세조회 성공 -> 조회 대상 : {}",memberRepDto.getEmail());
        return memberRepDto;
    }

    /**
     * 회원에게 경고를 주고 싶을때 사용됨
     * @param warning : on 일시 경고 횟수가 증가 됨
     */
    @Caching(evict = {
            @CacheEvict(value = "member",key = "#email",cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchMember",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchAdmin",allEntries = true,cacheManager = "contentCacheManager")
    })
    public void giveWarning(String email,String warning) {
        Member member = is_exist_member(email);
        log.info("회원 경고/상태 수정 시도 -> 대상 회원 : {}",email);
        if (warning.equals("on")) {
            member.setWarningCard(member.getWarningCard()+1);
            log.info("회원 경고 완료 -> 경고 대상 회원 : {}, 대상 회원 누적 경고수 : {}",email,member.getWarningCard());
        }
    }

    /**
     * 회원을 차단하고 싶을떄 사용
     * @param block : on 일시 회원상태가 차단되고, off 일사 회원상태가 활성으로 변하고 누적된 경고수가 0이 된다.
     */
    @Caching(evict = {
            @CacheEvict(value = "member",key = "#email",cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchMember",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchAdmin",allEntries = true,cacheManager = "contentCacheManager")
    })
    public void giveBlock(String email, String block) {
        Member member = is_exist_member(email);
        log.info("회원 경고/상태 수정 시도 -> 대상 회원 : {}",email);
        if (block.equals("on")) {
            member.setMemberStatus(Member.MemberStatus.차단);
            log.info("회원 차단 등록 완료 -> 대상 회원 : {}",email);
        } else {
            member.setMemberStatus(Member.MemberStatus.활성);
            member.setWarningCard(0);
            log.info("회원 활성 등록 완료 -> 대상 회원 : {}",email);
        }
    }

    /**
     * 관리자 추가할때 사용됨
     */
    @Caching(evict = {
            @CacheEvict(value = "searchMember",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchAdmin",allEntries = true,cacheManager = "contentCacheManager")
    })
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
    @Caching(evict = {
            @CacheEvict(value = "searchMember",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchAdmin",allEntries = true,cacheManager = "contentCacheManager")
    })
    public PagingResponseDto<MemberDto.Response> deleteAdmin(String email) {
        log.info("관리자 삭제 시도 -> 삭제 예정 이메일 : {}",email);
        memberRepository.deleteByEmail(email);
        log.info("관리자 삭제 완료 -> 삭제 완료 이메일 : {}",email);
        return searchAdmin("@",0,"memberId");
    }

    public int verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        log.info("회원 DB 중복 조회 시도 -> 회원 이메일 : {}",email);
        if (member.isPresent()) {
            log.error("회원 DB 중복 확인 -> 회원 이메일 : {}", email);
            return 1;
        } else {
            return 0;
        }
    }

    public Member is_exist_member(String email) {
        log.info("회원 DB 조회 시도 -> 회원 이메일 : {}",email);
        Optional<Member> optionalMember = memberRepository.findByEmail(email); // DB에서 회원 조회
        if (optionalMember.isEmpty()) {
            log.error("회원 DB 조회 실패 -> 회원 이메일 : {}",email);
        }
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_CANNOT_FIND));
        return findMember;
    }

    public void isUpdateImage(MultipartFile multipartFile, Member findMember) throws IOException {
        String url = s3UploadService.upload(multipartFile);
        if (!url.equals("")) {
            findMember.setMemberImage(url);
            log.info("회원 이미지 수정 성공 -> 수정 완료 이메일 : {}, 이미지 url : {}", findMember.getEmail(), url);
        } else {
            log.info("회원 이미지 수정 실패 -> 수정 시도 이메일 : {}, 이미지 url : {}", findMember.getEmail(), url);
        }
    }

    public static MemberDto.Response getMemberRepDto(Member findMember) {
        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .memberImage(findMember.getMemberImage())
                .email(findMember.getEmail())
                .createdAt(findMember.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .password(findMember.getPassword())
                .roles(findMember.getRoles())
                .sawMoviesTotalCount(findMember.getSawMovies().size())
                .wantMoviesTotalCount(findMember.getWantMovies().size())
                .commentCount(findMember.getComments().size())
                .build();

        return memberRepDto;
    }

    public static MemberDto.Response getAdminRepDto(Member member) {
        MemberDto.Response memberRepDto = MemberDto.Response.builder()
                .email(member.getEmail())
                .createdAt(member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .warningCard(member.getWarningCard())
                .memberStatus(member.getMemberStatus())
                .build();
        return memberRepDto;
    }

    public static List<MemberDto.Response> getMemberRepList(List<Member> members) {
        List<MemberDto.Response> memberRepDtoList = new ArrayList<>();
        for (Member member : members) {
            MemberDto.Response memberRepDto = getAdminRepDto(member);
            memberRepDtoList.add(memberRepDto);
        }
        return memberRepDtoList;
    }



}
