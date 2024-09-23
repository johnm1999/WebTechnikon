package gr.codehub.webtechnikon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException(String message) {
        super(message);
    }
}
