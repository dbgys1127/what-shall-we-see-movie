package shallwe.movie.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login-form")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "join-form";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessdenied";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberDto.Post memberDto, Model model) {
        Member member = new Member(memberDto.getEmail(), memberDto.getPassword());
        Member saveMember=memberService.createMember(member);
        model.addAttribute("member",saveMember);
        return "join";
    }
}
