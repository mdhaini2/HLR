package com.hlr.hlr.Service;

import com.hlr.hlr.Exceptions.InsufficientAmountException;
import com.hlr.hlr.Exceptions.ServiceAlreadyExistsException;
import com.hlr.hlr.Exceptions.UserAlreadySubscribedToServiceException;
import com.hlr.hlr.Security.JwtUtil;
import com.hlr.hlr.UserSubscribeService.UserSubscribeService;
import com.hlr.hlr.UserSubscribeService.UserSubscribeServiceRepository;
import com.hlr.hlr.Users.UserRepository;
import com.hlr.hlr.Users.Users;
import com.hlr.hlr.Utils.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.rmi.server.ServerCloneException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    UserSubscribeServiceRepository userSubscribeServiceRepository;
    private long currentTimeStamp = System.currentTimeMillis();

    private static final DecimalFormat df = new DecimalFormat("0.00");


    public Response addService(Services services) throws ServiceAlreadyExistsException {
        log.info("addService: adding new service to DB");
        services.setUpdatedDate(currentTimeStamp);
        services.setCreatedDate(currentTimeStamp);
        Services services1 = serviceRepository.findByServiceName(services.getServiceName());
        if (services1 != null) {
            log.error("addService: " + services.getServiceName() + " already exists");
            throw new ServiceAlreadyExistsException("addService: " + services.getServiceName() + " already exists");
        }
        serviceRepository.save(services);
        Response response = new Response("New service added successfully", services);
        return response;
    }


    public Response deleteService(int serviceID) {
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


    public Response getAllServices() throws ServerCloneException {
        List<Services> servicesList = serviceRepository.findAll();
        if (servicesList.isEmpty()) {
            log.error("getAllServices: No Services Found");
            throw new ServerCloneException("No services found");
        }
        log.info("getAllServices: List of services retrieved successfully");
        Response response = new Response("List of services retrieved successfully", servicesList);
        return response;
    }

    public Response subscribeToService(int serviceID) throws UserAlreadySubscribedToServiceException, InsufficientAmountException {
        Users user = getUserFromToken();
        log.info("Getting service with service id: " + serviceID + " from the DB");
        Optional<Services> service = serviceRepository.findById(serviceID);
        if (service.isEmpty()) {
            log.error("subscribeToService: Service with service id: " + serviceID + " does not exist");
            throw new BadCredentialsException("Service with service id:" + serviceID + " does not exist");
        }

        Services services = service.get();
        List<UserSubscribeService> subscribeServiceList = (List<UserSubscribeService>) userSubscribeServiceRepository.findUserSubscribedService(user.getId(), services.getId());
        if (!subscribeServiceList.isEmpty()) {
            for (UserSubscribeService userSubscribeService : subscribeServiceList) {
                if (userSubscribeService.getIsActive()) {
                    log.error("subscribeToService: user already subscribed to service");
                    throw new UserAlreadySubscribedToServiceException("user already subscribed to service " + services.getServiceName());
                }
            }
        }

        if (user.getLineType().getLineType().equalsIgnoreCase("PrePaid")) {
            if (user.getBalance() < services.getPrice()) {
                log.error("User does not have enough balance please recharge Current balance: ");
                throw new InsufficientAmountException("User does not have enough balance please recharge Current balance: " + user.getBalance() + " USD");
            }
            log.info("subtracting from user's prepaid balance");
            user.setBalance(Double.parseDouble(df.format(user.getBalance() - services.getPrice())));
        } else {
            log.info("adding to user's postpaid balance");
            user.setBalance(Double.parseDouble(df.format(user.getBalance() + services.getPrice())));
        }
        log.info("Creating userSubscribeService data");
        UserSubscribeService userSubscribeService = new UserSubscribeService(true,
                services.getData(),
                currentTimeStamp,
                services,
                user);
        userSubscribeService.setUpdateDate(currentTimeStamp);
        log.info("adding new subscription service to the database for user:"+user.getFullName());
        userSubscribeServiceRepository.save(userSubscribeService);

        log.info("Getting the list of service user "+user.getFullName()+" subscribed to");
        Set<UserSubscribeService> userSubscribeServicesSet = user.getUserSubscribeServiceSet();
        if (userSubscribeServicesSet == null) {
            log.info("user "+user.getFullName()+" doesn't have any previous subscription. Creating new list");
            userSubscribeServicesSet = new HashSet<UserSubscribeService>();
            userSubscribeServicesSet.add(userSubscribeService);
        } else {
            log.info("added new service subscription to userSubscriptionSet");
            userSubscribeServicesSet.add(userSubscribeService);
        }
        log.info("updating subscription services list");
        user.setUserSubscribeServiceSet(userSubscribeServicesSet);
        userRepository.save(user);



        Set<UserSubscribeService> usersSubscribedServiceSet = services.getUserSubscribedService();
        if (usersSubscribedServiceSet == null) {
            usersSubscribedServiceSet = new HashSet<UserSubscribeService>();
            usersSubscribedServiceSet.add(userSubscribeService);
        } else {
            usersSubscribedServiceSet.add(userSubscribeService);
        }
        services.setUserSubscribedService(usersSubscribedServiceSet);
        serviceRepository.save(services);

        Response response = new Response("User subscribed successfully to service " + services.getServiceName(), userSubscribeService);
        return response;
    }

    public Users getUserFromToken() {

        log.info("Get user from token");
        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }
}
