package shallwe.movie.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shallwe.movie.member.dto.MemberDto;

import javax.validation.Valid;

@Slf4j
@Controller
public class MemberFormController {
    @Value("${server.port}")
    private String port;

    @GetMapping("/login-form")
    public String loginForm() {
        log.info("now port {}",port);
        return "member/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        log.info("now port {}",port);
        return "member/accessdenied";
    }

    @GetMapping("/logout-form")
    public String logoutForm() {
        log.info("now port {}",port);
        return "member/logout";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        log.info("now port {}",port);
        return "member/join-form";
    }


    @GetMapping("/mypage/myImage")
    public String myImageForm() {
        log.info("now port {}",port);
        return "member/myImage";
    }

    @GetMapping("/mypage/myPassword")
    public String myPasswordForm() {
        log.info("now port {}",port);
        return "member/myPassword";
    }

    @GetMapping("/admin")
    public String adminPage() {
        log.info("now port {}",port);
        return "redirect:admin/member";
    }
    @GetMapping("/admin/administrator/add-admin-form")
    public String adminAddAdmin() {
        return "member/admin/addAdmin";
    }
}
