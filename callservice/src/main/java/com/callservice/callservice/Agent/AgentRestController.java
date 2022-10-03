package com.callservice.callservice.Agent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
public class AgentRestController {

    // set up emitter to transmit calls
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @RequestMapping("/agents")
    public SseEmitter agents() {
        SseEmitter sseEmitter = new SseEmitter((long) (60000 * 5));


        emitters.add(sseEmitter);

        // define callback
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));


        return sseEmitter;
    }


    @PostMapping(value = "/update")
    public void updateAgent(@RequestBody Map<String, String> info) {

        // parse the incoming body request assure proper fields
        String userID = info.get("id");
        String userStatus = info.get("status");
        // String userStatus = info.get("time");
        System.out.println(info);
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("spring").data(info));
            } catch (IOException e) {
                System.out.println("IO Exception occured");
            } catch (Exception e) {
                // TODO: Handle IOException for broken pipes
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}

