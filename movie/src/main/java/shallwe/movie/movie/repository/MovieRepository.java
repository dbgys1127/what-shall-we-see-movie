package shallwe.movie.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.querydsl.QuerydslRepository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie,Long>, QuerydslRepository {

    Optional<Movie> findByMovieTitle(String movieTitle);

    void deleteByMovieTitle(String movieTitle);
}
