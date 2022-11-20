package com.callservice.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Class Object to hold Agents information - Stores Name, ID, and a status. Time param will be set
 * when setting values in Database.
 */
@Entity
@Table(name = "AGENT_TBL")
public class AgentEntity {
    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storeId", unique = true)
    private Integer storeId; // Id for referencing in DB

    private String name, status;
    // custom column name
    @Column(unique = true)
    private String id;
    // timestamps
    private Date created = new Date(), updatedTS = null;


    // Constructors
    public AgentEntity() {}

    public AgentEntity(String name, String status, String id) {
        this.name = name;
        this.status = status;
        this.id = id;
    }


    // Getters
    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public Integer getStore() {
        return this.storeId;
    }

    public String getId() {
        return this.id;
    }

    public Date getUpdatedTS() {
        return this.updatedTS;
    }

    public Date getCreated() {
        return this.created;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
        setUpdatedTS(new Date());
    }

    public void setStatus(String status) {
        this.status = status;
        setUpdatedTS(new Date());
    }

    public void setStore(Integer storeId) {
        this.storeId = storeId;
    }

    public void setId(String id) {
        this.id = id;
    }

    private void setUpdatedTS(Date updatedTS) {
        this.updatedTS = updatedTS;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    // TO STRING
    @Override
    public String toString() {
        return "Agent [id=" + this.id + ", name=" + this.name + ", status=" + this.status + "]";
    }

    public String toJson() {
        return "{\"storeId\": " + this.storeId + ", \"name\": \"" + this.name + "\", \"id\": "
                + this.id + ", \"status\": \"" + this.status + "\"}";
    }

}
