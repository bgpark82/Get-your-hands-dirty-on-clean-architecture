package com.bgpark.app.acount.domain;

import com.bgpark.app.acount.domain.Account.AccountId;
import org.junit.jupiter.api.Test;

import static com.bgpark.app.common.AccountTestData.defaultAccount;
import static com.bgpark.app.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void withdrawalSucceeds() {
        AccountId accountId = new AccountId(1L);
        Account account = defaultAccount()
                .withAccountId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(999L)).build(),
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(1L)).build()))
                .build();

        boolean isSuccess = account.withdraw(Money.of(555L), new AccountId(99L));

        assertThat(isSuccess).isTrue();
        assertThat(account.getActivityWindow().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
    }
}