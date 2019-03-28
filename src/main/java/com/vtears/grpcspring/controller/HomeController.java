package com.vtears.grpcspring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = {"/home"})
public class HomeController {

    @RequestMapping(path = {"/hello"}, method = {RequestMethod.GET})
    public String sayHello(@PathParam(value = "name") String name) {
        return "Hello " + name;
    }
}
