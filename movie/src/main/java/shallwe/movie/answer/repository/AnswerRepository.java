package shallwe.movie.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shallwe.movie.answer.entity.Answer;
import shallwe.movie.querydsl.QuerydslRepository;

public interface AnswerRepository extends JpaRepository<Answer,Long>, QuerydslRepository {
}
