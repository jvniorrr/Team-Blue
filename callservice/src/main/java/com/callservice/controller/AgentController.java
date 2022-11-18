package com.callservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.callservice.entity.AgentEntity;
import com.callservice.service.AgentService;
import com.callservice.service.RuntimeProcess;



/**
 * Controller to handle rendering web pages using Thymeleaf; essentially though pure html css and js.
 */
@Controller
public class AgentController {

    Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private RuntimeProcess service;

    @Autowired
    private AgentService entityService;

    public AgentController() {
    }
    
    /**
     * GET  /  -> show the index page.
     */

    @GetMapping({"/", "/home"})
    public String home(Model model, @RequestParam(name = "status", required = false) String filter) {

        List<AgentEntity> agents;
        filter = filter != null ? (filter.equalsIgnoreCase("loggedout") ? "logged-out" : filter) : null; 

        // TODO: Sort by date created
        if (filter != null && validFilter(filter)) {
            agents = entityService.filterEntities(filter);
        } else {
            agents = entityService.getEntities();
        }

        model.addAttribute("agents", agents);

        logger.info("Page has agents " + agents.size() + " agents");
        logger.info("Returning index page");
        return "home";
    }

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
}