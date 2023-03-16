package shallwe.movie.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class FormController {
    @Value("${server.port}")
    private String port;

    @GetMapping("/login-form")
    public String loginForm() {
        log.info("now port {}",port);
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        log.info("now port {}",port);
        return "accessdenied";
    }

    @GetMapping("/logout-form")
    public String logoutForm() {
        log.info("now port {}",port);
        return "logout";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        log.info("now port {}",port);
        return "join-form";
    }

    @GetMapping("/mypage")
    public String mypage() {
        log.info("now port {}",port);
        return "mypage";
    }
    @GetMapping("/my-info/myImage")
    public String myImageForm() {
        log.info("now port {}",port);
        return "myImage";
    }

    @GetMapping("/my-info/myPassword")
    public String myPasswordForm() {
        log.info("now port {}",port);
        return "myPassword";
    }

    @GetMapping("/admin")
    public String adminPage() {
        log.info("now port {}",port);
        return "admin";
    }

}
