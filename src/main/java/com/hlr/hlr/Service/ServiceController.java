package com.hlr.hlr.Service;

import com.hlr.hlr.Exceptions.InsufficientAmountException;
import com.hlr.hlr.Exceptions.ServiceAlreadyExistsException;
import com.hlr.hlr.Exceptions.UserAlreadySubscribedToServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.rmi.server.ServerCloneException;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    ServiceServices serviceServices;

    @PostMapping(value = "/addService")
    public Object addService(@RequestBody Services services) throws ServiceAlreadyExistsException {
        return serviceServices.addService(services);
    }

    @PostMapping(value = "/subscribeToService")
    public Object subscribeToService(@RequestParam int serviceID) throws UserAlreadySubscribedToServiceException, InsufficientAmountException {
        return serviceServices.subscribeToService(serviceID);
    }

    @DeleteMapping(value = "/deleteService")
    public Object deleteService(@RequestParam int serviceID) {
        return serviceServices.deleteService(serviceID);
    }

    @GetMapping(value = "/getAllServices")
    public Object getAllServices() throws ServerCloneException {
        return serviceServices.getAllServices();
    }

}
