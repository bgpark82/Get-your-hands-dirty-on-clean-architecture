package com.bgpark.app.acount.domain;

import com.bgpark.app.acount.domain.Account.AccountId;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActivityWindow {

    private List<Activity> activities;

    public ActivityWindow(@NonNull List<Activity> activities) {
        this.activities = activities;
    }

    public ActivityWindow(@NonNull Activity... activities) {
        this.activities = new ArrayList<>(Arrays.asList(activities))
        ;
    }

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

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(this.activities);
    }

    public void addActivity(Activity withdrawal) {
        this.activities.add(withdrawal);
    }
}
