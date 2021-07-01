package com.bgpark.app.account.application.service;

import com.bgpark.app.account.application.port.out.AccountLock;
import com.bgpark.app.account.domain.Account;
import org.springframework.stereotype.Component;

@Component
class NoOpAccountLock implements AccountLock {

    @Override
    public void lockAccount(Account.AccountId accountId) {
        // do nothing
    }

    @Override
    public void releaseAccount(Account.AccountId accountId) {
        // do nothing
    }

}
