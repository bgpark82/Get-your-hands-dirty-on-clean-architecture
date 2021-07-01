package com.bgpark.app.account.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {
}
