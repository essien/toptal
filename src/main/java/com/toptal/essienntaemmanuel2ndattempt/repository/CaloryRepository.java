package com.toptal.essienntaemmanuel2ndattempt.repository;

import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author bodmas
 */
@Repository
public interface CaloryRepository extends JpaRepository<Calory, Long>, CaloryRepositoryCustom {

    Optional<Calory> findCaloryById(Long caloryId);

    List<Calory> findAllByAccountEmail(String email);

    List<Calory> findAllByAccountEmail(String email, Pageable pageable);
}
