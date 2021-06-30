package com.bgpark.app.acount.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * TODO: 왜 private 이지??
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    /**
     * TODO: 왜 final이지??
     */
    private final AccountId id;

    /**
     * 현재 창구에서, 첫번째로 출입금내역 발생하기 이전의 잔고
     */
    @Getter private final Money baselineBalance;

    /**
     * 가장 최근에 발생한 출입금내역 창구
     */
    @Getter private final ActivityWindow activityWindow;

    public static Account withoutId( Money baselineBalance, ActivityWindow activityWindow) {
        return new Account(null, baselineBalance, activityWindow);
    }

    public static Account withId(AccountId accountId, Money baselineBalance, ActivityWindow activityWindow) {
        return new Account(accountId, baselineBalance, activityWindow);
    }

    /**
     * baseline 잔고와 창구에서 발생한 출입금 내역을 더한 해당 계좌의 전체 잔고
     */
    public Money calculateBalance() {
        return Money.add(this.baselineBalance, this.activityWindow.calculateBalance(this.id));
    }

    public Optional<AccountId> getId(){
        return Optional.ofNullable(this.id);
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {
        /**
         * Business Validation
         * 현재 잔고보다 많은 돈을 인출할 경우
         */
        if(!mayWithdraw(money)) {
            return false;
        }

        Activity withdrawal = new Activity(
                this.id,
                this.id,
                targetAccountId,
                LocalDateTime.now(),
                money);

        this.activityWindow.addActivity(withdrawal);
        return true;
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {
        Activity deposit = new Activity(
                this.id,
                sourceAccountId,
                this.id,
                LocalDateTime.now(),
                money);

        this.activityWindow.addActivity(deposit);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        return Money.add(
                this.calculateBalance(),
                money.negate())
                .isPositiveOrZero();
    }

    /**
     * TODO: 왜 여기있지??
     */
    @Value
    public static class AccountId {
        private Long value;
    }
}
