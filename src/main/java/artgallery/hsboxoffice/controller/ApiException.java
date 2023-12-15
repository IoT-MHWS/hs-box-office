package artgallery.hsboxoffice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * ApiException
 */
@RequiredArgsConstructor
public class ApiException extends Throwable {
    private final ApiError apiError;

    ApiException(HttpStatus status) {
        apiError = new ApiError(status);
    }

    ApiException(HttpStatus status, Throwable ex) {
        apiError = new ApiError(status, ex);
    }

    ApiException(HttpStatus status, String message) {
        apiError = new ApiError(status, message);
    }

    ApiException(HttpStatus status, String message, Throwable ex) {
        apiError = new ApiError(status, message, ex);
    }

    public ApiError get() {
        return apiError;
    }
}
