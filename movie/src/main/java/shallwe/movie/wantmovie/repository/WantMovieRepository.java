package shallwe.movie.wantmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shallwe.movie.querydsl.QuerydslRepository;
import shallwe.movie.wantmovie.entity.WantMovie;

public interface WantMovieRepository extends JpaRepository<WantMovie,Long>, QuerydslRepository {
}
