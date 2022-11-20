package com.callservice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


/**
 * Class Object to hold Agents information
 * - Stores Name, ID, and a status. Time param will be set when setting values in Database. 
 */
@Entity
public class Agent {
        // Fields
        @Id
        @Column(name = "storeId")
        private Integer storeId;    //Id for referencing in DB

        private String name, status, idString;
        // @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(nullable = true)
        private Long id;

        // timestamps
        private Date created = new Date(), updatedTS = null;


        // Constructors
        public Agent() {
        }
    
        public Agent(String name, Long id,  String status, String idString) {
            this.name = name;
            this.id = id;
            this.status = status;
            this.idString = idString;
        }
        
    
        // Getters
        public String getName() { return this.name; }
        public Long getId() { return this.id; }
        public String getStatus() { return this.status; }
        public Integer getStore() { return this.storeId; }
        public String getIdString() { return this.idString; }
        public Date getUpdatedTS() { return this.updatedTS; }
        public Date getCreated() { return this.created; }
    
        // Setters
        public void setName(String name) {
            this.name = name;
            setUpdatedTS(new Date());
        }
        
        public void setStatus(String status) {
            this.status = status;
            setUpdatedTS(new Date());
        }
        public void setId(Long id) { this.id = id; }
        public void setStore(Integer storeId) { this.storeId = storeId; }
        public void setIdString(String idString) { this.idString = idString; }
        private void setUpdatedTS(Date updatedTS) { this.updatedTS = updatedTS; }
        public void setCreated(Date created) { this.created = created; }

        // TO STRING
        @Override
        public String toString() {
            return "Agent [id=" + id + ", name=" + name + ", status=" + status + ", idString="+ idString + "]";
        }

        public String toJson() {
            return "{\"storeId\": " + this.storeId + ", \"name\": \"" + this.name + "\", \"id\": " + this.id + ", \"status\": \"" + this.status + "\", \"idString\":\"" + this.idString + "\"}";
        }
}
