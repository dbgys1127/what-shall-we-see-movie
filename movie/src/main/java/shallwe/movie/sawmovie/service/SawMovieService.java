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

    /**
     * 멤버와 영화가 일치하는 SawMovie 객체 조회용
     */
    public SawMovie getSawMovie(Member member, Movie movie) {
        return sawMovieRepository.findSawMovieByMemberAndMovie(member, movie);
    }

    /**
     * 내가 시청횟수를 등록한 영화 목록을 조회하기 위한 메서드
     */
    public Page<SawMovie> getSawMovieList(Member member, PageRequest pageable) {
        return sawMovieRepository.findSawMoviesByMemberWithPaging(member,pageable);
    }

    /**
     * 시청 횟수 등록용
     * 기존에 시청횟수를 등록하지 않았다면, SawMovie 객체를 새로 생성, 등록했다면, SawMovie 객체를 가져와서 시청횟수를 수정한다.
     */
    public void saveSawMovie(Movie movie, Member member, int movieSawCount) {
        SawMovie findMovie = getSawMovie(member,movie);
        SawMovie sawMovie;

        if (Optional.ofNullable(findMovie).isEmpty()) {
            sawMovie = new SawMovie();
            sawMovie.setMovieSawCount(movieSawCount);
            setMovieRelation(movie,sawMovie);
            log.info("sawMovie new");
            calculateAvgCount(movie);
        } else {
            sawMovie = findMovie;
            sawMovie.setMovieSawCount(movieSawCount);
            calculateAvgCount(movie);
            log.info("sawMovie exist");
        }

        setMemberRelation(member,sawMovie);
        sawMovieRepository.save(sawMovie);
        log.info("시청횟수 완료 -> 회원 이메일 : {}, 영화 제목 : {}, 평균시청횟수 : {}",member.getEmail(),movie.getMovieTitle(),movie.getAvgSawCount());
    }

    /**
     * 영화 평균 시청횟수 개수용
     * 영화에 등록된 시청횟수를 집계해서 영화의 평균 시청횟수를 구한다.
     */
    private static void calculateAvgCount(Movie movie) {
        log.info("sawMovie size = {}", movie.getSawMovies().size());
        int sum =0;
        for (SawMovie updateSawMovie : movie.getSawMovies()) {
            sum+= updateSawMovie.getMovieSawCount();
        }
        log.info("sawMovie total sum = {}",sum);
        double avgSawCount = (double) sum / movie.getSawMovies().size();
        movie.setAvgSawCount(Math.round(avgSawCount*10)/10.0);
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
