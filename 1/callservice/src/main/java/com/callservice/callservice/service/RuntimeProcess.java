package com.callservice.callservice.service;

import java.util.List;

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
}
