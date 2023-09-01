package com.example.RuneBotApi.RbExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoWalkablePathException extends RuntimeException
{
    public NoWalkablePathException(String cause)
    {
        super(cause);
        log.error(cause);
    }
}
