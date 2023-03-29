package shallwe.movie.comment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shallwe.movie.audit.WriterAudit;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends WriterAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String commentDetail;

    private int claimCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    public Comment(Long commentId, String commentDetail, int claimCount, Member member, Movie movie) {
        this.commentId = commentId;
        this.commentDetail = commentDetail;
        this.claimCount = claimCount;
        this.member = member;
        this.movie = movie;
    }
}
