package com.gdu.wacdo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WebServiceException extends RuntimeException {
    public WebServiceException(Throwable status) {
        super("[REQUEST ERROR] ", status);
    }
}
