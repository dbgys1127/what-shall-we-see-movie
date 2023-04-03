package shallwe.movie.inquiry.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import shallwe.movie.answer.entity.Answer;
import shallwe.movie.audit.WriterAudit;
import shallwe.movie.member.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inquiry extends WriterAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    @Column(nullable = false,columnDefinition = "MEDIUMTEXT")
    private String inquiryTitle;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String inquiryDescription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
    List<Answer> answers = new ArrayList<>();

    public enum InquiryStatus {
        처리, 대기;
    }

    @Builder
    public Inquiry(Long inquiryId, String inquiryTitle, String inquiryDescription,Member member,List<Answer> answers) {
        this.inquiryId = inquiryId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryDescription = inquiryDescription;
        this.inquiryStatus = InquiryStatus.대기;
        this.member = member;
        this.answers = answers;
    }
}
