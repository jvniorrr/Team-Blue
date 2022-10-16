package com.callservice.callservice.Agent;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Class Object to hold Agents information
 * - Stores Name, ID, and a status. Time param will be set when setting values in Database. 
 */
@Entity
public class Agent {
        // Fields
        @Id
        private Integer storeId;    //Id for referencing in DB

        private String name;
        private Long id;
        private String status;
    
        // Constructors
        public Agent() {
        }
    
        public Agent(String name, Long id,  String status) {
            this.name = name;
            this.id = id;
            this.status = status;
        }
    
    
        // Getters
        public String getName() {
            return name;
        }
    
        public Long getId() {
            return id;
        }
    
        public String getStatus() {
            return status;
        }

        public Integer getStore()
        {
            return storeId;
        }
    
        // Setters
        public void setName(String name) {
            this.name = name;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public void setStatus(String status) {
            this.status = status;
        }    
        
        public void setStore(Integer storeId)
        {
            this.storeId = storeId;
        }

        // TO STRING
        @Override
        public String toString() {
            return "Agent [id=" + id + ", name=" + name + ", status=" + status + "]";
        }

        public String toJson()
        {
            return "{\"storeId\": " + this.storeId + ", \"name\": \"" + this.name + "\", \"id\": " + this.id + ", \"status\": \"" + this.status + "\"}";
        }
        
}
