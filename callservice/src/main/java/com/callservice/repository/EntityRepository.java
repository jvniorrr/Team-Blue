package com.callservice.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.callservice.entity.AgentEntity;


@Repository
public interface EntityRepository extends JpaRepository<AgentEntity, Integer> {

    @Query("select a from AgentEntity a where a.id = ?1")
    public AgentEntity findAgent(String id);

    @Async
    @Query("select a from AgentEntity a where a.id = ?1")
    public CompletableFuture<AgentEntity> findAgentAsync(String id);

    @Query("select a from AgentEntity a where a.status = ?1")
    public List<AgentEntity> findAllFilter(String filter);

    @Async
    @Query("select a from AgentEntity a where a.status = ?1")
    public CompletableFuture<List<AgentEntity>> findAllFilterAsync(String filter);

    /** MISC; MAYBE WILL USE */

    // @Async
    // @Transactional(timeout = 10)
    // @Override
    // <S extends AgentEntity> S save(S s);


    @Async
    @Transactional(timeout = 10)
    @Modifying
    @Query(value = "insert into AGENT_TBL(created, id, name, status, updatedTS) values(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    public CompletableFuture<Integer> saveOrUpdate(java.util.Date created, String id, String name, String status, java.util.Date updatedTS);

    
}
