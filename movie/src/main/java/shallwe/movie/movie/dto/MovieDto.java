package shallwe.movie.movie.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import shallwe.movie.movie.entity.Movie;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovieDto {

    @Getter
    @Setter
    public static class Response {
        private String title;
        private String moviePoster;
        private String movieDescription;
        private String movieRunningTime;
        private Movie.MovieGenre movieGenre;
        private LocalDate movieOpenDate;

        private LocalDateTime createdAt;

        @Builder
        public Response(String title, String moviePoster,
                        String movieDescription, String movieRunningTime,
                        Movie.MovieGenre movieGenre, LocalDate movieOpenDate,
                        LocalDateTime createdAt) {
            this.title = title;
            this.moviePoster = moviePoster;
            this.movieDescription = movieDescription;
            this.movieRunningTime = movieRunningTime;
            this.movieGenre = movieGenre;
            this.movieOpenDate = movieOpenDate;
            this.createdAt = createdAt;
        }
    }

}
