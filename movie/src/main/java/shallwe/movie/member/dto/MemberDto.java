package shallwe.movie.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.member.entity.Member;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response implements Serializable {
        private String email;
        private String password;
        private String memberImage;
        private int warningCard;

        private Member.MemberStatus memberStatus;

        private String createdAt;

        private List<String> roles;

        private List<MemberSawMovieResponseDto> sawMovies;

        private List<MemberWantMovieResponseDto> wantMovies;

        private List<MemberCommentResponseDto> comments;

        private int sawMoviesTotalCount;

        private int wantMoviesTotalCount;

        private int commentCount;

    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberSawMovieResponseDto implements Serializable {
        private String moviePoster;
        private String movieTitle;
        private int sawCount;
    }

    public static List<MemberSawMovieResponseDto> getMemberSawMovieResponseDtoList(List<SawMovie> sawMovies, int size) {
        return sawMovies
                .stream()
                .sorted(Comparator.comparing(SawMovie::getMovieSawCount).reversed())
                .limit(size)
                .map(sawMovie -> MemberSawMovieResponseDto
                        .builder()
                        .moviePoster(sawMovie.getMovie().getMoviePoster())
                        .movieTitle(sawMovie.getMovie().getMovieTitle())
                        .sawCount(sawMovie.getMovieSawCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberWantMovieResponseDto implements Serializable {
        private String moviePoster;
        private String movieTitle;
        private double avgSawCount;
    }

    public static List<MemberWantMovieResponseDto> getMemberWantMovieResponseDtoList(List<WantMovie> wantMovies, int size) {
        return wantMovies
                .stream()
                .sorted(Comparator.comparing(WantMovie::getWantMovieId).reversed())
                .limit(size)
                .map(wantMovie -> MemberWantMovieResponseDto
                        .builder()
                        .moviePoster(wantMovie.getMovie().getMoviePoster())
                        .movieTitle(wantMovie.getMovie().getMovieTitle())
                        .avgSawCount(wantMovie.getMovie().getAvgSawCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberCommentResponseDto implements Serializable {
        private Long commentId;
        private String commentDetail;
        private String movieTitle;
        private String createdAt;
        private String createdBy;
        private int claimCount;
    }

    public static List<MemberCommentResponseDto> getMemberCommentResponseDtoList(List<Comment> comments, int size) {
        return comments
                .stream()
                .limit(size)
                .map(comment -> MemberCommentResponseDto
                        .builder()
                        .commentId(comment.getCommentId())
                        .commentDetail(comment.getCommentDetail())
                        .movieTitle(comment.getMovie().getMovieTitle())
                        .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .createdBy(comment.getCreatedBy())
                        .claimCount(comment.getClaimCount())
                        .build())
                .collect(Collectors.toList());
    }
}
