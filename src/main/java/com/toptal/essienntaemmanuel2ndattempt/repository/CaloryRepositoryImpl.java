package com.toptal.essienntaemmanuel2ndattempt.repository;

import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bodmas
 */
public class CaloryRepositoryImpl implements CaloryRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(CaloryRepositoryImpl.class);

    private final EntityManager em;

    public CaloryRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Calory> findByCustomQuery(String email, String query) {
        return em
                .createQuery("SELECT c from Calory c WHERE c.account.email = :email AND " + query + " ORDER BY c.id", Calory.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public List<Calory> findByCustomQuery(String email, String query, int page, int size) {
        return em
                .createQuery("SELECT c from Calory c WHERE c.account.email = :email AND " + query + " ORDER BY c.id", Calory.class)
                .setParameter("email", email)
                .setFirstResult(page)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Calory> findByCustomQuery(String query) {
        return em
                .createQuery("SELECT c from Calory c WHERE " + query + " ORDER BY c.id", Calory.class)
                .getResultList();
    }

    @Override
    public List<Calory> findByCustomQuery(String query, int page, int size) {
        return em
                .createQuery("SELECT c from Calory c WHERE " + query + " ORDER BY c.id", Calory.class)
                .setFirstResult(page)
                .setMaxResults(size)
                .getResultList();
    }
}
