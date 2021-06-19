package com.bgpark.app.acount.application.service;

import com.bgpark.app.acount.application.port.in.GetAccountBalanceQuery;
import com.bgpark.app.acount.application.port.out.LocalAccountPort;
import com.bgpark.app.acount.domain.Account.AccountId;
import com.bgpark.app.acount.domain.Money;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceQuery {

    private final LocalAccountPort localAccountPort;

    @Override
    public Money getAccountBalance(AccountId accountId) {

        return localAccountPort.loadAccount(accountId, LocalDateTime.now())
                .calculateBalance();
    }
}
