package com.bgpark.app.acount.application.port.out;

import com.bgpark.app.acount.domain.Account;

public interface AccountLock {

    void lockAccount(Account.AccountId accountId);

    void releaseAccount(Account.AccountId accountId);
}
