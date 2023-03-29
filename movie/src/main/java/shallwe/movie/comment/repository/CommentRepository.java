package shallwe.movie.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.querydsl.QuerydslRepository;

public interface CommentRepository extends JpaRepository<Comment,Long>, QuerydslRepository {
}
