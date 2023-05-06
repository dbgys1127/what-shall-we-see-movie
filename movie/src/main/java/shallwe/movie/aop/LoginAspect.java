package shallwe.movie.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.service.MovieService;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@Transactional
public class LoginAspect {
    private final MemberService memberService;
    private final MovieService movieService;

    /**
     * 회원 객체가 필요한 컨트롤러 용 AOP
     * 이메일이 필요한 요청이 오면, SecurityContextHolder에서 Authentication 객체를 구한뒤, 이메일을 구한다.
     * 이후 memberService에서 해당 이메일의 Member 객체를 받아 컨트롤러 인자값으로 전달한다.
     */
    @Around("@annotation(shallwe.movie.aop.NeedMember)")
    public Object getMember(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Object[] parameterList = joinPoint.getArgs();

        if (auth != null) {
            Member loginMember = memberService.is_exist_member(auth.getName());
            parameterList[0] = loginMember;
        }
        Object resultObj = joinPoint.proceed(parameterList);

        return resultObj;
    }

    /**
     * 회원 이메일이 필요한 컨트롤러 용 AOP
     * 이메일이 필요한 요청이 오면, SecurityContextHolder에서 Authentication 객체를 구한뒤, 이메일을 구한다.
     * 이후 이메일을 컨트롤러 인자값으로 전달한다.
     */
    @Around("@annotation(shallwe.movie.aop.NeedEmail)")
    public Object getEmail(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Object[] parameterList = joinPoint.getArgs();

        if (auth != null) {
            parameterList[0] = auth.getName();
        }
        Object resultObj = joinPoint.proceed(parameterList);

        return resultObj;
    }

    /**
     * 회원 객체와 영화 객체가 필요한 컨트롤러 용 AOP
     * 이메일이 필요한 요청이 오면, SecurityContextHolder에서 Authentication 객체를 구한뒤, 이메일을 구한다.
     * 이후 이메일을 컨트롤러 인자값으로 전달한다.
     * Request 값 중 영화 제목값을 구한뒤 인자로 전달한다.
     */
    @Around("@annotation(shallwe.movie.aop.NeedMemberAndMovieTitle)")
    public Object getMemberAndMovieTitle(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String movieTitle=request.getParameter("movieTitle");

        Object[] parameterList = joinPoint.getArgs();

        if (auth != null) {
            Member loginMember = memberService.is_exist_member(auth.getName());
            Movie movie = movieService.is_exist_movie(movieTitle);
            parameterList[0] = loginMember;
            parameterList[1] = movie;
        }
        Object resultObj = joinPoint.proceed(parameterList);

        return resultObj;
    }
}
