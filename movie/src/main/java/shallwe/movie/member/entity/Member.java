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
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String memberImage;

    @Column(nullable = false)
    private int warningCard;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public enum MemberRole{
        ROLE_USER,ROLE_ADMIN
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.memberImage = "이미지";
    }

    // test용 생성자
    @Builder
    public Member(Long id, String email, String password, String memberImage, int warningCard) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.memberImage = memberImage;
        this.warningCard = warningCard;
    }
}
