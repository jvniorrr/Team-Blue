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
 * Controller to handle rendering web pages using Thymeleaf; essentially though pure html css and
 * js.
 */
@Controller
public class AgentController {

    Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private RuntimeProcess service;

    @Autowired
    private AgentService entityService;

    public AgentController() {}

    /**
     * GET / -> show the index page.
     */

    @GetMapping({"/", "/home"})
    public String home(Model model,
            @RequestParam(name = "status", required = false) String filter) {

        List<AgentEntity> agents;
        filter = filter != null ? (filter.equalsIgnoreCase("loggedout") ? "logged-out" : filter)
                : null;

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

    @GetMapping("/index")
    public String hello(Model model,
            @RequestParam(name = "status", required = false) String filter) {

        List<AgentEntity> agents;
        filter = filter != null ? (filter.equalsIgnoreCase("loggedout") ? "logged-out" : filter)
                : null;
        int loggedOut = 0, after = 0, busy = 0, preview = 0, available = 0;


        // TODO: Sort by date created
        if (filter != null && validFilter(filter)) {
            agents = entityService.filterEntities(filter);
        } else {
            agents = entityService.getEntities();
        }

        for (AgentEntity entity : agents) {
            if (entity.getStatus().equalsIgnoreCase("available"))
                available++;
            if (entity.getStatus().equalsIgnoreCase("busy"))
                busy++;
            if (entity.getStatus().equalsIgnoreCase("after"))
                after++;
            if (entity.getStatus().equalsIgnoreCase("logged-out"))
                loggedOut++;
            if (entity.getStatus().equalsIgnoreCase("preview"))
                preview++;
        }

        model.addAttribute("agents", agents);
        model.addAttribute("available", available);
        model.addAttribute("busy", busy);
        model.addAttribute("preview", preview);
        model.addAttribute("after", after);
        model.addAttribute("loggedout", loggedOut);

        logger.info("Page has agents " + agents.size() + " agents");
        logger.info("Returning index page");
        return "indexHome";
    }

    private Boolean validFilter(String filter) {
        if (filter.equalsIgnoreCase("available") || filter.equalsIgnoreCase("busy")
                || filter.equalsIgnoreCase("logged-out") || filter.equalsIgnoreCase("preview")
                || filter.equalsIgnoreCase("after")) {
            return true;
        } else {
            return false;
        }
    }
}