package shallwe.movie.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
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

    // 수정할 것
    // 유효성 검증 실패에 대한 내용 view 전달 방법 필요
    @PostMapping("/join")
    public String join(@ModelAttribute @Valid MemberDto.Post memberDto, Model model) {
        MemberDto.Response saveMember=memberService.createMember(memberDto);
        model.addAttribute("email",saveMember.getEmail());
        model.addAttribute("memberImage",saveMember.getMemberImage());
        return "join";
    }

    @PostMapping("/my-info/myImage")
    public String patchMyImage(@RequestPart("myImage") MultipartFile multipartFile,
                              Authentication authentication,
                              Model model) throws IOException {
        String email = authentication.getName();//aop 필요
        MemberDto.Response patchMember = memberService.updateMemberImage(multipartFile,email);
        model.addAttribute("memberImage", patchMember.getMemberImage());
        model.addAttribute("email", email);
        return "mypage";
    }

    @PostMapping("/my-info/myPassword")
    public String patchMyPassword(@ModelAttribute @Valid MemberDto.Patch memberDto,
                              Authentication authentication,
                              Model model) throws IOException {
        String email = authentication.getName();
        MemberDto.Response patchMember = memberService.updateMemberPassword(memberDto,email);
        model.addAttribute("memberImage", patchMember.getMemberImage());
        model.addAttribute("email", email);
        return "mypage";
    }

    @GetMapping("/admin/member")
    public String adminMemberPage() {

        return "member";
    }
}
