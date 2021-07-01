package com.bgpark.app.account.application.port.in;

public interface SendMoneyUseCase {

    boolean sendMoney (SendMoneyCommand command);
}
