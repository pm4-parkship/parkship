package ch.zhaw.parkship.errorHandling;


import ch.zhaw.parkship.reservation.ReservationCanNotBeCanceledException;
import ch.zhaw.parkship.reservation.ReservationNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ExceptionControllerAdvice {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        return buildResponseEntity(processFieldErrors(fieldErrors, ex));
    }

    private ApiError processFieldErrors(List<FieldError> fieldErrors, Exception ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, "validation error", ex);
        for(FieldError fieldError : fieldErrors) {
            apiError.addSubError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return apiError;
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    protected ResponseEntity<Object> handleReservationNotFoundException(ReservationNotFoundException ex) {
        return buildResponseEntity(new ApiError(NOT_FOUND,ex.getMessage(), ex));
    }

    @ExceptionHandler(ReservationCanNotBeCanceledException.class)
    protected ResponseEntity<Object> handleReservationCanNotBeCanceledException(ReservationCanNotBeCanceledException ex) {
        return buildResponseEntity(new ApiError(BAD_REQUEST,ex.getMessage(), ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }



}
