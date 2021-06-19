package com.bgpark.app.acount.application.service;

import com.bgpark.app.acount.application.port.in.SendMoneyCommand;
import com.bgpark.app.acount.application.port.in.SendMoneyUseCase;
import com.bgpark.app.acount.application.port.out.AccountLock;
import com.bgpark.app.acount.application.port.out.LoadAccountPort;
import com.bgpark.app.acount.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        // TODO: validate 비즈니스 룰
        // TODO: manipulate 모델 상태
        // TODO: return 결과

        /**
         * UseCase Validation
         * 잘모르겠으면 UseCase에서 Validation
         */
        checkThreshold(command);
        return false;
    }

    private void checkThreshold(SendMoneyCommand command) {
        if(command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
            throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
        }
    }
}
