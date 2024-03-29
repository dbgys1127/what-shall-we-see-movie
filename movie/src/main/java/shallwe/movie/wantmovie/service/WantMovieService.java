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

    /**
     * 멤버와 영화가 일치하는 WantMovie 객체 조회용
     */
    public WantMovie getWantMovie(Member member, Movie movie) {
        return wantMovieRepository.findWantMovieByMemberAndMovie(member, movie);
    }

    /**
     * 내가 찜을 등록한 영화 목록을 조회하기 위한 메서드
     */
    public Page<WantMovie> getWantMovieList(Member member, PageRequest pageable) {
        return wantMovieRepository.findWantMoviesByMemberWithPaging(member, pageable);
    }

    /**
     * 찜 등록용
     * 기존에 찜 등록하지 않았다면, WantMovie 객체를 새로 생성, 등록했다면, WantMovie 객체를 가져옴
     */
    public void saveWantMovie(Member findMember,Movie findMovie) {
        WantMovie findWantMovie = getWantMovie(findMember, findMovie);
        if (Optional.ofNullable(findWantMovie).isPresent()) {
            return;
        }
        WantMovie wantMovie = new WantMovie();
        setMemberRelation(findMember, wantMovie);
        setMovieRelation(findMovie,wantMovie);

        wantMovieRepository.save(wantMovie);
        log.info("영화 찜 등록완료 -> 회원 이메일 : {}, 영화 제목 : {}",findMember.getEmail(),findMovie.getMovieTitle());
    }

    public void deleteWantMovie(Member findMember, Movie findMovie) {
        WantMovie findWantMovie = getWantMovie(findMember, findMovie);
        if (Optional.ofNullable(findWantMovie).isEmpty()) {
            return;
        }
        wantMovieRepository.delete(findWantMovie);
        log.info("영화 찜 등록취소 -> 회원 이메일 : {}, 영화 제목 : {}",findMember.getEmail(),findMovie.getMovieTitle());
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
