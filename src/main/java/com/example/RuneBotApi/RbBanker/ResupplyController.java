package com.example.RuneBotApi.RbBanker;

public class ResupplyController extends RbBankController {
    public boolean eventLoop()
    {
        timeout = 1;

        return true;
    }
}
