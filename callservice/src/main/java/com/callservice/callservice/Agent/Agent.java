package com.callservice.callservice.Agent;


/**
 * Class Object to hold Agents information
 * - Stores Name, ID, and a status. Time param will be set when setting values in Database. 
 */
public class Agent {
        // Fields
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
    
        // TO STRING
        @Override
        public String toString() {
            return "Agent [id=" + id + ", name=" + name + ", status=" + status + "]";
        }
        
}
