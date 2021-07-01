package com.bgpark.app.acount.adapter.out.persistence;

import com.bgpark.app.account.adapter.out.persistence.AccountMapper;
import com.bgpark.app.account.adapter.out.persistence.AccountPersistenceAdapter;
import com.bgpark.app.account.adapter.out.persistence.ActivityJpaEntity;
import com.bgpark.app.account.adapter.out.persistence.ActivityRepository;
import com.bgpark.app.account.domain.Account;
import com.bgpark.app.account.domain.Account.AccountId;
import com.bgpark.app.account.domain.ActivityWindow;
import com.bgpark.app.account.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static com.bgpark.app.common.AccountTestData.defaultAccount;
import static com.bgpark.app.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {

    @Autowired
    private AccountPersistenceAdapter adapterUnderTest;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    void loadsAccount() {
        Account account = adapterUnderTest.loadAccount(new AccountId(1L), LocalDateTime.of(2018, 8, 10, 0, 0));

        assertThat(account.getActivityWindow().getActivities()).hasSize(2);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(500L));
    }

    @Test
    void updatesActivities() {
        Account account = defaultAccount()
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withId(null)
                                .withMoney(Money.of(1L)).build()))
                .build();

        adapterUnderTest.updateActivities(account);

        assertThat(activityRepository.count()).isEqualTo(1);

        ActivityJpaEntity savedActivity = activityRepository.findAll().get(0);
        assertThat(savedActivity.getAmount()).isEqualTo(1L);
    }

}