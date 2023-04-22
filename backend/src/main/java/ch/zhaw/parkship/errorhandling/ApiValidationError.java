package ch.zhaw.parkship.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.FieldError;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String attribute;
    private String message;

    ApiValidationError(FieldError error) {
        this.attribute = error.getField();
        this.message = error.getDefaultMessage();
    }
}

