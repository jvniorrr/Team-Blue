package com.callservice.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.callservice.entity.AgentEntity;


@Repository
public interface EntityRepository extends JpaRepository<AgentEntity, Integer> {
    
    @Nullable
    @Query("select max(a.storeId) from AgentEntity a")
    public CompletableFuture<Integer> findMaxId();

    @Async
    @Nullable
    @Query("select max(a.storeId) from AgentEntity a")
    public CompletableFuture<Integer> findMaxIdAsync();

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
    @Async
    @Modifying
    @Transactional
    @Query("update AgentEntity a set a.status = ?1, a.name =?2  where a.id =?3")
    public CompletableFuture<Integer> updateAgentAsync(String status, String name, String id);

    
}
