package shallwe.movie.exception;

import lombok.Getter;

public enum ExceptionCode {
    ALREADY_EXISTS_YOUR_EMAIL(404,"Already exists your email"),
    MEMBER_CANNOT_FIND(404,"Member can't find"),

    ALREADY_EXISTS_THIS_MOVIE(404,"Already exists this movie"),
    MOVIE_CANNOT_FIND(404,"Movie can't find");
    @Getter
    private int code;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
