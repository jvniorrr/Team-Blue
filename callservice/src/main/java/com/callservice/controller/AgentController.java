package com.callservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.callservice.auth.Authenticate;
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
    private Environment env;

    @Autowired
    private RuntimeProcess service;

    @Autowired
    private AgentService entityService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AgentController() {}

    /**
     * GET / -> show the index page.
     */

    /*@GetMapping("/side")
    public String home(Model model,
            @RequestParam(name = "status", required = false) String filter) {

        List<AgentEntity> agents;
        filter = filter != null ? (filter.equalsIgnoreCase("loggedout") ? "logged-out" : filter)
                : null;

        if (filter != null && validFilter(filter)) {
            agents = entityService.filterEntities(filter);
        } else {
            agents = entityService.getEntities();
        }

        model.addAttribute("agents", agents);

        logger.info("Page has agents " + agents.size() + " agents");
        logger.info("Returning index page");
        return "home";
    }*/

    //only allow get method in case of address bar url invocation and post method so front can securely send data
    //any other methods should not be allowed to this route so server will automatically return error page in such case
    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public String hello(Model model,
            @RequestParam(name = "status", required = false) String filter,
            @RequestParam(name = "key", required = false) Optional<String> auth) 
    {

        Authenticate authResult = new Authenticate();
        boolean breaker = false;
                
        if (!auth.isPresent())
        {
            breaker = true;
        }
        else if (!encoder.matches(auth.get(), env.getProperty("api.key")))
        {
            authResult.setMessage("Denied");
            breaker = true;
        }

        if (breaker)
        {
            model.addAttribute("response", authResult);
            return "landing";
        }

        List<AgentEntity> agents;
        filter = filter != null ? (filter.equalsIgnoreCase("loggedout") ? "logged-out" : filter)
                : null;
        int loggedOut = 0, after = 0, busy = 0, preview = 0, available = 0;


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