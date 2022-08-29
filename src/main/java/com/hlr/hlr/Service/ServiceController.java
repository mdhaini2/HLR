package com.hlr.hlr.Service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.rmi.server.ServerCloneException;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    ServiceServices serviceServices;

    @PostMapping(value = "/addService")
    public Object addService(@RequestBody Services services){
        return serviceServices.addService(services);
    }

    @DeleteMapping(value = "/deleteService")
    public Object deleteService(@RequestParam int serviceID){
        return serviceServices.deleteService(serviceID);
    }

    @GetMapping(value = "/getAllServices")
    public Object getAllServices() throws ServerCloneException {
        return serviceServices.getAllServices();
    }

}
