package shallwe.movie.movie.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MovieFormController {

    @GetMapping("/admin/movie/add-movie-form")
    public String adminAddMovie() {
        return "movie/addMovie";
    }

}
