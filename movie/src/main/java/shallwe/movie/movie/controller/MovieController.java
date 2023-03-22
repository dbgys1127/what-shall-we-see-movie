package shallwe.movie.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.service.MovieService;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

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
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.findAllMovie(page, sort);
        model.addAttribute("pageData", pageRepDto);
        return "movie/movies";
    }

    /** 관리자 영화 검색 메서드
     * Param page -> 관리자가 화면에서 선택한 페이지가 출력된다.
     * Param sort -> 관리자가 화면에서 선택한 정렬 기준대로 출력된다.
     */
    @GetMapping("/admin/movie/search")
    public String adminSearchMovies(@RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "movieTitle",required = false) String movieTitle, Model model) {
        log.info("title={}",movieTitle);
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.adminSearchMovie(movieTitle, page - 1, "movieId");
        model.addAttribute("pageData", pageRepDto);
        return "movie/movieSearchResult";
    }
}
