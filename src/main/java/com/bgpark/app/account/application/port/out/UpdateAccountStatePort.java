package com.bgpark.app.account.application.port.out;

import com.bgpark.app.account.domain.Account;

public interface UpdateAccountStatePort {

    void updateActivities(Account account);
}
