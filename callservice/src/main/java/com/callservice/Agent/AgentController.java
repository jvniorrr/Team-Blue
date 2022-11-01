package com.callservice.Agent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callservice.service.RuntimeProcess;



/**
 * Controller to handle rendering web pages using Thymeleaf; essentially though pure html css and js.
 */
@Controller
public class AgentController {

    @Autowired
    private RuntimeProcess service;

    public AgentController() {
    }
    
    /**
     * GET  /  -> show the index page.
     */
    // @RequestMapping("/")
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam(name = "status", required = false) String filter) {

        List<Agent> agents;
        // TODO: add SORT here so dots stay in relative position. Sort by date creation
        if (filter != null && validFilter(filter)) {
            agents = service.filterAll(filter);
        } 
        else {
            agents = service.readEmployees();
        }

        model.addAttribute("agents", agents);

        return "home";
    }


    @GetMapping("/ahmed")
    public String testF() {
        return "ahmedAgents";
    }




    private Boolean validFilter(String filter) {
        if (filter.equalsIgnoreCase("available") ||
                filter.equalsIgnoreCase("busy") ||
                filter.equalsIgnoreCase("logged-out") ||
                filter.equalsIgnoreCase("preview") ||
                filter.equalsIgnoreCase("after")) {
            return true;
        } else {
            return false;
        }
    }

}
