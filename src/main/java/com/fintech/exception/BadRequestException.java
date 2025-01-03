package com.fintech.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@AllArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid value passed")
@EqualsAndHashCode(callSuper = false)
public class BadRequestException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -167207076249088181L;

    public BadRequestException(String message) {
        super(message);
    }
}