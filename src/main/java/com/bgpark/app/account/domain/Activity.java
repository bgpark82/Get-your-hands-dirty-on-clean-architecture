package com.bgpark.app.account.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * {@link Account}에서의 송금 내역
 */
@Value
@RequiredArgsConstructor
public class Activity {

    @Getter
    private ActivityId id;

    @Getter
    @NonNull
    private final Account.AccountId ownerAccountId;

    /**
     * TODO: 무슨 차이지?
     * 같은 계좌인데, 입금할 때만 사용되는 필드
     */
    @Getter
    @NonNull
    private final Account.AccountId sourceAccountId;

    /**
     * TODO: 무슨 차이지?
     * 같은 계좌인데, 출금할 때만 사용되는 필드
     */
    @Getter
    @NonNull
    private final Account.AccountId targetAccountId;

    @Getter
    @NonNull
    private final LocalDateTime timestamp;

    @Getter
    @NonNull
    private final Money money;

    public Activity(
            @NonNull Account.AccountId ownerAccountId,
            @NonNull Account.AccountId sourceAccountId,
            @NonNull Account.AccountId targetAccountId,
            @NonNull LocalDateTime timestamp,
            @NonNull Money money) {
        this.id = null;
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.money = money;
    }

    @Value
    public static class ActivityId {
        private final Long value;
    }
}
