package ch.zhaw.parkship.errorHandling;

import io.swagger.v3.oas.annotations.Hidden;
import org.hibernate.TransientPropertyValueException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler({ConversionFailedException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ResponseMessage> handleConversion(RuntimeException ex) {
        return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<ResponseMessage> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(new ResponseMessage(ex.getReason()), ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Hidden
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            if (!errors.containsKey(fieldName))
                errors.put(fieldName, new ArrayList<>());

            errors.get(fieldName).add(errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseMessage> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ResponseMessage> handleRuntimeException(InvalidDataAccessApiUsageException ex) {
        if (ex.getCause().getCause() instanceof TransientPropertyValueException tex) {
            return new ResponseEntity<>(new ResponseMessage(tex.getPropertyName() + " must be valid"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ResponseMessage> handleRuntimeException(AccessDeniedException ex) {
        return new ResponseEntity<>(new ResponseMessage(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    // Strings don't get serialized in ResponseEntity body so with this record it gets serialized
    private record ResponseMessage(String message) {
    }
}
