package com.callservice.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.callservice.entity.AgentEntity;
import com.callservice.repository.EntityRepository;
import com.callservice.database.EmployeeDatabase;


@Service
public class AgentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // ENTITY MANAGER CLIENT
    @Autowired
    private AgentServiceRepo em;

    // REPOSITORY using JPA
    @Autowired
    private EntityRepository database;

    // @Transactional
    // public String entityRequest(Agent employee) {

    //     // check if the agent exists
    //     logger.info(employee.toString());
    //     String result = "";
    //     // CompletableFuture<Agent> future = database.findAgentAsync(employee.get());
    //     Agent entity = em.getByIdString(employee.get());
    //     logger.info("Check for entity: " + entity);

    //     try {
    //         Agent tmpEntity = null;
    //         if (entity != null) {
    //             // tmpEntity = em.update(entity);
    //             logger.info("Update soon");
    //         } else {
    //             tmpEntity = em.save(employee);
    //         }
    //         logger.info("Saved or updated entity: " + tmpEntity);
            
    //     } catch (Exception e) {
    //         logger.error("ERROR " + e);
    //     }



    //     // try {
    //     //     Agent agent = future.get();
    //     //     result = (agent != null ? agent.toString() : "");
    //     //     System.out.println(result);
    //     //     // Thread.sleep((long)(Math.random() * 1000));

    //     //     System.out.println(entityManager.isOpen());
    //     //     if (agent!= null) {
    //     //         // update the agents information
    //     //         result = (updateAgent(agent) ? "entity record updated" : "error updating entity");
    //     //     } else {
    //     //         result = (createAgent(employee) ? "entity record created" : "error creating entity");
    //     //     }
            
    //     // } catch (InterruptedException e) {
    //     //     e.printStackTrace();
    //     // } catch (ExecutionException e) {
    //     //     e.printStackTrace();
    //     // }

    //     return result;
    // }

    @Async
    @Transactional
    public CompletableFuture<AgentEntity> entityUpdate(AgentEntity entity) {
        long start = System.currentTimeMillis();
        AgentEntity agent;
        logger.info("Incoming Entity is: {}", entity.toString());
        try {
            CompletableFuture<AgentEntity> future = database.findAgentAsync(entity.getId());
            agent = future.get();
            // agent = database.findAgent(entity.getId());
            logger.info("Entity found: " + entity.getId());

        } catch (Exception e) {
            // FIXME: handle exception
            System.err.println(e.toString());
            logger.error(e.toString());
            logger.error("Uncaught exception finding entity;;; SORRY READING UP SOME DOCUMENTATION CURRENTLY");
            return null;
        }

        // TEST CASE
        // update the entities info
        if (agent != null) {
            agent = database.save(agent);
            logger.info("Updated entity: " + agent.getId());
        } else {
            agent = database.save(entity);
            logger.info("Created entity: " + agent.getId());
        }

        database.flush();
        long end = System.currentTimeMillis();
        logger.info("Transaction time {}", (end - start));

        // return future obj
        return CompletableFuture.completedFuture(agent);

    }


    // private boolean updateAgent(Agent agent) {
    //     CompletableFuture<Integer> updatedFuture = database.updateAgentAsync(agent.getStatus(), agent.getName(), agent.get());
    //     boolean result = false;

    //     try {
    //         Integer updated = updatedFuture.get();
    //         logger.info("Updated cells: " + updated);
    //         result = (updated == 1 ? true : false);
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     } catch (ExecutionException e) {
    //         e.printStackTrace();
    //     }

    //     return result;
    // }

}

