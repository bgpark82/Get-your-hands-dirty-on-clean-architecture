package com.bgpark.app.account.domain;

import lombok.NonNull;
import lombok.Value;

import java.math.BigInteger;

@Value
public class Money {

    public static Money ZERO = Money.of(0L);

    @NonNull
    private final BigInteger amount;

    public static Money of(Long amount) {
        return new Money(BigInteger.valueOf(amount));
    }

    public static Money add(Money a, Money b) {
        return new Money(a.amount.add(b.amount));
    }

    public static Money subtract(Money a, Money b) {
        return new Money(a.amount.subtract(b.getAmount()));
    }

    public Money negate() {
        return new Money(this.amount.negate());
    }

    public boolean isPositiveOrZero() {
        return this.amount.compareTo(BigInteger.ZERO) >= 0;
    }

    public boolean isGreaterThan(Money money) {
        return this.amount.compareTo(money.amount) >= 1;
    }

    public Money minus(Money money){
        return new Money(this.amount.subtract(money.amount));
    }

    public Money plus(Money money){
        return new Money(this.amount.add(money.amount));
    }
}
