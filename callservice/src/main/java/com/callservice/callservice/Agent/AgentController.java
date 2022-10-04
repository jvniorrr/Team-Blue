package com.callservice.callservice.Agent;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



/**
 * Controller to handle rendering web pages using Thymeleaf; essentially though pure html css and js.
 */
@Controller
public class AgentController {

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

    // commented out for now, index holds this now
    // @GetMapping("/agentsPage")
    // public String agents() {
    //     return "agents";
    // }

    @GetMapping("/ahmed")
    public String testF() {
        return "ahmedAgents";
    }

}
