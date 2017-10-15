package com.toptal.essienntaemmanuel2ndattempt.repository;

import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author bodmas
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);
}
