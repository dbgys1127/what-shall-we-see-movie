package shallwe.movie.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

public class MemberDto {

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Post {
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",message = "이메일 주소를 다시 기입하세요.")
        private String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$",message = "최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요.")
        private String password;
    }

}
