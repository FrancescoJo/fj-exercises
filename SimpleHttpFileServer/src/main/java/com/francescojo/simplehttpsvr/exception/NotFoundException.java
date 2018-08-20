/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Oct - 2016
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
