package shallwe.movie.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MovieService {
    private final MovieRepository movieRepository;

    // ============================ 관리자 요청 처리 메소드 ==============================
    /** 1.관리자 영화 목록 조회
     * sort -> 가입일, 시청영화순, 경고수, 차단여부
     * page -> 화면에서 회원이 선택한 페이지가 넘어 온다.
     */
    public PagingResponseDto<MovieDto.Response> findAllMovie(int page, String sort) {
        Page<Movie> pageInfo = movieRepository.findAll(PageRequest.of(page-1,10,Sort.by(sort).descending()));
        List<MovieDto.Response> movieRepDtoList = getMovieListAdmin(pageInfo);

        return new PagingResponseDto<>(movieRepDtoList,pageInfo);
    }

    /** 2. 관리자 영화 검색 조회
     * title -> 관리자의 검색 대상
     * sort -> 등록일 평균 시청횟수순 정렬
     * page -> 화면에서 회원이 선택한 페이지가 넘어 온다.
     */
    public PagingResponseDto<MovieDto.Response> adminSearchMovie(String title, int page, String sort) {
        Page<Movie> pageInfo = movieRepository.findMovieByTitleWithPaging(title,PageRequest.of(page,10, Sort.by(sort).descending()));
        List<MovieDto.Response> movieRepDtoList = getMovieListAdmin(pageInfo);

        return new PagingResponseDto<>(movieRepDtoList,pageInfo,title);
    }

    //================================= 중복 제거용 메소드 ================================
    private static List<MovieDto.Response> getMovieListAdmin(Page<Movie> pageInfo) {
        List<Movie> movies = pageInfo.getContent();
        List<MovieDto.Response> movieRepDtoList = new ArrayList<>();
        for (Movie movie : movies) {
            MovieDto.Response movieRepDto = MovieDto.Response.builder()
                    .title(movie.getMovieTitle())
                    .createdAt(movie.getCreatedAt())
                    .build();
            movieRepDtoList.add(movieRepDto);
        }
        return movieRepDtoList;
    }
}
