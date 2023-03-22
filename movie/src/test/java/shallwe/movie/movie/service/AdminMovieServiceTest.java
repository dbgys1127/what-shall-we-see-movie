package shallwe.movie.movie.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class AdminMovieServiceTest {
    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    private List<Movie> movies = new ArrayList<>();

    private Page<Movie> pageInfo;

    @BeforeEach
    void insertData() {
        for (int i = 1; i <= 31; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .build();
            movies.add(movie);
        }
    }

    @DisplayName("영화 목록 조회시 영화를 등록한 순으로 정렬된 데이터를 불러온다.")
    @Test
    void findAllMovie() {
        //given
        int page = 1;
        String sort = "movieId";
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sort).descending());
        pageInfo = new PageImpl<>(movies, pageable, movies.size());

        //stub
        given(movieRepository.findAll(PageRequest.of(page - 1, 10, Sort.by(sort).descending()))).willReturn(pageInfo);

        //when
        PagingResponseDto<MovieDto.Response> pagingResponseDto = movieService.findAllMovie(page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo(sort);
        Assertions.assertThat(pagingResponseDto.getEndPage()).isEqualTo(4);
    }
}
