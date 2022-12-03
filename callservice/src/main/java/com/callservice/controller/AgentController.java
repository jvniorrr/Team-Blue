package com.callservice.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.callservice.auth.Authenticate;
import com.callservice.entity.AgentEntity;
import com.callservice.service.AgentService;
import com.callservice.utils.StatisticsHelper;
import com.callservice.utils.ValidatorHelper;



/**
 * Controller to handle rendering web pages using Thymeleaf; essentially though pure html css and js.
 */
@Controller
public class AgentController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private Environment env;

    @Autowired
    private AgentService entityService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AgentController() {}

    //only allow get method in case of address bar url invocation and post method so front can securely send data
    //any other methods should not be allowed to this route so server will automatically return error page in such case
    @RequestMapping(value = {"/", "/home"}, method = {RequestMethod.GET, RequestMethod.POST})
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

        
        if (filter != null && ValidatorHelper.validFilter(filter)) {
            agents = entityService.filterEntities(filter);
        } else {
            agents = entityService.getEntities();
        }
        StatisticsHelper entityInformation = new StatisticsHelper(agents);


        model.addAttribute("agents", agents);
        model.addAttribute("available", entityInformation.getAllAvailable());
        model.addAttribute("busy", entityInformation.getAllBusy());
        model.addAttribute("preview", entityInformation.getAllPreview());
        model.addAttribute("after", entityInformation.getAllAfter());
        model.addAttribute("loggedout", entityInformation.getAllLoggedOut());

        logger.info("Page has agents " + agents.size() + " agents");
        logger.info("Returning index page");
        return "index";
    }

    @RequestMapping(value = "/settings", method = {RequestMethod.GET})
    public String settingsPage(Model model) {
        List<AgentEntity> agents = entityService.getEntities();
        StatisticsHelper entityInformation = new StatisticsHelper(agents);
        
        model.addAttribute("agents", agents);
        model.addAttribute("available", entityInformation.getAllAvailable());
        model.addAttribute("busy", entityInformation.getAllBusy());
        model.addAttribute("preview", entityInformation.getAllPreview());
        model.addAttribute("after", entityInformation.getAllAfter());
        model.addAttribute("loggedout", entityInformation.getAllLoggedOut());

        return "settings";
    }


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // if (status != null) {
        //     int statusCode = Integer.parseInt(status.toString());

            
        // }

        return "404";
    }
}