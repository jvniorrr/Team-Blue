package com.callservice.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.callservice.Agent.Agent;
import com.callservice.database.EmployeeDatabase;

/**
 * CLASS TO TEST FUNCTIONALITY OF ENEITY MANAGEMENT; ATTEMPT TO DEBUG MULTI THREADING LOCKING DB connection.
 */
@Repository
@Transactional
public class AgentServiceRepo {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EmployeeDatabase database;

    
    public Agent getByIdString(String entity) {
        Agent agent = null;
        Query query = entityManager.createNativeQuery("select * from Agent a where a.idString = :idString", Agent.class);
        query.setParameter("idString", entity);
        try {
            agent = (Agent) query.getSingleResult();
        } catch (NoResultException e) {
            logger.info("No entity found.");
            agent = null;
        } catch (Exception e) {
            logger.error("Error while querying the db" + e);
        } finally {
            try {
                logger.info("unwrapping entity manager");
                query.unwrap(Agent.class);
                logger.info("unwrapping done");
            } catch (Exception e) {
            }
        }
        return agent;

    }

    @Transactional
    public Agent save(Agent entity) {
        Query q1 = entityManager.createQuery("PRAGMA journal_mode=WAL");
        try {
           int updates = q1.executeUpdate();
           logger.info("Pragma Response: " + updates);
        } catch (Exception e) {
            // TODO: handle exception
        }
        // String sql_statement = "select max(a.storeId) from Agent a";
        // Query query = entityManager.createQuery("select max(a.storeId) from Agent a");

        Integer max;
        try {
            // entityManager.
            // Object tmp = query.getSingleResult();
            // // logger.info("Temp result " + tmp.toString());
            // max = (Integer) tmp;
            // if (max == null) {
            //     entity.setStore((Integer) 1);
            //     entity.setId(1L);
            // } else {
            //     int newMax = max + 1;
            //     entity.setStore(newMax);
            //     entity.setId(Long.valueOf(newMax));
            // }

            logger.info("helloo world saving");
            logger.info(entity.toString());
            logger.info(entity.toJson());
            entityManager.persist(entity);
            // if (entityManager.isOpen()) {
            //     logger.info("Hello world");
            //     // logger.info(entityManager.getLockMode(entity).toString());
            //     System.out.println(entityManager.getEntityManagerFactory().isOpen());
            //     entityManager.persist(entity);
            //     logger.info(entityManager.toString());
            // } else {
            //     entityManager.persist(entity);
            // }

        } catch (NoResultException e) {
            logger.info("No entities currently stored");
        } catch (Exception e) {
            logger.error("Error saving user, unknown error: " + e.getMessage());
            logger.error(e.toString());
        } finally {
            try {
                logger.info("unwrapping entity manager");
                // query.unwrap(Agent.class);
                entityManager.close();
                logger.info("unwrapping done");
            } catch (Exception e) {
            }
        }

        
        return entity;
    }

    @Transactional
    public Agent update(Agent entity) {
        entityManager.merge(entity);
        return entity;
    }
}
