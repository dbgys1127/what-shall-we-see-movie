package shallwe.movie.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.security.service.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Value("${server.port}")
    private String port;

    @GetMapping("/login-form")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessdenied";
    }

    @GetMapping("/logout-form")
    public String logoutForm() {
        return "logout";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "join-form";
    }

    // 수정할 것
    // 유효성 검증 실패에 대한 내용 view 전달 방법 필요
    @PostMapping("/join")
    public String join(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        log.info("now port {}",port);
        MemberDto.Response saveMember=memberService.createMember(memberDto);
        model.addAttribute("email",saveMember.getEmail());
        model.addAttribute("memberImage",saveMember.getMemberImage());
        return "join";
    }

    @GetMapping("/mypage")
    public String mypage() {
        log.info("now port {}",port);
        return "mypage";
    }

    @GetMapping("/my-info")
    public String myInfoForm() {
        return "my-info";
    }

    @PostMapping("/my-info")
    public String patchMyInfo(@RequestParam("memberImage") MultipartFile multipartFile,
                              @ModelAttribute @Valid MemberDto.Patch memberPatchDto,
                              Authentication authentication,
                              Model model) throws IOException {
        String memberEmail = authentication.getName();
        log.info("memberEmail = {}",memberEmail);
        MemberDto.Response patchMember = memberService.updateMember(multipartFile, memberPatchDto,memberEmail);
        model.addAttribute("memberImage", patchMember.getMemberImage());
        model.addAttribute("email", memberEmail);
        return "mypage";
    }

    @GetMapping("/admin")
    public String adminPage() {
        log.info("now port {}",port);
        return "admin";
    }
}
