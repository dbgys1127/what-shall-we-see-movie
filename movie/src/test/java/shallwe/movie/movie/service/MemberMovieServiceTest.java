package shallwe.movie.movie.service;

import com.amazonaws.services.s3.AmazonS3;
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
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.service.SawMovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class MemberMovieServiceTest {

    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MemberRepository memberRepository;


    @Mock
    private S3UploadService s3UploadService;

    @Mock
    private MemberService memberService;

    @Mock
    private SawMovieService sawMovieService;

    @Mock
    private AmazonS3 amazonS3;

    private List<Movie> movies = new ArrayList<>();

    private Page<Movie> pageInfo;

    @BeforeEach
    void insertData() {
        for (int i = 1; i < 11; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .movieGenre(Movie.MovieGenre.드라마)
                    .build();
            movies.add(movie);
        }
        for (int i = 11; i < 21; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .movieGenre(Movie.MovieGenre.코미디)
                    .build();
            movies.add(movie);
        }
        for (int i = 21; i < 31; i++) {
            Movie movie = Movie.builder()
                    .movieTitle("movie" + i)
                    .movieGenre(Movie.MovieGenre.액션)
                    .build();
            movies.add(movie);
        }
    }

    @DisplayName("장르별로 영화 목록을 조회할 수 있다.")
    @Test
    void searchMovieByGenre() {
        //given
        int page = 0;
        String sort = "movieId";
        String genre = "코미디";
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        pageInfo = new PageImpl<>(movies, pageable, movies.size());

        //stub
        given(movieRepository.findMovieByGenreWithPaging(genre, pageable)).willReturn(pageInfo);

        //when
        PagingResponseDto<MovieDto.Response> pagingResponseDto = movieService.searchMovieByGenre(genre, page, sort);

        //then
        Assertions.assertThat(pagingResponseDto.getNowPage()).isEqualTo(1);
        Assertions.assertThat(pagingResponseDto.getSort()).isEqualTo(sort);
        Assertions.assertThat(pagingResponseDto.getEndPage()).isEqualTo(3);
    }

    @DisplayName("멤버는 자신이 시청한 영화의 시청횟수를 등록할 수 있다.")
    @Test
    void updateSawCount() {
        //given
        String movieTitle = "movie";
        String email = "test@gmail.com";
        int movieSawCount = 2;

        Member member = Member.builder().email("test@gmail.com").build();
        movies.get(0).setAvgSawCount(movieSawCount);

        //stub
        given(memberService.is_exist_member(any())).willReturn(member);
        given(movieRepository.findByMovieTitle(any())).willReturn(Optional.of(movies.get(0)));
        given(sawMovieService.saveSawMovie(movies.get(0), member,2)).willReturn(SawMovie.builder().movieSawCount(movieSawCount)
                .build());

        //when
        MovieDto.Response movieRepDto = movieService.updateSawCount(movieTitle, email, movieSawCount);

        //then
        Assertions.assertThat(movieRepDto.getMemberSawCount()).isEqualTo(2);
        Assertions.assertThat(movieRepDto.getAvgSawCount()).isEqualTo(2);
    }
}
