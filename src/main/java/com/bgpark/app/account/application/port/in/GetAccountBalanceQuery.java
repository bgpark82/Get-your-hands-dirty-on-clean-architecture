package com.bgpark.app.account.application.port.in;

import com.bgpark.app.account.domain.Account.AccountId;
import com.bgpark.app.account.domain.Money;

public interface GetAccountBalanceQuery {

    Money getAccountBalance(AccountId accountId);
}
