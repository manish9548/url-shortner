package url_shortener.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UrlNotFoundException.class)
    public ErrorResponse handleUrlNotFound(UrlNotFoundException exception){
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );
    }
    @ExceptionHandler(UrlExpiredException.class)
    public ErrorResponse handleUrlExpired(UrlExpiredException exception) {

        return new ErrorResponse(
                HttpStatus.GONE.value(),
                exception.getMessage()
        );
    }

}
