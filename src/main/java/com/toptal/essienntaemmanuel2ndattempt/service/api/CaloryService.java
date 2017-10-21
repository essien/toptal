package com.toptal.essienntaemmanuel2ndattempt.service.api;

import com.toptal.essienntaemmanuel2ndattempt.domain.Calory;
import com.toptal.essienntaemmanuel2ndattempt.exception.NoSuchAccountException;
import java.util.List;
import java.util.Optional;

/**
 * @author bodmas
 */
public interface CaloryService {

    /**
     * Finds all calories burned by the user with the given email, sorted by id.
     * @param email the user's email address
     * @return all calories burned by the given user
     * @throws NoSuchAccountException if there's no account associated with the given email address
     */
    List<Calory> findAll(String email) throws NoSuchAccountException;

    /**
     * Finds all calories burned by the user with the given email, sorted by id.
     * @param email the user's email address
     * @param offset the number of calory records to skip
     * @param size the number of calory records to return
     * @return the calory records that match the search criteria
     * @throws NoSuchAccountException  if there's no account associated with the given email address
     */
    List<Calory> findAll(String email, int offset, int size) throws NoSuchAccountException;

    /**
     * Finds all calories burned by the user with the given email, sorted by id.
     * @param email the user's email address
     * @param query a query string that is used to filter the results
     * @return the calory records that match the query
     * @throws NoSuchAccountException if there's no account associated with the given email address
     */
    List<Calory> findAll(String email, String query) throws NoSuchAccountException;

    /**
     * Finds all calories burned by the user with the given email, sorted by id.
     * @param email the user's email address
     * @param query a query string that is used to filter the results
     * @param offset teh number of calory records to skip
     * @param size the number of calory records to return
     * @return the calory records that match the query, offset and size criterias
     * @throws NoSuchAccountException if there's no account associated with the given email address
     */
    List<Calory> findAll(String email, String query, int offset, int size) throws NoSuchAccountException;

    /**
     * @param caloryId the calory Id to search against
     * @return the optional calory identified by the given {@code caloryId}
     */
    Optional<Calory> findById(Long caloryId);

    /**
     * Saves the calory.
     * @param email the user's email address
     * @param calory the calory to save
     * @return the newly saved calory
     * @throws NoSuchAccountException if there's no account associated with the given email address 
     */
    Calory save(String email, Calory calory) throws NoSuchAccountException;
}
