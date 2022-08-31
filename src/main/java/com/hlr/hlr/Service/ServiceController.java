package com.hlr.hlr.Service;

import com.hlr.hlr.Exceptions.InsufficientAmountException;
import com.hlr.hlr.Exceptions.ServiceAlreadyExistsException;
import com.hlr.hlr.Exceptions.UserAlreadySubscribedToServiceException;
import com.hlr.hlr.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.rmi.server.ServerCloneException;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    @Autowired
    ServiceServices serviceServices;

    @PostMapping(value = "/addService")
    public Response addService(@RequestBody Services services) throws ServiceAlreadyExistsException {
        return serviceServices.addService(services);
    }

    @PostMapping(value = "/subscribeToService")
    public Response subscribeToService(@RequestParam int serviceID) throws UserAlreadySubscribedToServiceException, InsufficientAmountException {
        return serviceServices.subscribeToService(serviceID);
    }

    @DeleteMapping(value = "/deleteService")
    public Response deleteService(@RequestParam int serviceID) {
        return serviceServices.deleteService(serviceID);
    }

    @GetMapping(value = "/getAllServices")
    public Response getAllServices() throws ServerCloneException {
        return serviceServices.getAllServices();
    }

}
