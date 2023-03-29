package shallwe.movie.movie.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import shallwe.movie.movie.entity.Movie;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovieDto {

    @Getter
    @Setter
    public static class Post {
        @NotNull
        @Size(max=20)
        private String movieTitle;

        @NotNull
        @Size(max=300)
        private String movieDescription;

        @NotNull
        @Positive
        private int movieRunningTime;

        @NotNull
        private Movie.MovieGenre movieGenre;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate movieOpenDate;

        @Builder
        public Post(String movieTitle, String movieDescription,
                    int movieRunningTime, Movie.MovieGenre movieGenre,
                    LocalDate movieOpenDate) {
            this.movieTitle = movieTitle;
            this.movieDescription = movieDescription;
            this.movieRunningTime = movieRunningTime;
            this.movieGenre = movieGenre;
            this.movieOpenDate = movieOpenDate;
        }
    }

    @Getter
    @Setter
    public static class Patch {

        @NotNull
        @Size(max=20)
        private String movieTitle;

        @NotNull
        @Size(max=300)
        private String movieDescription;

        @NotNull
        @Positive
        private int movieRunningTime;

        @NotNull
        private Movie.MovieGenre movieGenre;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate movieOpenDate;

        @Builder
        public Patch(String movieTitle, String movieDescription,
                     int movieRunningTime, Movie.MovieGenre movieGenre,
                     LocalDate movieOpenDate) {
            this.movieTitle = movieTitle;
            this.movieDescription = movieDescription;
            this.movieRunningTime = movieRunningTime;
            this.movieGenre = movieGenre;
            this.movieOpenDate = movieOpenDate;
        }
    }



    @Getter
    @Setter
    public static class Response {
        private String movieTitle;
        private String moviePoster;
        private String movieDescription;
        private int movieRunningTime;
        private Movie.MovieGenre movieGenre;
        private LocalDate movieOpenDate;
        private LocalDateTime createdAt;

        private int memberSawCount;

        private double avgSawCount;

        private String isWant;

        @Builder
        public Response(String movieTitle, String moviePoster,
                        String movieDescription, int movieRunningTime,
                        Movie.MovieGenre movieGenre, LocalDate movieOpenDate,
                        LocalDateTime createdAt, double avgSawCount,String isWant) {
            this.movieTitle = movieTitle;
            this.moviePoster = moviePoster;
            this.movieDescription = movieDescription;
            this.movieRunningTime = movieRunningTime;
            this.movieGenre = movieGenre;
            this.movieOpenDate = movieOpenDate;
            this.createdAt = createdAt;
            this.avgSawCount = avgSawCount;
            this.isWant = isWant;
        }
    }
}
