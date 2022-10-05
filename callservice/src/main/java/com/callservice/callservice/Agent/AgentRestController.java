package com.callservice.callservice.Agent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


/**
 * Rest API Controller 
 * - For sending updates for new agents. 
 */
@RestController
public class AgentRestController {

    // set up emitter to transmit calls
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @RequestMapping("/agents")
    public SseEmitter agents() {
        SseEmitter sseEmitter = new SseEmitter((long) (60000 * 1)); // add a 1 minute timeout


        emitters.add(sseEmitter);

        // define callback
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));


        return sseEmitter;
    }


    @PostMapping(value = "/update")
    public Map<String, String> updateAgent(@RequestBody Map<String, String> info) {
        Map<String, String> ret = new HashMap<>();

        // parse the incoming body request assure proper fields
        String userID = info.get("id");
        String userStatus = info.get("status");

        for (SseEmitter emitter : emitters) {
            try {
                // can return a msg upon successfull call
                emitter.send(SseEmitter.event().name("updateAgent").data(info));
                // set return attibutes / keys
                ret.put("message", "Successfullly processed");
                ret.put("response", "200");
                ret.put("success", "true");
                
            } catch (IOException e) {
                // TODO: Handle IOException for broken pipes correctly to have a reconnection

                // set return attibutes / keys
                ret.put("message", "IO Exception");
                ret.put("response", "500");
                ret.put("success", "false");
                System.out.println("IO Exception occured");
                
            } catch (Exception e) {
                // set return attibutes / keys
                ret.put("message", "Server Error");
                ret.put("response", "500");
                ret.put("success", "false");
                // handle exception
                e.printStackTrace();

            }
        }
        return ret;
    }
}

