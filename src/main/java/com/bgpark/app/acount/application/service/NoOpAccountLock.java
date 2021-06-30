package com.bgpark.app.acount.application.service;

import com.bgpark.app.acount.application.port.out.AccountLock;
import org.springframework.stereotype.Component;

import static com.bgpark.app.acount.domain.Account.*;

@Component
class NoOpAccountLock implements AccountLock {

    @Override
    public void lockAccount(AccountId accountId) {
        // do nothing
    }

    @Override
    public void releaseAccount(AccountId accountId) {
        // do nothing
    }

}
