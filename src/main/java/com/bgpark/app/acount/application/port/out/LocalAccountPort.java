package com.bgpark.app.acount.application.port.out;

import com.bgpark.app.acount.domain.Account;
import com.bgpark.app.acount.domain.Account.AccountId;

import java.time.LocalDateTime;

public interface LocalAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime now);
}
