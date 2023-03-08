package shallwe.movie.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
public class TestController {
    @Value("${server.port}")
    private String port;
    @GetMapping("/")
    public String welcomePage() {
        log.info("now port {}",port);
        return "index";
    }
}
