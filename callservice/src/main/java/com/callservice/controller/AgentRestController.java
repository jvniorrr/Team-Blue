package com.callservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.callservice.entity.Agent;
import com.callservice.entity.AgentEntity;
import com.callservice.service.AgentService;
import com.callservice.service.RuntimeProcess;


/**
 * Rest API Controller 
 * - For sending updates for new agents. 
 */
@RestController
public class AgentRestController {

    Logger logger = LoggerFactory.getLogger(AgentRestController.class);

    @Autowired
    private RuntimeProcess service;

    @Autowired
    private AgentService agentService;

    private List<AgentEntity> allEntities = new ArrayList<>();

    // set up emitter to transmit calls
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @RequestMapping(value = "/gate", method = RequestMethod.POST)
    public String createEmployee(@RequestBody Agent employee) {
        return service.createEmployee(employee);
    }

    @RequestMapping(value = "/gate", method = RequestMethod.GET)
    public List<Agent> readEmployees() {
        return service.readEmployees();
    }

    @RequestMapping(value = "/gate", method = RequestMethod.PUT)
    public String updateEmployee(@RequestBody Agent employee) {
        return service.updateEmployee(employee);
    }

    @RequestMapping(value = "/gate", method = RequestMethod.DELETE)
    public String deleteEmployee(@RequestBody Agent employee) {
        return service.deleteEmployee(employee);
    }






    // JUNIOR CRUD
    /**
     * Method to update an agent given a UUID (String), Status (String), and Name (String)
     * @param employee
     * @return information regarding what process occured.
     */
    @RequestMapping(value = "/gatej", method = RequestMethod.GET)
    public Map<String, String> updateAgent(@RequestBody AgentEntity employee) {

        logger.info("Incoming API Request -> Update entity");

        Map<String, String> ret = new HashMap<>();
        // String update = agentService.entityRequest(employee);
        // System.out.println(update);
        String update = agentService.saveEntity(employee);

        ret.put("message", update);
        ret.put("status", "200");
        ret.put("success", (update.equalsIgnoreCase("updated entity") || update.equalsIgnoreCase("created entity") ? "true" : "false"));
        // return service.updateAgent(employee);
        return ret;
    }
    
    /**
     * Method to delete an agent given a UUID (String), Status (String), and Name (String)
     * @param employee
     * @return information regarding what process occured.
     */
    @RequestMapping(value = "/gatej", method = RequestMethod.DELETE)
    public Map<String, String> deleteAgent(@RequestBody Agent employee) {
        // System.out.println(employee);
        Map<String, String> ret = new HashMap<>();
        String update = service.deleteAgent(employee);

        ret.put("message", update);
        ret.put("status", "200");
        ret.put("success", (update.equalsIgnoreCase("employee record deleted") ? "true" : "false"));
        return ret;
        // return service.deleteAgent(employee);
    }
    // JUNIOR CRUD

    /**
     * Method to filter out employees based off of a specific filter string. API CALL
     * @param filter
     * @return
     */
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public List<Agent> filterAgents(@RequestParam(name = "status", required = false) String filter) {
        logger.info("Incoming API Request -> View all by filter");
        if (filter != null && validFilter(filter) == true) {
            return service.filterAll(filter);
        } else {
            return service.readEmployees();
        }
    }

    /** Method to verify user inputted a valid filter param */
    private Boolean validFilter(String filter) {
        if (filter.equalsIgnoreCase("available") ||
            filter.equalsIgnoreCase("busy") || 
            filter.equalsIgnoreCase("logged-out") || 
            filter.equalsIgnoreCase("preview") || 
            filter.equalsIgnoreCase("after") 
        ) {
            return true;
        } 
        else {
            return false;
        }
    }

    /**
     * Server Event Initializer, creates new Emitters when a new request is sent on our frontend.
     * @return
     */
    @RequestMapping("/agents")
    public SseEmitter agents() {
        SseEmitter sseEmitter = new SseEmitter((long) (60000 * 1)); // add a 1 minute timeout
        // SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // define callback
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitters.remove(sseEmitter));
        emitters.add(sseEmitter);

        // logger.info("New Emitter created");
        
        return sseEmitter;
    }

    /**
     * REST API call for client to send in entities and their status'
     * @param employee
     * @return
     */
    @PostMapping(value = "/update")
    public Map<String, String> sseUpdateAgent(@RequestBody com.callservice.entity.AgentEntity employee) {

        // logger.info("New Emitter created");
        Map<String, String> ret = new HashMap<>();

        // parse the incoming body request assure proper fields
        for (SseEmitter emitter : emitters) {
            try {
                // can return a msg upon successfull call
                emitter.send(SseEmitter.event().name("updateAgent").data(employee));
                // set return attibutes / keys
                ret.put("message", "Successfully processed");
                ret.put("response", "200");
                ret.put("success", "true");
                
            } catch (IOException e) {

                // set return attibutes / keys
                ret.put("message", "IO Exception");
                ret.put("response", "500");
                ret.put("success", "false");
                emitters.remove(emitter); // handler for io exception
                
            } catch (Exception e) {
                // set return attibutes / keys
                ret.put("message", "Server Error");
                ret.put("response", "500");
                ret.put("success", "false");
                // handle exception
                e.printStackTrace();

            }
        }

        allEntities.add(employee);
        if (allEntities.size() >= 100) {
            try {
                CompletableFuture<List<AgentEntity>> future = agentService.saveUsers(allEntities);
                List<AgentEntity> entities = future.get(2, TimeUnit.SECONDS);
                // logger.info("Successfully saved all users: {}", entities);
                logger.info("Successfully saved all users");
                allEntities.clear();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        return ret;
    }

}