package com.bgpark.app.acount.application.port.in;

import com.bgpark.app.acount.domain.Account.AccountId;
import com.bgpark.app.acount.domain.Money;

public interface GetAccountBalanceQuery {

    Money getAccountBalance(AccountId accountId);
}
