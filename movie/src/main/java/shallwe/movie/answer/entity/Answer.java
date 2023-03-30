package shallwe.movie.answer.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shallwe.movie.audit.WriterAudit;
import shallwe.movie.inquiry.entity.Inquiry;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Answer extends WriterAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @Column(nullable = false,columnDefinition = "LONGTEXT")
    private String answerDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    @Builder
    public Answer(Long answerId, String answerDescription, Inquiry inquiry) {
        this.answerId = answerId;
        this.answerDescription = answerDescription;
        this.inquiry = inquiry;
    }
}
