package com.example.RuneBotApi.RbExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoSuchInventoryItemException extends RuntimeException
{
    public NoSuchInventoryItemException(String cause)
    {
        super(cause);
        log.error(cause);
    }
}
