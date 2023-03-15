package shallwe.movie.member.entity;

import lombok.*;
import shallwe.movie.audit.TimeAudit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends TimeAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String memberImage="이미지";

    @Column(nullable = false)
    private int warningCard=0;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public enum MemberRole{
        ROLE_USER,ROLE_ADMIN
    }


    // test용 생성자
    @Builder
    public Member(Long memberId, String email, String password, String memberImage, int warningCard, List<String> roles) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.memberImage = memberImage;
        this.warningCard = warningCard;
        this.roles = roles;
    }
}
