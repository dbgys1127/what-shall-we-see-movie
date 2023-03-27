package shallwe.movie.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shallwe.movie.member.entity.Member;
import shallwe.movie.sawmovie.entity.SawMovie;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberDto {

    @Getter
    @Setter
    public static class Post {
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",message = "이메일 주소를 다시 기입하세요.")
        private String email;

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$",message = "최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요.")
        private String password;

        @Builder
        public Post(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
    @Getter
    @Setter
    public static class Patch {

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$",message = "최소 8자, 최소 하나의 문자, 숫자, 특수 문자를 기입하세요.")
        private String password;
    }

    @Getter
    @Setter
    public static class Response {
        private String email;
        private String password;
        private String memberImage;
        private int warningCard;

        private Member.MemberStatus memberStatus;

        private LocalDateTime createdAt;

        private List<String> roles;

        private List<MemberSawMovieResponseDto> sawMovies;

        @Builder
        public Response(String email, String password, String memberImage,
                        int warningCard, Member.MemberStatus memberStatus,
                        LocalDateTime createdAt,List<String> roles) {
            this.email = email;
            this.password = password;
            this.memberImage = memberImage;
            this.warningCard = warningCard;
            this.memberStatus = memberStatus;
            this.createdAt = createdAt;
            this.roles = roles;
        }

    }
    @Builder
    @Getter
    @Setter
    public static class MemberSawMovieResponseDto {
        private String moviePoster;
        private String movieTitle;
        private double avgSawCount;
    }

    public static List<MemberSawMovieResponseDto> getMemberSawMovieResponseDtoList(List<SawMovie> sawMovies) {
        return sawMovies
                .stream()
                .sorted(Comparator.comparing(SawMovie::getMovieSawCount).reversed())
                .map(sawMovie -> MemberSawMovieResponseDto
                        .builder()
                        .moviePoster(sawMovie.getMovie().getMoviePoster())
                        .movieTitle(sawMovie.getMovie().getMovieTitle())
                        .avgSawCount(sawMovie.getMovie().getAvgSawCount())
                        .build())
                .collect(Collectors.toList());
    }
}
