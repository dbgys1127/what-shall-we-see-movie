package shallwe.movie.movie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.comment.service.CommentService;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.exception.BusinessLogicException;
import shallwe.movie.exception.ExceptionCode;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.repository.MovieRepository;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.sawmovie.entity.SawMovie;
import shallwe.movie.sawmovie.service.SawMovieService;
import shallwe.movie.wantmovie.entity.WantMovie;
import shallwe.movie.wantmovie.service.WantMovieService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
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
    private final CommentService commentService;
    private final S3UploadService s3UploadService;


    // ============================ 일반 유저 요청 처리 메소드 =============================
    @Cacheable(value = "searchMovie",key = "#movieGenre.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MovieDto.Response> searchMovieByGenre(String movieGenre, int page, String sort) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        Page<Movie> pageInfo = movieRepository.findMovieByGenreWithPaging(movieGenre, pageable);
        List<MovieDto.Response> movieRepDtoList = getMovieList(pageInfo);
        return new PagingResponseDto<>(movieRepDtoList,pageInfo,"",movieGenre);
    }


    @Cacheable(value = "movieOne",key = "#movie.movieTitle",cacheManager = "contentCacheManager",unless = "#result == null")
    public MovieDto.Response pickMovie(Member member, Movie movie) {
        SawMovie sawMovie=sawMovieService.getSawMovie(member, movie);
        WantMovie wantMovie = wantMovieService.getWantMovie(member, movie);
        MovieDto.Response movieRepDto = getSawCount(movie, sawMovie);
        movieRepDto.setCurrentMember(member.getEmail());
        if (Optional.ofNullable(wantMovie).isEmpty()) {
            movieRepDto.setIsWant("off");
        } else {
            movieRepDto.setIsWant("on");
        }

        return movieRepDto;
    }

    @Caching(evict = {
            @CacheEvict(value = "movieOne",key = "#movie.movieTitle",cacheManager = "contentCacheManager"),
            @CacheEvict(value = "allMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "mySawMovie",allEntries = true,cacheManager = "contentCacheManager"),
    })
    public void updateSawCount(Member member, Movie movie, int movieSawCount) {
        log.info("시청횟수 등록 시도 -> 회원 이메일 : {}, 영화 제목 : {}, 시청횟수 : {}",member.getEmail(),movie.getMovieTitle(),movieSawCount);
        sawMovieService.saveSawMovie(movie, member, movieSawCount);
    }

    @Caching(evict = {
            @CacheEvict(value = "movieOne",key = "#movie.movieTitle",cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myWantMovie",allEntries = true,cacheManager = "contentCacheManager"),
    })
    public void updateWantMovie(Member member, Movie movie, String isWant) {
        if (isWant.equals("on")) {
            log.info("영화 찜 상태변경 시도 -> 회원 이메일 : {}, 영화 제목 : {}, 찜등록  : {}",member.getEmail(),movie.getMovieTitle());
            wantMovieService.saveWantMovie(member, movie);
        } else {
            log.info("영화 찜 상태변경 시도 -> 회원 이메일 : {}, 영화 제목 : {}, 찜등록 취소  : {}",member.getEmail(),movie.getMovieTitle());
            wantMovieService.deleteWantMovie(member,movie);
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "movieOne",key = "#movie.movieTitle",cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myComment",allEntries = true,cacheManager = "contentCacheManager"),
    })
    public void writeMovieComment(Member member, Movie movie, CommentDto.Post commentDto) {
        log.info("댓글 등록 시도 -> 회원 이메일 : {}, 영화 제목 : {}",member.getEmail(),movie.getMovieTitle());
        commentService.saveMovieComment(member, movie, commentDto);
    }
    @Caching(evict = {
            @CacheEvict(value = "movieOne",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myComment",allEntries = true,cacheManager = "contentCacheManager"),
    })
    public void addMovieCommentClaim(Long commentId) {
        log.info("댓글 신고 등록 시도 -> 신고 대상 댓글 : {}",commentId);
        commentService.addMovieCommentClaim(commentId);
    }

    @Caching(evict = {
            @CacheEvict(value = "movieOne",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myComment",allEntries = true,cacheManager = "contentCacheManager"),
    })
    public void deleteMovieComment(Long commentId) {
        log.info("댓글 삭제 시도 -> 삭제 대상 댓글 : {}",commentId);
        commentService.deleteMovieComment(commentId);
    }
    // ============================ 관리자 요청 처리 메소드 ==============================
    @Caching(evict = {
            @CacheEvict(value = "allMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchMovie",allEntries = true,cacheManager = "contentCacheManager")
    })
    public MovieDto.Response createMovie(MultipartFile multipartFile, MovieDto.Post movieDto) throws IOException {
        log.info("영화 등록 시도 -> 영화 제목 : {}",movieDto.getMovieTitle());
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
        log.info("영화 등록 완료 -> 영화 제목 : {}",movieDto.getMovieTitle());
        return movieRepDto;
    }


    /** 1.관리자 영화 목록 조회
     * sort -> 가입일, 시청영화순, 경고수, 차단여부
     * page -> 화면에서 회원이 선택한 페이지가 넘어 온다.
     */
    @Cacheable(value = "allMovie",key = "#sort.concat('-').concat(#page)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MovieDto.Response> findAllMovie(int page, String sort) {
        log.info("영화 목록 조회 -> 조회 페이지 : {}, 조회 정렬 기준 : {}",page,sort);
        Page<Movie> pageInfo = movieRepository.findAll(PageRequest.of(page,10,Sort.by(sort).descending()));
        List<MovieDto.Response> movieRepDtoList = getMovieList(pageInfo);

        return new PagingResponseDto<>(movieRepDtoList,pageInfo);
    }

    /** 2. 관리자 영화 검색 조회
     * title -> 관리자의 검색 대상
     * sort -> 등록일 평균 시청횟수순 정렬
     * page -> 화면에서 회원이 선택한 페이지가 넘어 온다.
     */
    @Cacheable(value = "searchMovie",key = "#title.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<MovieDto.Response> searchMovieByTitle(String title, int page, String sort) {
        log.info("영화 검색 시도 -> 검색어 : {}",title);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        Page<Movie> pageInfo = movieRepository.findMovieByTitleWithPaging(title, pageable);
        List<MovieDto.Response> movieRepDtoList = getMovieList(pageInfo);
        log.info("영화 검색 결과 -> 검색어 : {}, 검색된 결과 : {}",title,pageInfo.getTotalElements());
        return new PagingResponseDto<>(movieRepDtoList,pageInfo,title,"");
    }

    @Caching(evict = {
            @CacheEvict(value = "allMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "mySawMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myWantMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myComment",allEntries = true,cacheManager = "contentCacheManager")
    })
    public MovieDto.Response updateMovie(MultipartFile multipartFile, MovieDto.Patch movieDto) throws IOException {
        log.info("영화 수정 시도 -> 수정 대상 영화 제목 : {}",movieDto.getMovieTitle());
        Movie movie = is_exist_movie(movieDto.getPreMovieTitle());
        isUpdateImage(multipartFile,movie);
        movie.setMovieTitle(movieDto.getMovieTitle());
        movie.setMovieRunningTime(movieDto.getMovieRunningTime());
        movie.setMovieOpenDate(movieDto.getMovieOpenDate());
        movie.setMovieGenre(movieDto.getMovieGenre());
        movie.setMovieDescription(movieDto.getMovieDescription());
        log.info("영화 수정 완료 -> 수정 대상 영화 아이디 : {}",movie.getMovieId());
        return getMovieRepDto(movie);
    }

    @Caching(evict = {
            @CacheEvict(value = "allMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "searchMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "mySawMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myWantMovie",allEntries = true,cacheManager = "contentCacheManager"),
            @CacheEvict(value = "myComment",allEntries = true,cacheManager = "contentCacheManager")
    })
    public PagingResponseDto<MovieDto.Response> deleteMovie(String movieTitle) {
        log.info("영화 삭제 시도 -> 삭제 대상 영화 제목 : {}",movieTitle);
        movieRepository.deleteByMovieTitle(movieTitle);
        log.info("영화 삭제 완료 -> 삭제 대상 영화 제목 : {}",movieTitle);
        return findAllMovie(0, "movieId");
    }

    //================================= 중복 제거용 메소드 ================================

    public void verifyExistsTitle(String movieTitle) {
        Optional<Movie> movie = movieRepository.findByMovieTitle(movieTitle);
        log.info("영화 DB 중복 조회 시도 -> 영화 제목 : {}",movieTitle);
        if (movie.isPresent()) {
            log.error("영화 DB 중복 확인 -> 영화 제목 : {}",movieTitle);
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_THIS_MOVIE);
        }
    }

    public Movie is_exist_movie(String movieTitle) {
        log.info("영화 DB 조회 시도 -> 영화 제목 : {}",movieTitle);
        Optional<Movie> optionalMovie = movieRepository.findByMovieTitle(movieTitle); // DB에서 회원 조회
        if (optionalMovie.isEmpty()) {
            log.error("영화 DB 조회 실패 -> 영화 제목 : {}",movieTitle);
        }
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
                    .createdAt(movie.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
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
                .comments(CommentDto.getCommentResponseDtoList(savedMovie.getComments()))
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
