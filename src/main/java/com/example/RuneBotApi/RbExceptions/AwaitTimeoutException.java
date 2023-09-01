package com.example.RuneBotApi.RbExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwaitTimeoutException extends RuntimeException
{
    public AwaitTimeoutException(String when)
    {
        super(when);
        log.error(when);
    }
}
