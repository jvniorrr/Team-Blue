package Agent;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AgentController {

    public AgentController() {
    }
    
    /**
     * GET  /  -> show the index page.
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }


    /**
     * ???  /  -> Retrieves STOMP messaging and returns an Agent.
     */
    @MessageMapping("/hello")
    @SendTo("/topic/service")
    public Agent socketReturn(String msg) throws Exception {
        System.out.println("Hello world test");

        // retrieve the input as either a JSON file or retrieve input and then return a JSON obj for easier accessiblity frontend
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(msg, JsonObject.class);
        String agentName = jsonObject.get("name").getAsString();

        return new Agent(agentName, 1L, "active");
    }
}
