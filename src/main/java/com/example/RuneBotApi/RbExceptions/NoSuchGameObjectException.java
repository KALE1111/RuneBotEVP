package com.example.RuneBotApi.RbExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSuchGameObjectException extends RuntimeException
{
    public NoSuchGameObjectException(String cause)
    {
        super(cause);
        log.error(cause);
    }
}
