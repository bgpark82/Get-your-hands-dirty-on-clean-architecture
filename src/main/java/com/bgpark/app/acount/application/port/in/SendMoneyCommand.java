package com.bgpark.app.acount.application.port.in;

import com.bgpark.app.acount.domain.Account.AccountId;
import com.bgpark.app.acount.domain.Money;
import com.bgpark.app.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

    @NonNull
    private final AccountId sourceAccountId;

    @NonNull
    private final AccountId targetAccountId;

    @NonNull
    private final Money money;

    public SendMoneyCommand(
            AccountId sourceAccountId,
            AccountId targetAccountId,
            Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
        /**
         * Input Validation
         */
        this.validateSelf();
    }
}
