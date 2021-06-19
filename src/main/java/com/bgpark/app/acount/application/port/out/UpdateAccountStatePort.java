package com.bgpark.app.acount.application.port.out;

import com.bgpark.app.acount.domain.Account;

public interface UpdateAccountStatePort {

    void updateActivities(Account account);
}
