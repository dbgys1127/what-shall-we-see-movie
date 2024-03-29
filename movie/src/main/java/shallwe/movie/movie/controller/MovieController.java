package shallwe.movie.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shallwe.movie.aop.NeedMemberAndMovieTitle;
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.member.dto.MemberDto;
import shallwe.movie.member.entity.Member;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.entity.Movie;
import shallwe.movie.movie.service.MovieService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    //==========================사용자 요청 처리 API=========================

    /**
     * 영화 목록 조회용이며 메인 페이지
     */
    @GetMapping("/movie")
    public String getMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                            @RequestParam(value = "sort", defaultValue = "movieOpenDate") String sort, Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.findAllMovie(page - 1, sort);
        model.addAttribute("pageData", pageRepDto);
        return "movie/member-movie-list/allMovie";
    }

    /**
     * 영화 검색용
     */
    @GetMapping("/movie/search/title")
    public String getSearchByTitleMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                                  @RequestParam(value = "movieTitle",required = false) String movieTitle,
                                  Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.searchMovieByTitle(movieTitle,page - 1, "movieId");
        model.addAttribute("pageData", pageRepDto);
        return "movie/member-movie-list/memberMovieSearchResult";
    }

    /**
     * 영화 장르별 검색용
     */
    @GetMapping("/movie/search/genre")
    public String getSearchByGenreMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                                         @RequestParam(value="movieGenre", required=false) String movieGenre,
                                         Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.searchMovieByGenre(movieGenre,page - 1, "movieId");
        model.addAttribute("pageData", pageRepDto);
        return "movie/member-movie-list/memberMovieSearchResult";
    }

    /**
     * 영화 상세페이지용
     */
    @NeedMemberAndMovieTitle
    @GetMapping("/movie/detail")
    public String getMovie(Member member, Movie movie,
                           Model model) {
        MovieDto.Response movieRepDto=movieService.pickMovie(member,movie);
        model.addAttribute("movie", movieRepDto);
        return "movie/movieDetail";
    }

    /**
     * 사용자의 영화 시청횟수 등록용
     */
    @NeedMemberAndMovieTitle
    @PostMapping("/movie/saw-movie")
    @ResponseBody
    public void postSawCount(Member member, Movie movie,
                               @RequestParam("movieSawCount") int movieSawCount) {
        movieService.updateSawCount(member, movie,movieSawCount);
    }

    /**
     * 사용자의 영화 찜 등록용
     */
    @NeedMemberAndMovieTitle
    @PostMapping("/movie/want-movie")
    @ResponseBody
    public void postWantMovie(Member member, Movie movie,
                                @RequestParam(value = "wantMovie", defaultValue = "off") String isWant) {
        movieService.updateWantMovie(member, movie, isWant);
    }

    /**
     * 사용자의 영화에 대한 댓글 등록용
     */
    @NeedMemberAndMovieTitle
    @PostMapping("/movie/comment")
    @ResponseBody
    public void postComment(Member member, Movie movie,
                              @ModelAttribute @Valid CommentDto.Post commentDto) {
        movieService.writeMovieComment(member, movie, commentDto);
    }

    /**
     * 사용자의 영화에 대한 댓글 수정용
     */
    @PostMapping("/movie/comment/patch")
    @ResponseBody
    public void patchComment(@RequestParam("commentId") Long commentId, @ModelAttribute CommentDto.Patch commentDto){
        movieService.patchMovieComment(commentId, commentDto);
    }

    /**
     * 사용자의 댓글 신고용
     */
    @PostMapping("/movie/comment/claim")
    @ResponseBody
    public void postCommentClaim(@RequestParam("commentId") Long commentId) {
        movieService.addMovieCommentClaim(commentId);
    }

    /**
     * 사용자의 댓글 삭제용
     */
    @PostMapping("/movie/comment/delete")
    @ResponseBody
    public void deleteComment(@RequestParam("commentId") Long commentId) {
        movieService.deleteMovieComment(commentId);
    }

    //==========================관리자 요청 처리 API=========================

    /**
     * 영화 등록시 등록된 동일 제목 영화가 있는지 판단용
     */
    @PostMapping("/admin/movie/check")
    @ResponseBody
    public int checkEmail(@RequestParam("movieTitle") String movieTitle) {
        int cnt = movieService.verifyExistsTitle(movieTitle);
        return cnt;
    }

    /**
     * 관리자의 영화 등록용
     */
    @PostMapping("/admin/movie")
    public String postMovie(@RequestPart("moviePoster") MultipartFile multipartFile,
                            @ModelAttribute @Valid MovieDto.Post movieDto, Model model) throws IOException {
        MovieDto.Response savedMovie = movieService.createMovie(multipartFile, movieDto);
        model.addAttribute("movie", savedMovie);
        return "movie/movie";
    }

    /**
     * 관리자 영화 목록 조회 메서드
     */
    @GetMapping("/admin/movie")
    public String adminGetMovies(@RequestParam("page") int page,
                                  @RequestParam(value = "sort",defaultValue = "movieId") String sort, Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.findAllMovie(page-1, sort);
        model.addAttribute("pageData", pageRepDto);
        return "movie/admin/movies";
    }

    /**
     * 관리자 영화 검색 메서드
     */
    @GetMapping("/admin/movie/search")
    public String adminSearchMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "movieTitle",required = false) String movieTitle,
                                    Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.searchMovieByTitle(movieTitle,page - 1, "movieId");
        model.addAttribute("pageData", pageRepDto);
        return "movie/admin/movieSearchResult";
    }

    /**
     * 수정할 영화 페이지 가져오기
     */
    @NeedMemberAndMovieTitle
    @GetMapping("/admin/movie/patch")
    public String adminGetMovie(Member member, Movie movie, Model model) {
        MovieDto.Response movieRepDto=movieService.pickMovie(member,movie);
        model.addAttribute("movie", movieRepDto);
        return "movie/admin/moviePatch";
    }

    /**
     * 영화 내용 수정
     */
    @PostMapping("/admin/movie/patch")
    public String adminPatchMovie(@RequestPart(value = "moviePoster",required = false) MultipartFile multipartFile,
                                  @ModelAttribute @Valid MovieDto.Patch movieDto, Model model) throws IOException {
        MovieDto.Response movieRepDto = movieService.updateMovie(multipartFile, movieDto);
        model.addAttribute("movie", movieRepDto);
        return "movie/movie";
    }

    /**
     * 영화 삭제 메서드
     */
    @PostMapping("/admin/movie/delete")
    @ResponseBody
    public void adminDeleteMovie(@RequestParam("movieTitle") String movieTitle) {
        movieService.deleteMovie(movieTitle);
    }
}
