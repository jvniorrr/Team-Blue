package com.callservice.service;

import java.util.List;
import java.util.UUID;


import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.callservice.entity.Agent;
import com.callservice.repository.EmployeeRepository;


// @Transactional
@Service
public class RuntimeProcess 
{
    Logger logger = LoggerFactory.getLogger(RuntimeProcess.class);

    @Autowired
    private EmployeeRepository database;

    @Transactional
    public String createEmployee(Agent employee) {
        try
        {
            if (database.findMaxId() == null) {
                employee.setStore((Integer) 1);
                database.save(employee);
                return "employee saved";
            }
            else if (database.IdIfExists(employee.getId()) != null)
            {
                return "employee exists by id";
            }
            else 
            {

                employee.setStore(database.findMaxId() + 1);
                database.save(employee);
                return "Employee saved";
            }

        }
        catch (Exception e) {
            throw e;
        }
    }

    public List<Agent> readEmployees() {
        return database.findAll();
    }

    @Transactional
    public String updateEmployee(Agent employee) {   
        employee.setStore(database.mapStore(employee.getId()));

        if (database.existsById(employee.getStore())) {
            try {
                Agent update = database.findById(employee.getStore()).get();
                update.setStatus(employee.getStatus());
                update.setName(employee.getName());

                database.save(update);
                return "employee record updated";
            }

            catch (Exception e) {
                throw e;
            }
        }
        else {
            return "employee does not exist in database by id";
        }
    }

    @Transactional
    public String deleteEmployee(Agent employee) {
        employee.setStore(database.mapStore(employee.getId()));

        if (database.existsById(employee.getStore())) {
            try {
                database.delete(employee);
                return "employee record deleted";
            }
            catch (Exception e) {
                throw e;
            }
        }
        else {
            return "employee record failed to delete";
        }
    }


    // @Transactional(propagation=Propagation.REQUIRES_NEW)
    @Transactional
    public String updateAgent(Agent employee) {
        try {
            Agent agent = database.findAgent(employee.getIdString());
            logger.info(employee.toString());
            // Agent agent = agentExists(employee);
            String st; 
            if (agent != null ) {
                // UPDATING AGENT
                employee = setData(agent, employee);
                // employee = database.save(employee);
                int updates = database.updateAgent(employee.getStatus(), employee.getName(), employee.getIdString());
                logger.info("Updated cells: " + updates);
                st = "employee record updated";
                // System.out.println(employee);
                System.out.println("RunTimeProcess. \"updateAgent: " +  employee.toString());
                return st;

            } else {
                // create new agent
                // employee.setStore(database.findMaxId() == null ? (Integer) 1 : database.findMaxId() + 1);
                Integer max = database.findMaxId();
                if (max == null) {
                    employee.setStore((Integer) 1);
                    employee.setId(1L);
                } else {
                    int newMax = max + 1;
                    employee.setStore(newMax);
                    employee.setId(Long.valueOf(newMax));
                }
                // employee.setId(database.findMaxId() == null ? 1L : Long.valueOf(database.findMaxId() + 1));
                st = "employee created";

            }
            System.out.println("RunTimeProcess \"updateAgent: " +  employee.toJson());
            // int saved = database.saveAgent(employee.getCreated(), employee.getId(), employee.getIdString(), employee.getName(), employee.getStatus(), employee.getUpdatedTS(), employee.getStore());
            database.save(employee);
            return st;            

        } 
        catch (Exception e) {
            // TODO: handle exception
            logger.info("ERROR SAVING");
            logger.error(e.toString());
            return "ERROR";
            // throw e;
        }
    }

    @Transactional 
    public String deleteAgent(Agent employee) {
        // Agent agent = database.findAgent(employee.getIdString());
        Agent agent = agentExists(employee);

        // user exists
        if (agent != null) {
            try {
                database.delete(agent);
                return "employee record deleted";
            } 

            catch (Exception e) {
                throw e;
            }
        } else {
            return "employee record not found";
        }
    }

    // @Transactional(readOnly = true)
    public List<Agent> filterAll(String filter) {
        return database.findAllFilter(filter);
    }


    // UTILITIES
    @Transactional(readOnly = true)
    private Agent agentExists(Agent employee) {
        Agent agent = database.findAgent(employee.getIdString());
        // logger.info("Agent Found: " + (agent != null ? agent.toString() : "NULL"));
        return agent;
    }

    // @Transactional(readOnly = true)
    // @Transactional
    private Agent setData(Agent employee, Agent newEmployee) {
        if (employee.getId() == null) {
            employee.setId(database.findMaxId() == null ? 0L : Long.valueOf(database.findMaxId() + 1));
        }

        if (employee.getIdString() == null) {
            employee.setIdString(UUID.randomUUID().toString());
        }

        if (employee.getStatus() == null) {
            // default
            employee.setStatus("available");
        } else if (!(employee.getStatus().equalsIgnoreCase(newEmployee.getStatus()))) {
            employee.setStatus(newEmployee.getStatus());
        }

        // check if name changed
        if (!(employee.getName().equalsIgnoreCase(newEmployee.getName()))) {
            employee.setName(newEmployee.getName());
        }

        // database.save(employee);      
        return employee;
    }
}
