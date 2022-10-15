package com.callservice.callservice.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.callservice.callservice.Agent.Agent;

@Repository
public interface EmployeeDatabase extends JpaRepository<Agent, Integer>
{
    @Query("select max(a.storeId) from Agent a")
    public Integer findMaxId();

    @Query("select a.id from Agent a where a.id = ?1")
    public Long IdIfExists(Long id);

    @Query("select a.storeId from Agent a where a.id = ?1")
    public Integer mapStore(Long id);
}
