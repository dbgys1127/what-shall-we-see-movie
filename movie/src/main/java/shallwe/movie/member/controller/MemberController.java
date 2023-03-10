package shallwe.movie.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;

import javax.validation.Valid;

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

    @PostMapping("/join")
    public String join(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        log.info("now port {}",port);
        Member member = new Member(memberDto.getEmail(), memberDto.getPassword());
        Member saveMember=memberService.createMember(member);
        model.addAttribute("member",saveMember);
        return "join";
    }
    @GetMapping("/mypage")
    public void mypage() {
        log.info("now port {}",port);
    }
    @GetMapping("/movie")
    public void movie() {
        log.info("now port {}",port);
    }
    @GetMapping("/inquiry")
    public void inquiry() {
        log.info("now port {}",port);
    }

}
