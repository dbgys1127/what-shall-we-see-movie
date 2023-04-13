package shallwe.movie.comment.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.wantmovie.entity.WantMovie;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentDto {
    @Getter
    @Setter
    @Builder
    public static class Post {
        @NotBlank(message = "댓글을 기입해주세요.")
        private String commentDetail;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response implements Serializable {
        private Long commentId;
        private String commentDetail;
        private String createdBy;
        private String createdAt;
        private int claimCount;
    }

    public static List<CommentDto.Response> getCommentResponseDtoList(List<Comment> comments) {
        return comments
                .stream()
                .sorted(Comparator.comparing(Comment::getCommentId).reversed())
                .map(comment -> Response
                        .builder()
                        .commentId(comment.getCommentId())
                        .commentDetail(comment.getCommentDetail())
                        .createdBy(comment.getCreatedBy())
                        .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .claimCount(comment.getClaimCount())
                        .build())
                .collect(Collectors.toList());
    }
}
