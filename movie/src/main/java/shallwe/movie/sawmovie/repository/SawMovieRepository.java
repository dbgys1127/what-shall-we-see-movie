package shallwe.movie.sawmovie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shallwe.movie.querydsl.QuerydslRepository;
import shallwe.movie.sawmovie.entity.SawMovie;

public interface SawMovieRepository extends JpaRepository<SawMovie,Long> , QuerydslRepository {
}
