package ch.zhaw.parkship.errorhandling;


import org.hibernate.TransientPropertyValueException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        return buildResponseEntity(processFieldErrors(fieldErrors, ex));
    }

    private ApiError processFieldErrors(List<FieldError> fieldErrors, Exception ex) {
        ApiError apiError = new ApiError(BAD_REQUEST, "validation error", ex);
        for (FieldError fieldError : fieldErrors) {
            apiError.addSubError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return apiError;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return buildResponseEntity(new ApiError(BAD_REQUEST, ex.getMessage(), ex));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<Object> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        if (ex.getCause().getCause() instanceof TransientPropertyValueException tex) {
            return buildResponseEntity(new ApiError(CONFLICT, tex.getPropertyName() + " must be valid", ex));
        }
        return buildResponseEntity(new ApiError(INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleRuntimeException(AccessDeniedException ex) {
        return buildResponseEntity(new ApiError(FORBIDDEN, ex.getMessage(), ex));
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<Object> handleBaseException(BaseException ex) {
        return buildResponseEntity(new ApiError(ex.getHttpStatus(), ex.getMessage(), ex));
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}
