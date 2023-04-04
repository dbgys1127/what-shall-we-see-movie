package shallwe.movie.movie.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.format.annotation.DateTimeFormat;
import shallwe.movie.comment.dto.CommentDto;
import shallwe.movie.movie.entity.Movie;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @RedisHash(value = "movieOne",timeToLive = 600)
    public static class Response implements Serializable {
        @Id
        private String movieTitle;
        private String moviePoster;
        private String movieDescription;
        private int movieRunningTime;
        private Movie.MovieGenre movieGenre;

//        @JsonSerialize(using = LocalDateSerializer.class)
//        @JsonDeserialize(using = LocalDateDeserializer.class)
        private LocalDate movieOpenDate;

//        @JsonSerialize(using = LocalDateTimeSerializer.class)
//        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime createdAt;

        private int memberSawCount;

        private double avgSawCount;

        private String isWant;

        private String currentMember;

        private List<CommentDto.Response> comments;

        @Builder
        public Response(String movieTitle, String moviePoster,
                        String movieDescription, int movieRunningTime,
                        Movie.MovieGenre movieGenre, LocalDate movieOpenDate,
                        LocalDateTime createdAt, double avgSawCount,String isWant,
                        String currentMember,
                        List<CommentDto.Response> comments) {
            this.movieTitle = movieTitle;
            this.moviePoster = moviePoster;
            this.movieDescription = movieDescription;
            this.movieRunningTime = movieRunningTime;
            this.movieGenre = movieGenre;
            this.movieOpenDate = movieOpenDate;
            this.createdAt = createdAt;
            this.avgSawCount = avgSawCount;
            this.isWant = isWant;
            this.currentMember = currentMember;
            this.comments = comments;
        }
    }
}
