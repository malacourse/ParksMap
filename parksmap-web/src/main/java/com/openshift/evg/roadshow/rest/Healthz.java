package com.openshift.evg.roadshow.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Healthz endpoint for liveness and readiness of the application
 *
 * Created by jmorales on 11/08/16.
 */
@RestController
@RequestMapping("/ws")
public class Healthz {

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/healthz", produces = "application/json")
    public String healthz() {
        return "OK";
    }
}
