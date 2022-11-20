package com.callservice.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * Rest API Controller - For sending updates for new agents.
 */
@RestController
@RequestMapping("/api/v1")
public class AgentRestController {
    Logger logger = LoggerFactory.getLogger(AgentRestController.class);

    @Autowired
    private RuntimeProcess service;

    @Autowired
    private AgentService agentService;

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
     * Method to filter out employees based off of a specific filter string. API CALL
     * 
     * @param filter
     * @return
     */
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> filterAgents(
            @RequestParam(name = "status", required = false) String filter) {
        logger.debug("API Invoked: filterAgents()");
        List<AgentEntity> entities;
        Map<String, Object> ret = new HashMap<>();
        if (filter != null && validFilter(filter) == true) {
            entities = agentService.filterEntities(filter);
        } else {
            entities = agentService.getEntities();
        }
        ret.put("agents", entities);
        ret.put("response", String.valueOf(HttpStatus.OK.value()));

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    /**
     * Server Event Initializer, creates new Emitters when a new request is sent on our frontend.
     * 
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
     * 
     * @param employee
     * @return
     */
    @PostMapping(value = "/update")
    public ResponseEntity<Map<String, String>> saveOrUpdateEntity(
            @RequestBody AgentEntity employee) {
        logger.debug("API Invoked: saveOrUpdateEntity()");
        Map<String, String> ret = new HashMap<>();
        ResponseEntity<Map<String, String>> response;

        // parse the incoming body request assure proper fields
        for (SseEmitter emitter : emitters) {
            try {
                // send an event for each emmitter that is open
                emitter.send(SseEmitter.event().name("updateAgent").data(employee));
            } catch (IOException e) {
                emitters.remove(emitter); // handler for io exception
            } catch (Exception e) {
                // handle exception
                e.printStackTrace();
            }
        }

        try {
            String tmp = agentService.saveEntity(employee);
            ret.put("message", tmp);
            ret.put("response", String.valueOf(HttpStatus.OK.value()));
            response = new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ret.put("message", e.getMessage());
            ret.put("response", String.valueOf(HttpStatus.BAD_REQUEST.value()));
            response = new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
        }

        return response;

    }



    /** Method to verify user inputted a valid filter param */
    private Boolean validFilter(String filter) {
        return (filter.equalsIgnoreCase("available") || filter.equalsIgnoreCase("busy")
                || filter.equalsIgnoreCase("logged-out") || filter.equalsIgnoreCase("preview")
                || filter.equalsIgnoreCase("after"));
    }

}
