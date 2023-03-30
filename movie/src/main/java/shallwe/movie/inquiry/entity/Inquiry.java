package shallwe.movie.inquiry.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shallwe.movie.audit.WriterAudit;
import shallwe.movie.member.entity.Member;

import javax.persistence.*;

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
    private InquiryStatus inquiryStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public enum InquiryStatus {
        처리, 대기;
    }

    @Builder
    public Inquiry(Long inquiryId, String inquiryTitle, String inquiryDescription) {
        this.inquiryId = inquiryId;
        this.inquiryTitle = inquiryTitle;
        this.inquiryDescription = inquiryDescription;
        this.inquiryStatus = InquiryStatus.대기;
    }
}
