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

    //==========================사용자 화면 컨트롤러=========================

    @GetMapping("/movie")
    public String getMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                            @RequestParam(value = "sort", defaultValue = "movieOpenDate") String sort, Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.findAllMovie(page - 1, sort);
        model.addAttribute("pageData", pageRepDto);
        return "movie/member-movie-list/allMovie";
    }

    @GetMapping("/movie/search/title")
    public String getSearchByTitleMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                                  @RequestParam(value = "movieTitle",required = false) String movieTitle,
                                  Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.searchMovieByTitle(movieTitle,page - 1, "movieId");
        model.addAttribute("pageData", pageRepDto);
        return "movie/member-movie-list/memberMovieSearchResult";
    }

    @GetMapping("/movie/search/genre")
    public String getSearchByGenreMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                                         @RequestParam(value="movieGenre", required=false) String movieGenre,
                                         Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.searchMovieByGenre(movieGenre,page - 1, "movieId");
        model.addAttribute("pageData", pageRepDto);
        return "movie/member-movie-list/memberMovieSearchResult";
    }

    @NeedMemberAndMovieTitle
    @GetMapping("/movie/detail")
    public String getMovie(Member member, Movie movie,
                           Model model) {
        MovieDto.Response movieRepDto=movieService.pickMovie(member,movie);
        model.addAttribute("movie", movieRepDto);
        return "movie/movieDetail";
    }

    @NeedMemberAndMovieTitle
    @PostMapping("/movie/saw-movie")
    @ResponseBody
    public String postSawCount(Member member, Movie movie,
                               @RequestParam("movieSawCount") int movieSawCount,
                               RedirectAttributes redirectAttributes) {

        movieService.updateSawCount(member, movie,movieSawCount);
        redirectAttributes.addAttribute("movieTitle", movie.getMovieTitle());
        return "redirect:/movie/detail";
    }

    @NeedMemberAndMovieTitle
    @PostMapping("/movie/want-movie")
    @ResponseBody
    public void postWantMovie(Member member, Movie movie,
                                @RequestParam(value = "wantMovie", defaultValue = "off") String isWant) {
        movieService.updateWantMovie(member, movie, isWant);
    }

    @NeedMemberAndMovieTitle
    @PostMapping("/movie/comment")
    @ResponseBody
    public void postComment(Member member, Movie movie,
                              @ModelAttribute @Valid CommentDto.Post commentDto) {
        movieService.writeMovieComment(member, movie, commentDto);
    }

    @PostMapping("/movie/comment/claim")
    public String postCommentClaim(@RequestParam("movieTitle") String movieTitle,
                                   @RequestParam("commentId") Long commentId,
                                   RedirectAttributes redirectAttributes) {
        movieService.addMovieCommentClaim(commentId);
        redirectAttributes.addAttribute("movieTitle", movieTitle);
        return "redirect:/movie/detail";
    }
    @PostMapping("/movie/comment/delete")
    public String deleteComment(@RequestParam("movieTitle") String movieTitle,
                                @RequestParam("commentId") Long commentId,
                                RedirectAttributes redirectAttributes) {
        movieService.deleteMovieComment(commentId);
        redirectAttributes.addAttribute("movieTitle", movieTitle);
        return "redirect:/movie/detail";
    }

    //==========================관리자 화면 컨트롤러=========================

    @PostMapping("/admin/movie")
    public String postMovie(@RequestPart("moviePoster") MultipartFile multipartFile,
                            @ModelAttribute @Valid MovieDto.Post movieDto, Model model) throws IOException {
        MovieDto.Response savedMovie = movieService.createMovie(multipartFile, movieDto);
        model.addAttribute("movie", savedMovie);
        return "movie/movie";
    }

    /** 관리자 영화 목록 조회 메서드
     * Param page -> 관리자가 화면에서 선택한 페이지가 출력된다.
     * Param sort -> 관리자가 화면에서 선택한 정렬 기준대로 출력된다.
      */
    @GetMapping("/admin/movie")
    public String adminGetMovies(@RequestParam("page") int page,
                                  @RequestParam(value = "sort",defaultValue = "movieId") String sort, Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.findAllMovie(page-1, sort);
        model.addAttribute("pageData", pageRepDto);
        return "movie/admin/movies";
    }

    /** 관리자 영화 검색 메서드
     * Param page -> 관리자가 화면에서 선택한 페이지가 출력된다.
     * Param sort -> 관리자가 화면에서 선택한 정렬 기준대로 출력된다.
     */
    @GetMapping("/admin/movie/search")
    public String adminSearchMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "movieTitle",required = false) String movieTitle,
                                    Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.searchMovieByTitle(movieTitle,page - 1, "movieId");
        model.addAttribute("pageData", pageRepDto);
        return "movie/admin/movieSearchResult";
    }

    // 수정할 영화 페이지 가져오기
    @NeedMemberAndMovieTitle
    @GetMapping("/admin/movie/patch")
    public String adminGetMovie(Member member, Movie movie, Model model) {
        MovieDto.Response movieRepDto=movieService.pickMovie(member,movie);
        model.addAttribute("movie", movieRepDto);
        return "movie/admin/moviePatch";
    }

    // 영화 내용 수정
    @PostMapping("/admin/movie/patch")
    public String adminPatchMovie(@RequestPart(value = "moviePoster",required = false) MultipartFile multipartFile,
                                  @ModelAttribute @Valid MovieDto.Patch movieDto, Model model) throws IOException {
        MovieDto.Response movieRepDto = movieService.updateMovie(multipartFile, movieDto);
        model.addAttribute("movie", movieRepDto);
        return "movie/movie";
    }

    // 영화 삭제 메서드
    @GetMapping("/admin/movie/delete")
    public String adminDeleteMovie(@RequestParam("movieTitle") String movieTitle, Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto=movieService.deleteMovie(movieTitle);
        model.addAttribute("pageData", pageRepDto);
        return "movie/movies";
    }
}
