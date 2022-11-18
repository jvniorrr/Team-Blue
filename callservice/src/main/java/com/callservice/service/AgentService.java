package com.callservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.callservice.entity.AgentEntity;
import com.callservice.repository.EntityRepository;


@Service
public class AgentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    // REPOSITORY using JPA
    @Autowired
    private EntityRepository database;


    public List<AgentEntity> getEntities() {
        return database.findAll();
    }

    public List<AgentEntity> filterEntities(String filter) {
        return database.findAllFilter(filter);
    }

    // Method to save single entity
    @Transactional
    public String saveEntity(AgentEntity entity) {
        AgentEntity agent;

        agent = database.findAgent(entity.getId());
        String retVal = "";

        if (agent != null) {
            agent = database.save(agent);
            logger.info("Updated entity: " + agent.getId());
            retVal = "Updated entity";
        } else {
            try {
                // CompletableFuture<Integer> future = database.saveOrUpdate(new java.util.Date(), entity.getId(), entity.getName(), entity.getStatus(), new java.util.Date());
                // CompletableFuture<Integer> future = database.saveOrUpdate(entity.getId(), entity.getName(), entity.getStatus());
                agent = database.save(entity);
                // int tmp = future.get(1, TimeUnit.SECONDS);
                // logger.info("Values updated: {}", tmp);

                logger.info("Created entity: " + agent);
                retVal = "Created entity";

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return retVal;
    }

    // Async method to save single entity
    @Async
    @Transactional
    public CompletableFuture<AgentEntity> entityUpdate(AgentEntity entity) {
        long start = System.currentTimeMillis();
        AgentEntity agent;
        logger.info("Incoming Entity is: {}", entity.toString());
        try {
            CompletableFuture<AgentEntity> future = database.findAgentAsync(entity.getId());
            agent = future.get(3, TimeUnit.SECONDS);
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
            try {
                // CompletableFuture<Integer> future = database.saveOrUpdate(new java.util.Date(), entity.getId(), entity.getName(), entity.getStatus(), new java.util.Date());
                // CompletableFuture<Integer> future = database.saveOrUpdate(entity.getId(), entity.getName(), entity.getStatus());
                // int tmp = future.get(1, TimeUnit.SECONDS);
                // logger.info("Values updated: {}", tmp);
                agent = database.save(entity);
                logger.info("Created entity: " + agent);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        database.flush();
        long end = System.currentTimeMillis();
        logger.info("Transaction time: {}ms", (end - start));

        // return future obj
        return CompletableFuture.completedFuture(agent);

    }

    @Async
    @Transactional
    public CompletableFuture<List<AgentEntity>> saveUsers(List<AgentEntity> entities) {
        AgentEntity agent;
        List<AgentEntity> result = new ArrayList<>();
        long start = System.currentTimeMillis();
        // iterate through each assuring not in db
        for (int i=0; i<entities.size(); i++) {
            try {
                agent = database.findAgent(entities.get(i).getId());

                if (agent != null) {
                    agent.setName(entities.get(i).getName());
                    agent.setStatus(entities.get(i).getStatus());
                    agent = database.saveAndFlush(agent);
                    logger.info("Updated entity: {}", agent.getId());
                } else {
                    agent = database.saveAndFlush(entities.get(i));
                    logger.info("Created entity: {}", agent.getId());
                }
                result.add(agent);
                // database.flush(); // prevents some warning for now; will see in production
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                logger.error("Error updating/creating entity.");
            }
        }
        long end = System.currentTimeMillis();
        logger.info("Saving/Updating users time: {}ms", (end - start));

        return CompletableFuture.completedFuture(result);
    }


}

