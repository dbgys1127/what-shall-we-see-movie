package shallwe.movie.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.service.SawMovieService;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.service.WantMovieService;

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
    private final SawMovieService sawMovieService;
    private final WantMovieService wantMovieService;
    private final S3UploadService s3UploadService;
    private final MemberService memberService;

    // ============================ 일반 유저 요청 처리 메소드 ==============================
    public PagingResponseDto<MovieDto.Response> searchMovieByGenre(String movieGenre, int page, String sort) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        Page<Movie> pageInfo = movieRepository.findMovieByGenreWithPaging(movieGenre, pageable);
        List<MovieDto.Response> movieRepDtoList = getMovieList(pageInfo);
        return new PagingResponseDto<>(movieRepDtoList,pageInfo,"",movieGenre);
    }

    public MovieDto.Response pickMovie(String movieTitle, String email) {
        Movie findMovie = is_exist_movie(movieTitle);
        Member findMember = memberService.is_exist_member(email);
        SawMovie sawMovie=sawMovieService.getSawMovie(findMovie, findMember);
        WantMovie wantMovie = wantMovieService.getWantMovie(findMember, findMovie);
        MovieDto.Response movieRepDto = getSawCount(findMovie, sawMovie);
        if (Optional.ofNullable(wantMovie).isEmpty()) {
            movieRepDto.setIsWant("off");
        } else {
            movieRepDto.setIsWant("on");
        }
        return movieRepDto;
    }
    public void updateSawCount(String movieTitle, String email, int movieSawCount) {
        Movie findMovie = is_exist_movie(movieTitle);
        Member findMember = memberService.is_exist_member(email);
        sawMovieService.saveSawMovie(findMovie, findMember, movieSawCount);

    }
    public void updateWantMovie(String movieTitle, String email, String isWant) {
        Movie findMovie = is_exist_movie(movieTitle);
        Member findMember = memberService.is_exist_member(email);
        if (isWant.equals("on")) {
            wantMovieService.saveWantMovie(findMember, findMovie);
        } else {
            wantMovieService.deleteWantMovie(findMember,findMovie);
        }
    }
    // ============================ 관리자 요청 처리 메소드 ==============================
    public MovieDto.Response createMovie(MultipartFile multipartFile, MovieDto.Post movieDto) throws IOException {
        verifyExistsTitle(movieDto.getMovieTitle());
        Movie movie = Movie.builder()
                .movieTitle(movieDto.getMovieTitle())
                .moviePoster(s3UploadService.upload(multipartFile))
                .movieRunningTime(movieDto.getMovieRunningTime())
                .movieDescription(movieDto.getMovieDescription())
                .movieGenre(movieDto.getMovieGenre())
                .movieOpenDate(movieDto.getMovieOpenDate())
                .build();

        Movie savedMovie = movieRepository.save(movie);

        MovieDto.Response movieRepDto = getMovieRepDto(savedMovie);
        return movieRepDto;
    }


    /** 1.관리자 영화 목록 조회
     * sort -> 가입일, 시청영화순, 경고수, 차단여부
     * page -> 화면에서 회원이 선택한 페이지가 넘어 온다.
     */
    public PagingResponseDto<MovieDto.Response> findAllMovie(int page, String sort) {
        Page<Movie> pageInfo = movieRepository.findAll(PageRequest.of(page,10,Sort.by(sort).descending()));
        List<MovieDto.Response> movieRepDtoList = getMovieList(pageInfo);

        return new PagingResponseDto<>(movieRepDtoList,pageInfo);
    }

    /** 2. 관리자 영화 검색 조회
     * title -> 관리자의 검색 대상
     * sort -> 등록일 평균 시청횟수순 정렬
     * page -> 화면에서 회원이 선택한 페이지가 넘어 온다.
     */
    public PagingResponseDto<MovieDto.Response> searchMovieByTitle(String title, int page, String sort) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        Page<Movie> pageInfo = movieRepository.findMovieByTitleWithPaging(title, pageable);
        List<MovieDto.Response> movieRepDtoList = getMovieList(pageInfo);
        return new PagingResponseDto<>(movieRepDtoList,pageInfo,title,"");
    }

    public MovieDto.Response updateMovie(MultipartFile multipartFile, MovieDto.Patch movieDto) throws IOException {
        Movie movie = is_exist_movie(movieDto.getMovieTitle());
        isUpdateImage(multipartFile,movie);
        movie.setMovieTitle(movieDto.getMovieTitle());
        movie.setMovieRunningTime(movieDto.getMovieRunningTime());
        movie.setMovieOpenDate(movieDto.getMovieOpenDate());
        movie.setMovieGenre(movieDto.getMovieGenre());
        movie.setMovieDescription(movieDto.getMovieDescription());

        return getMovieRepDto(movie);
    }
    public PagingResponseDto<MovieDto.Response> deleteMovie(String movieTitle) {
        movieRepository.deleteByMovieTitle(movieTitle);
        return findAllMovie(0, "movieId");
    }



    //================================= 중복 제거용 메소드 ================================

    public void verifyExistsTitle(String movieTitle) {
        Optional<Movie> movie = movieRepository.findByMovieTitle(movieTitle);
        if (movie.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_THIS_MOVIE);
        }
    }

    public Movie is_exist_movie(String movieTitle) {
        Optional<Movie> optionalMovie = movieRepository.findByMovieTitle(movieTitle); // DB에서 회원 조회
        Movie findMovie = optionalMovie.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MOVIE_CANNOT_FIND));
        return findMovie;
    }

    private static List<MovieDto.Response> getMovieList(Page<Movie> pageInfo) {
        List<Movie> movies = pageInfo.getContent();
        List<MovieDto.Response> movieRepDtoList = new ArrayList<>();
        for (Movie movie : movies) {
            MovieDto.Response movieRepDto = MovieDto.Response.builder()
                    .movieTitle(movie.getMovieTitle())
                    .moviePoster(movie.getMoviePoster())
                    .movieRunningTime(movie.getMovieRunningTime())
                    .movieOpenDate(movie.getMovieOpenDate())
                    .movieGenre(movie.getMovieGenre())
                    .movieDescription(movie.getMovieDescription())
                    .avgSawCount(movie.getAvgSawCount())
                    .build();

            movieRepDtoList.add(movieRepDto);
        }
        return movieRepDtoList;
    }

    private static MovieDto.Response getMovieRepDto(Movie savedMovie) {
        MovieDto.Response movieRepDto = MovieDto.Response.builder()
                .movieTitle(savedMovie.getMovieTitle())
                .moviePoster(savedMovie.getMoviePoster())
                .movieRunningTime(savedMovie.getMovieRunningTime())
                .movieOpenDate(savedMovie.getMovieOpenDate())
                .movieGenre(savedMovie.getMovieGenre())
                .movieDescription(savedMovie.getMovieDescription())
                .build();
        return movieRepDto;
    }

    //3. 영화 수정시 등록하지 않으면 s3에 이미지를 업로드 하지 않게 한다.
    public void isUpdateImage(MultipartFile multipartFile, Movie findMovie) throws IOException {
        String url = s3UploadService.upload(multipartFile);
        if (!url.equals("")) {
            findMovie.setMoviePoster(url);
        }
    }
    private static MovieDto.Response getSawCount(Movie findMovie, SawMovie sawMovie) {
        MovieDto.Response movieRepDto = getMovieRepDto(findMovie);
        movieRepDto.setAvgSawCount(findMovie.getAvgSawCount());
        if (Optional.ofNullable(sawMovie).isPresent()) {
            movieRepDto.setMemberSawCount(sawMovie.getMovieSawCount());
        } else {
            movieRepDto.setMemberSawCount(0);
        }
        return movieRepDto;
    }


}
