package shallwe.movie.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    @Value("${server.port}")
    private String port;
    private final MemberService memberService;

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
        Member member = new Member(memberDto.getEmail(), memberDto.getPassword());
        Member saveMember=memberService.createMember(member);
        model.addAttribute("email",saveMember.getEmail());
        model.addAttribute("memberImage",saveMember.getMemberImage());
        return "join";
    }
    @GetMapping("/mypage")
    public String mypage() {
        log.info("now port {}",port);
        return "mypage";
    }
    @GetMapping("/admin")
    public String adminPage() {
        log.info("now port {}",port);
        return "admin";
    }
}
