package shallwe.movie.wantmovie.service;

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
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.repository.WantMovieRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class WantMovieServiceTest {
    @InjectMocks
    WantMovieService wantMovieService;

    @Mock
    WantMovieRepository wantMovieRepository;

    @DisplayName("1.멤버가 영화를 찜하면, 멤버와 영화가 wantMovie와 연관관계를 갖게 된다.")
    @Test
    void saveWantMovie() {
        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();

        Movie movie = Movie.builder()
                .movieTitle("movie1")
                .build();


        WantMovie wantMovie = WantMovie.builder()
                .build();

        //stub
        given(wantMovieRepository.findWantMovieByMemberAndMovie(member, movie)).willReturn(null);
        given(wantMovieRepository.save(any())).willReturn(wantMovie);

        //when
        wantMovieService.saveWantMovie(member,movie);

        //then
        Assertions.assertThat(member.getWantMovies()).isNotNull();
        Assertions.assertThat(movie.getWantMovies()).isNotNull();
    }
    @DisplayName("2.앞서 멤버가 영화를 찜하면, wantMovie를 새롭게 저장하지 않는다.")
    @Test
    void saveAlreadyWantMovieAlreadyExist() {
        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        Movie movie = Movie.builder()
                .movieTitle("movie1")
                .build();

        WantMovie wantMovie = WantMovie.builder()
                .build();

        //stub
        given(wantMovieRepository.findWantMovieByMemberAndMovie(member, movie)).willReturn(wantMovie);

        //when
        wantMovieService.saveWantMovie(member,movie);

        //then
        Assertions.assertThat(member.getWantMovies()).isEmpty();
        Assertions.assertThat(movie.getWantMovies()).isEmpty();
    }
    @DisplayName("3.멤버가 영화를 찜을 취소하면, 멤버와 영화가 wantMovie와 연관관계를 끊는다.")
    @Test
    void deleteWantMovie() {
        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        member.setWantMovies(new ArrayList<>());
        Movie movie = Movie.builder()
                .movieTitle("movie1")
                .build();
        movie.setWantMovies(new ArrayList<>());

        WantMovie wantMovie = WantMovie.builder()
                .member(member)
                .movie(movie)
                .build();

        //stub
        given(wantMovieRepository.findWantMovieByMemberAndMovie(member, movie)).willReturn(wantMovie);

        //when
        wantMovieService.deleteWantMovie(member,movie);

        //then
        Assertions.assertThat(member.getWantMovies()).isEmpty();
        Assertions.assertThat(movie.getWantMovies()).isEmpty();
    }
    @DisplayName("4.멤버가 영화를 찜을 취소했을때, 기존에 찜을 등록한 적이 없다면, 지우는 동작이 일어나지 않는다.")
    @Test
    void deleteWantMovieAlreadyExist() {
        //given
        Member member = Member.builder()
                .email("test@gmail.com")
                .build();
        member.setWantMovies(new ArrayList<>());
        Movie movie = Movie.builder()
                .movieTitle("movie1")
                .build();
        movie.setWantMovies(new ArrayList<>());

        WantMovie wantMovie = WantMovie.builder()
                .member(member)
                .movie(movie)
                .build();

        //stub
        given(wantMovieRepository.findWantMovieByMemberAndMovie(member, movie)).willReturn(null);

        //when
        wantMovieService.deleteWantMovie(member,movie);

        //then
        Assertions.assertThat(member.getWantMovies()).isNotNull();
        Assertions.assertThat(movie.getWantMovies()).isNotNull();
    }
}
