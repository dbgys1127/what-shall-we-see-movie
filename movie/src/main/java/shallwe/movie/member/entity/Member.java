package shallwe.movie.member.entity;

import lombok.*;
import shallwe.movie.audit.TimeAudit;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;

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

    @Column(columnDefinition = "MEDIUMTEXT")
    private String memberImage;

    @Column(nullable = false)
    private int warningCard=0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SawMovie> sawMovies = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<WantMovie> wantMovies = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public enum MemberRole{
        ROLE_USER,ROLE_ADMIN;
    }

    public enum MemberStatus {
        활성, 차단;
    }

    // test용 생성자
    @Builder
    public Member(Long memberId, String email, String password, int warningCard, List<String> roles) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.memberImage = "https://movie-project-bucket.s3.ap-northeast-2.amazonaws.com/4648c38d-87ef-4d66-93ee-15ac8cc373fb-%E1%84%92%E1%85%AC%E1%84%8B%E1%85%AF%E1%86%AB%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%89%E1%85%A1%E1%84%8C%E1%85%B5%E1%86%AB.png";
        this.warningCard = warningCard;
        this.memberStatus = MemberStatus.활성;
        this.roles = roles;
    }
}
