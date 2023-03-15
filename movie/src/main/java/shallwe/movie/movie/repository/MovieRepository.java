package shallwe.movie.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shallwe.movie.movie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie,Long> {
}
