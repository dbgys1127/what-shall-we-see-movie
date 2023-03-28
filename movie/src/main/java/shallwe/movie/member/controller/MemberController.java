package shallwe.movie.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.member.dto.MemberDto;

import shallwe.movie.member.service.MemberService;


import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


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
        MemberDto.Response saveMember=memberService.createMember(memberDto);
        model.addAttribute("email",saveMember.getEmail());
        model.addAttribute("memberImage",saveMember.getMemberImage());
        return "member/join";
    }

    @GetMapping("/mypage")
    public String mypage(Authentication authentication,Model model) {
        String email = authentication.getName();//aop 필요
        MemberDto.Response memberRepDto = memberService.getMyInfo(email);
        model.addAttribute("member", memberRepDto);
        return "member/mypage";
    }

    @PostMapping("/mypage/myImage")
    public String patchMyImage(@RequestPart("myImage") MultipartFile multipartFile,
                              Authentication authentication,
                              Model model) throws IOException {
        String email = authentication.getName();//aop 필요
        MemberDto.Response patchMember = memberService.updateMemberImage(multipartFile,email);
        model.addAttribute("memberImage", patchMember.getMemberImage());
        model.addAttribute("email", email);
        return "member/mypage";
    }

    @PostMapping("/mypage/myPassword")
    public String patchMyPassword(@ModelAttribute @Valid MemberDto.Patch memberDto,
                              Authentication authentication,
                              Model model) throws IOException {
        String email = authentication.getName();
        MemberDto.Response patchMember = memberService.updateMemberPassword(memberDto,email);
        model.addAttribute("memberImage", patchMember.getMemberImage());
        model.addAttribute("email", email);
        return "member/mypage";
    }

    @GetMapping("/mypage/saw-movie")
    public String getMySawMovieList(@RequestParam(value = "page", defaultValue = "1") int page,
                                   Authentication authentication
                                   , Model model) {
        String email = authentication.getName();
        PagingResponseDto<MemberDto.MemberSawMovieResponseDto> sawMovieResponseDtoList = memberService.findMySawMovieList(page-1,email);
        model.addAttribute("pageData", sawMovieResponseDtoList);
        return "member/sawMovieList";
    }

    //==========================관리자 화면 컨트롤러=========================

    @GetMapping("/admin/member/warning-page")
    public String adminGetWarning(@RequestParam("email") String email, Model model) {
        MemberDto.Response memberRepDto=memberService.pickMember(email);
        model.addAttribute("email", memberRepDto.getEmail());
        model.addAttribute("warningCard", memberRepDto.getWarningCard());
        model.addAttribute("memberStatus", memberRepDto.getMemberStatus());
        return "member/warning";
    }

    @PostMapping("/admin/member/warning")
    public String adminPatchWarning(@RequestParam("email") String email,
                                     @RequestParam(value = "warning",defaultValue = "off") String warning,
                                     @RequestParam(value = "block",defaultValue = "off") String block, Model model) {
        MemberDto.Response memberRepDto=memberService.giveWarning(email,warning, block);
        model.addAttribute("email",memberRepDto.getEmail());
        model.addAttribute("warningCard",memberRepDto.getWarningCard());
        model.addAttribute("memberStatus",memberRepDto.getMemberStatus());
        return "member/warning";
    }

    @GetMapping("/admin/member")
    public String adminGetMembers(@RequestParam("page") int page,
                                  @RequestParam(value = "sort", defaultValue = "memberId") String sort, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchMember("@",page - 1, sort);
        log.info("MEM_startPage={}",pageRepDto.getStartPage());
        model.addAttribute("pageData", pageRepDto);
        return "member/member";
    }

    @GetMapping("/admin/member/search")
    public String getMemberBySearch(@RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "email",required = false) String email, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchMember(email,page-1,"memberId");
        model.addAttribute("pageData", pageRepDto);
        return "member/memberSearchResult";
    }

    @GetMapping("/admin/administrator")
    public String adminGetAdmins(@RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "sort",defaultValue = "memberId") String sort, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchAdmin("@",page-1,sort);
        model.addAttribute("pageData", pageRepDto);
        return "member/admins";
    }

    @GetMapping("/admin/administrator/search")
    public String adminGetAdminsBySearch(@RequestParam(value = "page",defaultValue = "1") int page,
                                 @RequestParam(value = "email",required = false) String email, Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto = memberService.searchAdmin(email,page-1,"memberId");
        model.addAttribute("pageData", pageRepDto);
        return "member/admins";
    }

    @PostMapping("/admin/administrator/add")
    public String adminAddAdmin(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        MemberDto.Response saveMember=memberService.createAdmin(memberDto);
        model.addAttribute("email",saveMember.getEmail());
        model.addAttribute("memberImage",saveMember.getMemberImage());
        return "member/join";
    }

    @GetMapping("/admin/administrator/delete")
    public String adminDeleteAdmin(@RequestParam("email") String email,Model model) {
        PagingResponseDto<MemberDto.Response> pageRepDto=memberService.deleteAdmin(email);
        model.addAttribute("pageData", pageRepDto);
        return "member/admins";
    }
}
