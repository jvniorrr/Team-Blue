package com.callservice.callservice.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.callservice.callservice.Agent.Agent;
import com.callservice.callservice.database.EmployeeDatabase;


@Service
public class RuntimeProcess 
{
    @Autowired
    private EmployeeDatabase database;

    @Transactional
    public String createEmployee(Agent employee)
    {
        try
        {
            if (database.findMaxId() == null)
            {
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
        catch (Exception e)
        {
            throw e;
        }
    }

    public List<Agent> readEmployees()
    {
        return database.findAll();
    }

    @Transactional
    public String updateEmployee(Agent employee)
    {   
        employee.setStore(database.mapStore(employee.getId()));

        if (database.existsById(employee.getStore()))
        {
            try
            {
                Agent update = database.findById(employee.getStore()).get();
                update.setStatus(employee.getStatus());
                update.setName(employee.getName());

                database.save(update);
                return "employee record updated";
            }
            catch (Exception e)
            {
                throw e;
            }
        }
        else
        {
            return "employee does not exist in database by id";
        }
    }

    @Transactional
    public String deleteEmployee(Agent employee)
    {
        employee.setStore(database.mapStore(employee.getId()));

        if (database.existsById(employee.getStore()))
        {
            try
            {
                database.delete(employee);
                return "employee record deleted";
            }
            catch (Exception e)
            {
                throw e;
            }
        }
        else
        {
            return "employee record failed to delete";
        }
    }


    
    @Transactional
    public String updateAgent(Agent employee)
    {
        try {
            // Agent agent = database.findAgent(employee.getIdString());
            Agent agent = agentExists(employee);

            if (agent != null ) 
            {
                // updating agent
                setData(agent, employee);

                // database.save(agent);
                return "employee record updated";
            } else 
            {
                // create new agent
                employee.setStore(database.findMaxId() == null ? (Integer) 1 : database.findMaxId() + 1);
                employee.setId(database.findMaxId() == null ? 1L : Long.valueOf(database.findMaxId() + 1));
                database.save(employee);
                return "employee created";
            }
        } 
        catch (Exception e) 
        {
            // TODO: handle exception
            throw e;
        }
    }

    @Transactional 
    public String deleteAgent(Agent employee)
    {
        // Agent agent = database.findAgent(employee.getIdString());
        Agent agent = agentExists(employee);

        // user exists
        if (agent != null) {
            try {
                database.delete(agent);
                return "employee record deleted";
            } 
            catch (Exception e) 
            {
                throw e;
            }
        } else 
        {
            return "employee record not found";
        }
    }

    public List<Agent> filterAll(String filter)
    {
        return database.findAllFilter(filter);
    }


    // UTILITIES
    private Agent agentExists(Agent employee) 
    {
        Agent agent = database.findAgent(employee.getIdString());
        return (agent != null ? agent : null);
    }

    private void setData(Agent employee, Agent newEmployee) 
    {
        if (employee.getId() == null)
        {
            employee.setId(database.findMaxId() == null ? 0L : Long.valueOf(database.findMaxId() + 1));
        }

        if (employee.getIdString() == null)
        {
            employee.setIdString(UUID.randomUUID().toString());
        }

        if (employee.getStatus() == null) 
        {
            // default
            employee.setStatus("available");
        } 
        else if (!(employee.getStatus().equalsIgnoreCase(newEmployee.getStatus())))
        {
            employee.setStatus(newEmployee.getStatus());
        }

        // check if name changed
        if (!(employee.getName().equalsIgnoreCase(newEmployee.getName())))
        {
            employee.setName(newEmployee.getName());
        }

        database.save(employee);        
    }
}
