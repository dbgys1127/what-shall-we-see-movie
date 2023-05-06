package shallwe.movie.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.aop.NeedEmail;
import shallwe.movie.aop.NeedMember;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.member.dto.MemberDto;

import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;

import javax.validation.Valid;
import java.io.IOException;



@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    //==========================사용자 요청 처리 API=========================

    /**
     * 회원 가입시 이메일 중복 체크용
     */
    @PostMapping("/email/check")
    @ResponseBody
    public int checkEmail(@RequestParam("email") String email) {
        int cnt = memberService.verifyExistsEmail(email);
        return cnt;
    }

    /**
     * 회원 가입용
     */
    @PostMapping("/join")
    public String join(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        MemberDto.Response saveMember = memberService.createMember(memberDto);
        model.addAttribute("email", saveMember.getEmail());
        model.addAttribute("memberImage", saveMember.getMemberImage());
        return "member/login";
    }

    /**
     * 마이페이지 조회용
     */
    @NeedMember
    @GetMapping("/mypage")
    public String mypage(Member member, Model model) {
        MemberDto.Response memberRepDto = memberService.getMyInfo(member);
        model.addAttribute("member", memberRepDto);
        return "member/mypage";
    }

    /**
     * 나의 이미지 수정용
     */
    @NeedMember
    @PostMapping("/mypage/myImage")
    public String patchMyImage(Member member,@RequestPart("myImage") MultipartFile multipartFile) throws IOException {
        memberService.updateMemberImage(multipartFile, member);
        return "redirect:/mypage";
    }

    /**
     * 비밀번호 수정용
     */
    @NeedMember
    @PostMapping("/mypage/myPassword")
    @ResponseBody
    public void patchMyPassword(Member member,@ModelAttribute @Valid MemberDto.Patch memberDto) throws IOException {
        memberService.updateMemberPassword(memberDto, member);
    }

    /**
     * 마이 페이지에서 내가 시청한 영화를 더 조회 하고 싶을때 사용
     */
    @NeedMember
    @GetMapping("/mypage/saw-movie")
    public String getMySawMovieList(Member member, @RequestParam(value = "page", defaultValue = "1") int page,
                                     Model model) {
        PagingResponseDto<MemberDto.MemberSawMovieResponseDto> sawMovieResponseDtoList = memberService.findMySawMovieList(page - 1, member);
        model.addAttribute("pageData", sawMovieResponseDtoList);
        return "member/sawMovieList";
    }

    /**
     * 마이 페이지에서 내가 찜한 영화를 더 조회 하고 싶을때 사용
     */
    @NeedMember
    @GetMapping("/mypage/want-movie")
    public String getMyWantMovieList(Member member, @RequestParam(value = "page", defaultValue = "1") int page,
                                      Model model) {
        PagingResponseDto<MemberDto.MemberWantMovieResponseDto> wantMovieResponseDtoList = memberService.findMyWantMovieList(page - 1, member);
        model.addAttribute("pageData", wantMovieResponseDtoList);
        return "member/wantMovieList";
    }

    /**
     * 마이 페이지에서 나의 댓글을 더 조회 하고 싶을때 사용
     */
    @NeedEmail
    @GetMapping("/mypage/comment")
    public String getMyCommentList(String email, @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "sort",defaultValue = "createdAtForComment") String sort,
                                    Model model) {
        PagingResponseDto<MemberDto.MemberCommentResponseDto> commentResponseDtoList = memberService.findMyCommentList(page - 1, email,sort);
        model.addAttribute("pageData", commentResponseDtoList);
        return "member/commentList";
    }

    //==========================관리자 요청 처리 컨트롤러=========================

    /**
     * 관리자가 경고 주고 싶은 회원을 클릭하면 넘아가는 페이지
     */
    @GetMapping("/admin/member/warning-page")
    public String adminGetWarning(@RequestParam("email") String email, Model model) {
        MemberDto.Response memberRepDto = memberService.pickMember(email);
        model.addAttribute("email", memberRepDto.getEmail());
        model.addAttribute("warningCard", memberRepDto.getWarningCard());
        model.addAttribute("memberStatus", memberRepDto.getMemberStatus());
        return "member/admin/warning";
    }

    /**
     * 관리자가 회원에게 경고 등록
     */
    @PostMapping("/admin/member/warning")
    @ResponseBody
    public void adminPatchWarning(@RequestParam("email") String email,
                                    @RequestParam(value = "warning", defaultValue = "off") String warning) {
        memberService.giveWarning(email, warning);
    }

    /**
     * 관리자가 회원을 차단/활성 등록
     */
    @PostMapping("/admin/member/block")
    @ResponseBody
    public void adminPatchBlock(@RequestParam("email") String email,
                                  @RequestParam(value = "block", defaultValue = "off") String block) {
        memberService.giveBlock(email,block);
    }

    /**
     * 관리자용 회원 목록 조회
     */
    @GetMapping("/admin/member")
    public String adminGetMembers(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "sort", defaultValue = "memberId") String sort, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchMember("@", page - 1, sort);
        log.info("MEM_startPage={}", pageRepDto.getStartPage());
        model.addAttribute("pageData", pageRepDto);
        return "member/admin/member";
    }

    /**
     * 관리자용 회원 검색
     */
    @GetMapping("/admin/member/search")
    public String getMemberBySearch(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "email", required = false) String email, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchMember(email, page - 1, "memberId");
        model.addAttribute("pageData", pageRepDto);
        return "member/admin/memberSearchResult";
    }

    /**
     * 관리자 조회
     */
    @GetMapping("/admin/administrator")
    public String adminGetAdmins(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "sort", defaultValue = "memberId") String sort, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchAdmin("@", page - 1, sort);
        model.addAttribute("pageData", pageRepDto);
        return "member/admin/admins";
    }

    /**
     * 관리자 검색
     */
    @GetMapping("/admin/administrator/search")
    public String adminGetAdminsBySearch(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "email", required = false) String email, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchAdmin(email, page - 1, "memberId");
        model.addAttribute("pageData", pageRepDto);
        return "member/admin/admins";
    }

    /**
     * 관리자 추가
     */
    @PostMapping("/admin/administrator/add")
    public String adminAddAdmin(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        MemberDto.Response saveMember = memberService.createAdmin(memberDto);
        model.addAttribute("email", saveMember.getEmail());
        model.addAttribute("memberImage", saveMember.getMemberImage());
        return "member/join";
    }

    /**
     * 관리자 삭제
     */
    @PostMapping("/admin/administrator/delete")
    @ResponseBody
    public void adminDeleteAdmin(@RequestParam("email") String email) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.deleteAdmin(email);
    }

    /**
     * 관리자가 신고된 댓글을 파악하기 위한 API
     */
    @GetMapping("/admin/comment")
    public String adminGetCommentList(@RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "sort",defaultValue = "claimCount") String sort,
                                      Model model) {
        PagingResponseDto<MemberDto.MemberCommentResponseDto> commentResponseDtoList = memberService.findMyCommentList(page - 1, "@",sort);
        model.addAttribute("pageData", commentResponseDtoList);
        return "member/admin/claimCommentList";
    }
}
