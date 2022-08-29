package com.hlr.hlr.Service;

import com.hlr.hlr.Security.JwtUtil;
import com.hlr.hlr.UserSubscribeService.UserSubscribeService;
import com.hlr.hlr.Users.UserRepository;
import com.hlr.hlr.Users.Users;
import com.hlr.hlr.Utils.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.rmi.server.ServerCloneException;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ServiceServices {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;


    public Object addService(Services services) {
        log.info("addService: adding new service to DB");
        serviceRepository.save(services);
        Response response = new Response("New service added successfully", services);
        return response;
    }


    public Object deleteService(int serviceID) {
        Optional<Services> optimalServices = serviceRepository.findById(serviceID);
        if (optimalServices.isEmpty()) {
            log.error("deleteService: Service with service id: " + serviceID + " does not exists!");
            throw new BadCredentialsException("Service with service id: " + serviceID + " does not exists!");
        }
        Services service = optimalServices.get();

        serviceRepository.delete(service);
        log.info("deleteService: Service deleted from DB");

        Response response = new Response("Service deleted successfully", null);
        return response;
    }


    public Object getAllServices() throws ServerCloneException {
        List<Services> servicesList = serviceRepository.findAll();
        if (servicesList.isEmpty()) {
            log.error("getAllServices: No Services Found");
            throw new ServerCloneException("No services found");
        }
        log.info("getAllServices: List of services retrieved successfully");
        Response response = new Response("List of services retrieved successfully", servicesList);
        return response;
    }

//    public Object subscribeToService(int serviceID) {
//        Users user = getUserFromToken();
//
//        Optional<Services> service = serviceRepository.findById(serviceID);
//        if (service.isEmpty()) {
//            log.error("subscribeToService:Service with service id: " + serviceID + " does not exist");
//            throw new BadCredentialsException("Service with service id:" + serviceID + " does not exist");
//        }
//        Services services = service.get();
//
//        UserSubscribeService userSubscribeService = new UserSubscribeService("Active",)
//    }

    public Users getUserFromToken() {

        log.info("Get user from token");
        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }
}
