package shallwe.movie.answer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class AnswerDto {
    @Getter
    @Setter
    @Builder
    public static class Post {
        @NotBlank(message = "내용을 기입하셔야 합니다.")
        private String answerDescription;
    }

    @Getter
    @Setter
    @Builder
    public static class Response implements Serializable {
        private Long answerId;
        private String answerDescription;
        private String createdBy;
        private String createdAt;
    }
}
