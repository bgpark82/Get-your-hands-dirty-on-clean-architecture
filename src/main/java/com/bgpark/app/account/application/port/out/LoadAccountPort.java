package com.bgpark.app.account.application.port.out;

import com.bgpark.app.account.domain.Account;
import com.bgpark.app.account.domain.Account.AccountId;

import java.time.LocalDateTime;

public interface LoadAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
