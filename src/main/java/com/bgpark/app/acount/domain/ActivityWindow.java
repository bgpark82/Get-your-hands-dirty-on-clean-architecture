package com.bgpark.app.acount.domain;

import com.bgpark.app.acount.domain.Account.AccountId;

import java.util.List;

public class ActivityWindow {

    private List<Activity> activities;

    /**
     * 창구에서 발생한 입금 내역 - 출금 내역
     */
    public Money calculateBalance(AccountId accountId) {
        Money depositBalance = activities.stream()
                .filter(a -> a.getTargetAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        Money withdrawalBalance = activities.stream()
                .filter(a -> a.getSourceAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        return Money.add(depositBalance, withdrawalBalance.negate());
    }

    public void addActivity(Activity withdrawal) {
        this.activities.add(withdrawal);
    }
}
