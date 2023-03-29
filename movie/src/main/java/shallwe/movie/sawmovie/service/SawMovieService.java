package shallwe.movie.sawmovie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final SawMovieRepository sawMovieRepository;

    public SawMovie getSawMovie(Movie movie, Member member) {
        return sawMovieRepository.findSawMovieByMemberAndMovie(member, movie);
    }

    public void saveSawMovie(Movie movie, Member member, int movieSawCount) {
        SawMovie findMovie = getSawMovie(movie,member);
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
        sawMovieRepository.save(sawMovie);
    }
    public Page<SawMovie> getSawMovieList(Member member, PageRequest pageable) {
        return sawMovieRepository.findSawMoviesByMemberWithPaging(member,pageable);
    }

    public void setMemberRelation(Member member, SawMovie sawMovie) {
        sawMovie.setMember(member);
        member.getSawMovies().add(sawMovie);
    }

    public void setMovieRelation(Movie movie, SawMovie sawMovie) {
        sawMovie.setMovie(movie);
        movie.getSawMovies().add(sawMovie);
        int sum =0;
        for (SawMovie updateSawMovie : movie.getSawMovies()) {
            sum+= updateSawMovie.getMovieSawCount();
        }
        movie.setAvgSawCount((double) sum/movie.getSawMovies().size());
    }


}
