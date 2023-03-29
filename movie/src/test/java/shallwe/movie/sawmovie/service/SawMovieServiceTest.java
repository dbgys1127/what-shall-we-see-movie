package shallwe.movie.sawmovie.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.repository.SawMovieRepository;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.repository.WantMovieRepository;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class SawMovieServiceTest {
    @InjectMocks
    SawMovieService sawMovieService;

    @Mock
    SawMovieRepository sawMovieRepository;

    @DisplayName("1.멤버가 시청횟수를 등록하면, 대상 영화, 등록한 회원, 횟수를 담은 객체가 반환된다.")
    @Test
    void saveSawMovieAlreadyExist() {
        //given
        int movieSawCount = 1;
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        member.setSawMovies(new ArrayList<>());
        Movie movie = Movie.builder()
                .movieTitle("movie1")
                .build();
        movie.setSawMovies(new ArrayList<>());

        SawMovie sawMovie = SawMovie.builder()
                .movieSawCount(movieSawCount)
                .movie(movie)
                .member(member)
                .build();

        //stub
        given(sawMovieRepository.findSawMovieByMemberAndMovie(member, movie)).willReturn(sawMovie);
        given(sawMovieRepository.save(any())).willReturn(sawMovie);

        //when
        sawMovieService.saveSawMovie(movie, member, movieSawCount);
        SawMovie saveSawMovie = sawMovieService.getSawMovie(movie, member);

        //then
        Assertions.assertThat(saveSawMovie.getMovieSawCount()).isEqualTo(1);
        Assertions.assertThat(saveSawMovie.getMovie().getMovieTitle()).isEqualTo(movie.getMovieTitle());
        Assertions.assertThat(saveSawMovie.getMember().getEmail()).isEqualTo(member.getEmail());
    }
}
