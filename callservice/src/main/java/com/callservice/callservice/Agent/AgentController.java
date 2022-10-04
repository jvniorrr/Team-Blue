package com.callservice.callservice.Agent;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

    @RequestMapping("/supervisor")
    public String people() {
        return "supervisorPage";
    }

    @GetMapping("/agentsPage")
    public String agents() {
        return "agents";
    }

    @GetMapping("/ahmedTest")
    public String testF() {
        return "ahmedAgents";
    }


    /**
     * ???  /  -> Retrieves STOMP messaging and returns an Agent.
     */
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Agent socketReturn(String msg) throws Exception {
        System.out.println("Hello world test");

        // retrieve the input as either a JSON file or retrieve input and then return a JSON obj for easier accessiblity frontend
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(msg, JsonObject.class);
        String agentName = jsonObject.get("name").getAsString();

        return new Agent(agentName, 1L, "active");
    }
}
