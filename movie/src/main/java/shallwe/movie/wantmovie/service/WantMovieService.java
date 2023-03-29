package shallwe.movie.wantmovie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.repository.WantMovieRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WantMovieService {

    private final WantMovieRepository wantMovieRepository;

    public WantMovie getWantMovie(Member member, Movie movie) {
        return wantMovieRepository.findWantMovieByMemberAndMovie(member, movie);
    }

    public Page<WantMovie> getWantMovieList(Member member, PageRequest pageable) {
        return wantMovieRepository.findWantMoviesByMemberWithPaging(member, pageable);
    }

    public void saveWantMovie(Member findMember,Movie findMovie) {
        WantMovie findWantMovie = getWantMovie(findMember, findMovie);
        if (Optional.ofNullable(findWantMovie).isPresent()) {
            return;
        }
        WantMovie wantMovie = new WantMovie();
        setMemberRelation(findMember, wantMovie);
        setMovieRelation(findMovie,wantMovie);

        wantMovieRepository.save(wantMovie);
    }

    public void deleteWantMovie(Member findMember, Movie findMovie) {
        WantMovie findWantMovie = getWantMovie(findMember, findMovie);
        if (Optional.ofNullable(findWantMovie).isEmpty()) {
            return;
        }
        wantMovieRepository.delete(findWantMovie);
    }

    public void setMemberRelation(Member member, WantMovie wantMovie) {
        wantMovie.setMember(member);
        member.getWantMovies().add(wantMovie);
    }

    public void setMovieRelation(Movie movie, WantMovie wantMovie) {
        wantMovie.setMovie(movie);
        movie.getWantMovies().add(wantMovie);
    }


}
