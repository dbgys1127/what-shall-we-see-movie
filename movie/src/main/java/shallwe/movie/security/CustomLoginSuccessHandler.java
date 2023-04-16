package shallwe.movie.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.setAttribute("nowMember",authentication.getName()+"님 무볼까요?");
        session.setAttribute("nowMemberAuth",authentication.getAuthorities().stream().collect(Collectors.toList()).get(0).toString());
        log.info("nowMemberAuth = {}",authentication.getAuthorities().stream().collect(Collectors.toList()).get(0).toString());
        response.sendRedirect("/");
    }

}
