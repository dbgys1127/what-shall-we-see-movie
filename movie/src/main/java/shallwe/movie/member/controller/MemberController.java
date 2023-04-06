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


    //==========================사용자 화면 컨트롤러=========================

    // 수정할 것
    // 유효성 검증 실패에 대한 내용 view 전달 방법 필요
    @PostMapping("/join")
    public String join(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        MemberDto.Response saveMember = memberService.createMember(memberDto);
        model.addAttribute("email", saveMember.getEmail());
        model.addAttribute("memberImage", saveMember.getMemberImage());
        return "member/join";
    }

    @NeedMember
    @GetMapping("/mypage")
    public String mypage(Member member, Model model) {
        MemberDto.Response memberRepDto = memberService.getMyInfo(member);
        model.addAttribute("member", memberRepDto);
        return "member/mypage";
    }

    @NeedMember
    @PostMapping("/mypage/myImage")
    public String patchMyImage(Member member,@RequestPart("myImage") MultipartFile multipartFile,
                               Model model) throws IOException {
        MemberDto.Response patchMember = memberService.updateMemberImage(multipartFile, member);
        model.addAttribute("member", patchMember);
        return "member/mypage";
    }

    @NeedMember
    @PostMapping("/mypage/myPassword")
    public String patchMyPassword(Member member,@ModelAttribute @Valid MemberDto.Patch memberDto,
                                  Model model) throws IOException {
        MemberDto.Response patchMember = memberService.updateMemberPassword(memberDto, member);
        model.addAttribute("member", patchMember);
        return "member/mypage";
    }

    @NeedMember
    @GetMapping("/mypage/saw-movie")
    public String getMySawMovieList(Member member, @RequestParam(value = "page", defaultValue = "1") int page,
                                     Model model) {
        PagingResponseDto<MemberDto.MemberSawMovieResponseDto> sawMovieResponseDtoList = memberService.findMySawMovieList(page - 1, member);
        model.addAttribute("pageData", sawMovieResponseDtoList);
        return "member/sawMovieList";
    }

    @NeedMember
    @GetMapping("/mypage/want-movie")
    public String getMyWantMovieList(Member member, @RequestParam(value = "page", defaultValue = "1") int page,
                                      Model model) {
        PagingResponseDto<MemberDto.MemberWantMovieResponseDto> wantMovieResponseDtoList = memberService.findMyWantMovieList(page - 1, member);
        model.addAttribute("pageData", wantMovieResponseDtoList);
        return "member/wantMovieList";
    }

    @NeedEmail
    @GetMapping("/mypage/comment")
    public String getMyCommentList(String email, @RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "sort",defaultValue = "createdAtForComment") String sort,
                                    Model model) {
        PagingResponseDto<MemberDto.MemberCommentResponseDto> commentResponseDtoList = memberService.findMyCommentList(page - 1, email,sort);
        model.addAttribute("pageData", commentResponseDtoList);
        return "member/commentList";
    }

    //==========================관리자 화면 컨트롤러=========================

    @GetMapping("/admin/member/warning-page")
    public String adminGetWarning(@RequestParam("email") String email, Model model) {
        MemberDto.Response memberRepDto = memberService.pickMember(email);
        model.addAttribute("email", memberRepDto.getEmail());
        model.addAttribute("warningCard", memberRepDto.getWarningCard());
        model.addAttribute("memberStatus", memberRepDto.getMemberStatus());
        return "member/warning";
    }

    @PostMapping("/admin/member/warning")
    public String adminPatchWarning(@RequestParam("email") String email,
                                    @RequestParam(value = "warning", defaultValue = "off") String warning,
                                    @RequestParam(value = "block", defaultValue = "off") String block, Model model) {
        MemberDto.Response memberRepDto = memberService.giveWarning(email, warning, block);
        model.addAttribute("email", memberRepDto.getEmail());
        model.addAttribute("warningCard", memberRepDto.getWarningCard());
        model.addAttribute("memberStatus", memberRepDto.getMemberStatus());
        return "member/warning";
    }

    @GetMapping("/admin/member")
    public String adminGetMembers(@RequestParam("page") int page,
                                  @RequestParam(value = "sort", defaultValue = "memberId") String sort, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchMember("@", page - 1, sort);
        log.info("MEM_startPage={}", pageRepDto.getStartPage());
        model.addAttribute("pageData", pageRepDto);
        return "member/member";
    }

    @GetMapping("/admin/member/search")
    public String getMemberBySearch(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "email", required = false) String email, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchMember(email, page - 1, "memberId");
        model.addAttribute("pageData", pageRepDto);
        return "member/memberSearchResult";
    }

    @GetMapping("/admin/administrator")
    public String adminGetAdmins(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "sort", defaultValue = "memberId") String sort, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchAdmin("@", page - 1, sort);
        model.addAttribute("pageData", pageRepDto);
        return "member/admins";
    }

    @GetMapping("/admin/administrator/search")
    public String adminGetAdminsBySearch(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "email", required = false) String email, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchAdmin(email, page - 1, "memberId");
        model.addAttribute("pageData", pageRepDto);
        return "member/admins";
    }

    @PostMapping("/admin/administrator/add")
    public String adminAddAdmin(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        MemberDto.Response saveMember = memberService.createAdmin(memberDto);
        model.addAttribute("email", saveMember.getEmail());
        model.addAttribute("memberImage", saveMember.getMemberImage());
        return "member/join";
    }

    @GetMapping("/admin/administrator/delete")
    public String adminDeleteAdmin(@RequestParam("email") String email, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.deleteAdmin(email);
        model.addAttribute("pageData", pageRepDto);
        return "member/admins";
    }

    @GetMapping("/admin/comment")
    public String adminGetCommentList(@RequestParam(value = "page", defaultValue = "1") int page,
                                      @RequestParam(value = "sort",defaultValue = "claimCount") String sort,
                                      Model model) {
        PagingResponseDto<MemberDto.MemberCommentResponseDto> commentResponseDtoList = memberService.findMyCommentList(page - 1, "@",sort);
        model.addAttribute("pageData", commentResponseDtoList);
        return "member/claimCommentList";
    }
}
