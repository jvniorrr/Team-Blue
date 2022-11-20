package com.callservice.repository;

// import java.util.Date;
// import java.sql.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.callservice.entity.Agent;

@Repository
@Transactional(readOnly = true)
public interface EmployeeRepository extends JpaRepository<Agent, Integer>
{
    @Query("select max(a.storeId) from Agent a")
    public Integer findMaxId();

    @Query("select a.id from Agent a where a.id = ?1")
    public Long IdIfExists(Long id);

    @Query("select a.storeId from Agent a where a.id = ?1")
    public Integer mapStore(Long id);


    // JUNIOR
    @Transactional(readOnly = true)
    @Query("select a from Agent a where a.idString = ?1")
    public Agent findAgent(String id);

    @Async
    @Query("select a from Agent a where a.idString = ?1")
    public CompletableFuture<Agent> findAgentAsync(String id);

    @Query("select a from Agent a where a.status = ?1")
    public List<Agent> findAllFilter(String param);

    @Modifying
    @Transactional
    @Query("update Agent a set a.status = ?1, a.name =?2  where a.idString =?3")
    public int updateAgent(String status, String name, String idString);
    

    @Async
    @Modifying
    @Transactional
    @Query("update Agent a set a.status = ?1, a.name =?2  where a.idString =?3")
    public CompletableFuture<Integer> updateAgentAsync(String status, String name, String idString);

    // @Modifying
    // // @Query("insert into agent (created, id, id_string, name, status, updatedts, store_id) values (?1, ?2, ?3, ?4, ?5, ?6, ?7)")
    // @Query("insert into Agent values(?1, ?2, ?3, ?4, ?5, ?6, ?7)")
    // public int saveAgent(Date created, Long id, String id_string, String name, String status, Date updated, int store_id);
}
