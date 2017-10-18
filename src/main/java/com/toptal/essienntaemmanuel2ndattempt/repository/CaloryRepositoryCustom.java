package com.toptal.essienntaemmanuel2ndattempt.repository;

import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import java.util.List;

/**
 * @author bodmas
 */
public interface CaloryRepositoryCustom {

    List<Calory> findByCustomQuery(String email, String query);
    List<Calory> findByCustomQuery(String email, String query, int page, int size);
    List<Calory> findByCustomQuery(String query);
    List<Calory> findByCustomQuery(String query, int page, int size);
}
