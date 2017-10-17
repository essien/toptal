package com.toptal.essienntaemmanuel2ndattempt.repository;

import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author bodmas
 */
@Repository
public interface CaloryRepository extends JpaRepository<Calory, Long> {

    Optional<Calory> findCaloryById(Long caloryId);
}
