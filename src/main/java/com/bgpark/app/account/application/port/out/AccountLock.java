package com.bgpark.app.account.application.port.out;

import com.bgpark.app.account.domain.Account;

public interface AccountLock {

    void lockAccount(Account.AccountId accountId);

    void releaseAccount(Account.AccountId accountId);
}
