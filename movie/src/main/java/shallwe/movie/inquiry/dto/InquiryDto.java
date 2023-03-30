package shallwe.movie.inquiry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shallwe.movie.answer.dto.AnswerDto;
import shallwe.movie.answer.entity.Answer;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.inquiry.entity.Inquiry;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InquiryDto {
    @Getter
    @Setter
    public static class Post {
        @NotBlank(message = "제목은 공백일 수 없습니다.")
        private String inquiryTitle;

        @NotBlank(message = "내용을 기입하셔야 합니다.")
        private String inquiryDescription;

        @Builder
        public Post(String inquiryTitle, String inquiryDescription) {
            this.inquiryTitle = inquiryTitle;
            this.inquiryDescription = inquiryDescription;
        }
    }
    @Builder
    @Getter
    @Setter
    public static class Response {
        private Long inquiryId;
        private String inquiryTitle;
        private String inquiryDescription;
        private Inquiry.InquiryStatus inquiryStatus;
        private String createdBy;
        private LocalDateTime createdAt;
        private List<AnswerDto.Response> answers;
    }
    public static List<AnswerDto.Response> getAnswerResponseDtoList(List<Answer> answers) {
        return answers
                .stream()
                .sorted(Comparator.comparing(Answer::getAnswerId).reversed())
                .map(answer -> AnswerDto.Response
                        .builder()
                        .answerDescription(answer.getAnswerDescription())
                        .createdBy(answer.getCreatedBy())
                        .createdAt(answer.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
