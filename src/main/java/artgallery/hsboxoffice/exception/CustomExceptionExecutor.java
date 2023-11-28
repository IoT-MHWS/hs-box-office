package artgallery.hsboxoffice.exception;

import artgallery.hsboxoffice.controller.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomExceptionExecutor {

    @ResponseBody
    @ExceptionHandler({
            DoesNotExistException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(Exception ex) {

        return new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({
            DatabaseConflictException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exIndb(Exception ex) {

        return new ApiError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError notCorrect(Exception ex) {

        return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

}
