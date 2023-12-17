package artgallery.hsboxoffice.configuration;

import artgallery.hsboxoffice.controller.ApiError;
import artgallery.hsboxoffice.exception.DatabaseConflictException;
import artgallery.hsboxoffice.exception.DoesNotExistException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler({
            DoesNotExistException.class
    })
    public ResponseEntity<?> notFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler({
            DatabaseConflictException.class
    })
    public ResponseEntity<?> exIndb(Exception ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<?> notCorrect(Exception ex) {

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler({
        FeignException.class
    })
    public ResponseEntity<?> feignException(FeignException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(ex.status())
            .body(ex.contentUTF8());
    }
}
