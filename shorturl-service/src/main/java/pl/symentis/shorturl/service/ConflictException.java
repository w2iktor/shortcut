package pl.symentis.shorturl.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="duplicate URL shortcut")
public class ConflictException extends RuntimeException {

  private static final long serialVersionUID = -6928520333928798043L;

	public ConflictException(String message) {
        super(message);
    }
}
