package shallwe.movie.wantmovie.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import shallwe.movie.TestConfig;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.repository.SawMovieRepository;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.service.WantMovieService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
@ActiveProfiles("test")
@Slf4j
public class WantMovieRepositoryTest {
    @Autowired
    private WantMovieRepository wantMovieRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MovieRepository movieRepository;

    private List<Member> memberList = new ArrayList<>();
    private List<Movie> movieList = new ArrayList<>();


    @BeforeEach
    void insertData() {
        for (int i = 1; i < 21; i++) {
            Member member=memberRepository.save(Member.builder()
                    .email("test" + i + "@gmail.com")
                    .password("1234!abc")
                    .warningCard(0)
                    .build());
            memberList.add(member);
            Movie movie = movieRepository.save(Movie.builder()
                    .movieTitle("movie" + i)
                    .moviePoster("img")
                    .movieDescription("111")
                    .movieRunningTime(90)
                    .movieOpenDate(LocalDate.of(2023,1,1))
                    .movieGenre(Movie.MovieGenre.드라마)
                    .build());
            movieList.add(movie);
            WantMovie wantMovie = WantMovie.builder()
                    .member(member)
                    .movie(movie)
                    .build();
            wantMovieRepository.save(wantMovie);
        }
    }
    @AfterEach
    void deleteData() {
        wantMovieRepository.deleteAll();
    }

    @DisplayName("영화와 회원이 일치하는 wantMovie 객체를 가져올수 있다.")
    @Test
    void findWantMovieByMemberAndMovie() {
        //given
        Member member = memberRepository.findByEmail("test1@gmail.com").orElseThrow();
        Movie movie = movieRepository.findByMovieTitle("movie1").orElseThrow();


        //when
        WantMovie wantMovie=wantMovieRepository.findWantMovieByMemberAndMovie(member, movie);

        //then
        Assertions.assertThat(wantMovie.getMovie()).isEqualTo(movie);
        Assertions.assertThat(wantMovie.getMember()).isEqualTo(member);
    }

    @DisplayName("회원이 일치하는 wantMovie 객체를 가져올수 있다.")
    @Test
    void findSawMoviesByMemberWithPaging() {
        //given
        Member member = memberRepository.findByEmail("test1@gmail.com").orElseThrow();

        //when
        Page<WantMovie> wantMovies=wantMovieRepository.findWantMoviesByMemberWithPaging(member, PageRequest.of(0, 10, Sort.by("createdAtForWantMovie")));

        //then
        Assertions.assertThat(wantMovies.getContent().size()).isEqualTo(1);
    }
}
