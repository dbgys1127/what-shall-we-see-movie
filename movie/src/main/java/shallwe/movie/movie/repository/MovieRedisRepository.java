package shallwe.movie.movie.repository;

import org.springframework.data.repository.CrudRepository;
import shallwe.movie.movie.dto.MovieDto;

public interface MovieRedisRepository extends CrudRepository<MovieDto.Response,String> {
}
