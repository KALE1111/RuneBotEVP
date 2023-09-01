package com.example.RuneBotApi.RbExceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * usually just want to make configs such that this isn't an issue, but that isn't always possible
 * i.e. a bank pin must be stored as a string, not an int, since leading zeros get dropped
 */
@Slf4j
public class InvalidConfigException extends RuntimeException {
    public InvalidConfigException(String cause)
    {
        super(cause);
        log.error(cause);
    }
}
