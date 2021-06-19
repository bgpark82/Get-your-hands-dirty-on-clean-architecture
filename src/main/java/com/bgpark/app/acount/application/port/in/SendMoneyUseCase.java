package com.bgpark.app.acount.application.port.in;

public interface SendMoneyUseCase {

    boolean sendMoney (SendMoneyCommand command);
}
