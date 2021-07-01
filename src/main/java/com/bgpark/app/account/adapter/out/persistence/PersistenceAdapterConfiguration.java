package com.bgpark.app.account.adapter.out.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
public class PersistenceAdapterConfiguration {

    @Bean
    AccountPersistenceAdapter accountPersistenceAdapter(
            AccountRepository accountRepository,
            ActivityRepository activityRepository,
            AccountMapper accountMapper
    ) {
        return new AccountPersistenceAdapter(
                accountRepository,
                activityRepository,
                accountMapper);
    }

    @Bean
    AccountMapper accountMapper() {
        return new AccountMapper();
    }
}
