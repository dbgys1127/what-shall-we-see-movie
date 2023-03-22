package shallwe.movie.movie.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.movie.dto.MovieDto;
import shallwe.movie.movie.service.MovieService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/admin/movie")
    public String adminGetMovies(@RequestParam("page") int page,
                                  @RequestParam(value = "sort",defaultValue = "movieId") String sort, Model model) {
        PagingResponseDto<MovieDto.Response> pageRepDto = movieService.findAllMovie(page, sort);
        log.info("MO_startPage={}",pageRepDto.getStartPage());
        model.addAttribute("pageData", pageRepDto);
        return "movie/movie";
    }
}
