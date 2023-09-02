package com.example.RuneBotApi.RbExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSuchBankItemException extends RuntimeException {
    public NoSuchBankItemException(String cause)
    {
        super(cause);
        log.error(cause);
    }
}
