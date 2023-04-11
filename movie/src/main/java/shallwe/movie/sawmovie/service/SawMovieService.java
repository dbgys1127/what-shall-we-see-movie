package shallwe.movie.sawmovie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    public SawMovie getSawMovie(Member member, Movie movie) {
        return sawMovieRepository.findSawMovieByMemberAndMovie(member, movie);
    }

    public Page<SawMovie> getSawMovieList(Member member, PageRequest pageable) {
        return sawMovieRepository.findSawMoviesByMemberWithPaging(member,pageable);
    }


    public void saveSawMovie(Movie movie, Member member, int movieSawCount) {
        SawMovie findMovie = getSawMovie(member,movie);
        SawMovie sawMovie;

        if (Optional.ofNullable(findMovie).isEmpty()) {
            sawMovie = new SawMovie();
            sawMovie.setMovieSawCount(movieSawCount);

        } else {
            sawMovie = findMovie;
            sawMovie.setMovieSawCount(movieSawCount);
        }

        setMovieRelation(movie,sawMovie);
        setMemberRelation(member,sawMovie);
        sawMovieRepository.save(sawMovie);
        log.info("시청횟수 완료 -> 회원 이메일 : {}, 영화 제목 : {}, 평균시청횟수 : {}",member.getEmail(),movie.getMovieTitle(),movie.getAvgSawCount());
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
