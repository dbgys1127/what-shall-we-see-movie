package shallwe.movie.sawmovie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.movie.service.MovieService;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.repository.SawMovieRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SawMovieService {
    private final MemberService memberService;
    private final SawMovieRepository sawMovieRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;

    public SawMovie getSawMovie(Movie movie, Member member) {
        return sawMovieRepository.findSawMovieByMemberAndMovie(member, movie);
    }

    public SawMovie saveSawMovie(Movie movie, Member member, int movieSawCount) {
        SawMovie findMovie = sawMovieRepository.findSawMovieByMemberAndMovie(member, movie);
        SawMovie sawMovie;

        if (Optional.ofNullable(findMovie).isEmpty()) {
            sawMovie = new SawMovie();
            sawMovie.setMovieSawCount(movieSawCount);
        } else {
            sawMovie = findMovie;
            sawMovie.setMovieSawCount(movieSawCount);
        }
        setMemberRelation(member,sawMovie);
        setMovieRelation(movie,sawMovie);

        memberRepository.save(member);
        movieRepository.save(movie);
        return sawMovieRepository.save(sawMovie);
    }

    public void setMemberRelation(Member member, SawMovie sawMovie) {
        sawMovie.setMember(member);
        member.getSawMovies().add(sawMovie);
    }

    public void setMovieRelation(Movie movie, SawMovie sawMovie) {
        sawMovie.setMovie(movie);
        movie.getSawMovies().add(sawMovie);
    }
}
