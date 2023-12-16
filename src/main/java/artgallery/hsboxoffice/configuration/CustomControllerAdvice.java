package artgallery.hsboxoffice.configuration;

import artgallery.hsboxoffice.controller.ApiError;
import artgallery.hsboxoffice.exception.DatabaseConflictException;
import artgallery.hsboxoffice.exception.DoesNotExistException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomControllerAdvice {

    @ResponseBody
    @ExceptionHandler({
            DoesNotExistException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(Exception ex) {

        return new ApiError(HttpStatus.NOT_FOUND, ex);
    }

    @ResponseBody
    @ExceptionHandler({
            DatabaseConflictException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exIndb(Exception ex) {

        return new ApiError(HttpStatus.CONFLICT, ex);
    }

    @ResponseBody
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError notCorrect(Exception ex) {

        return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    @ResponseBody
    @ExceptionHandler({
        FeignException.class
    })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiError feignException(Exception ex) {
        return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }
}
