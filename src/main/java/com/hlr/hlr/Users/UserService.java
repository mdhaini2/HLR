package com.hlr.hlr.Users;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hlr.hlr.Exceptions.CredentialsNotValidException;
import com.hlr.hlr.Exceptions.PhoneNumberInvalidException;
import com.hlr.hlr.Exceptions.UsersNotFoundException;
import com.hlr.hlr.Security.JwtUtil;
import com.hlr.hlr.Security.MyUserDetailsService;
import com.hlr.hlr.UserSubscribeService.UserSubscribeService;
import com.hlr.hlr.UserSubscribeService.UserSubscribeServiceRepository;
import com.hlr.hlr.LineType.LineType;
import com.hlr.hlr.Utils.Response;
import com.hlr.hlr.Utils.UserBalance;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Value(value = "${phone.country.code}")
    private int countryCode;

    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private UserSubscribeServiceRepository userSubscribeServiceRepository;
    private long currentTimeStamp = System.currentTimeMillis();

    public Object registerUser(Users user) throws NumberParseException, PhoneNumberInvalidException, CredentialsNotValidException {
        // Phone number validator
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String userPhoneNumber = String.valueOf(user.getPhoneNumber());
        Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber();

        number.setCountryCode(countryCode);
        char firstChar = userPhoneNumber.charAt(0);
        if (firstChar == '0') {
            number.setItalianLeadingZero(true);
            number.clearNumberOfLeadingZeros();
        }

        number.setNationalNumber(Long.valueOf(userPhoneNumber));
        log.info("User Phone Number:" + String.valueOf(Long.valueOf(userPhoneNumber)));

        userPhoneNumber = String.valueOf(Long.valueOf(userPhoneNumber));
        user.setPhoneNumber(userPhoneNumber);


        if (!phoneNumberUtil.isPossibleNumber(number)) {
            log.error(user.getPhoneNumber() + " is not a valid phone number");
            throw new PhoneNumberInvalidException(user.getPhoneNumber() + " is not a valid phone number");
        }

        Users existingUser = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if (existingUser != null) {
            log.error("User with phone number: " + user.getPhoneNumber() + " already exists!");
            throw new PhoneNumberInvalidException("User with phone number: " + user.getPhoneNumber() + " already exists!");
        }

        int lineTypeId = user.getLineType().getId();
        log.error("Line Type equals " + lineTypeId);
        if (lineTypeId != 0 && lineTypeId != 1) {
            log.error("Not valid line type " + user.getLineType());
            throw new CredentialsNotValidException(user.getLineType() + " is not a valid line type");

        }


        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        user.setCreatedDate(currentTimeStamp);
        user.setUpdatedDate(currentTimeStamp);

        log.info("saving user to the database");
        userRepository.save(user);

        String responseMessage = "User registered successfully";
        Response response = new Response(responseMessage, user);
        return response;

    }

    public Object getAllUsers() throws UsersNotFoundException {
        log.info("Getting list of users");
        List<Users> usersList = userRepository.findAll();
        if (usersList.isEmpty()) {
            log.error("users list is empty");
            throw new UsersNotFoundException("No Users founds");
        }
        Response response = new Response("List of users retrieved successfully", usersList);
        return response;
    }

    public Object deleteUser(int id) throws CredentialsNotValidException {
        Optional<Users> optimalUser = userRepository.findById(id);

        if (optimalUser.isEmpty()) {
            log.error("user not found");
            throw new CredentialsNotValidException("User with user id: " + id + " does not exists!");
        }
        log.info("user found");
        Users user = optimalUser.get();

        log.info("deleted user with id: " + id);
        userRepository.delete(user);

        Response response = new Response("user with id:" + id + " deleted successfully", null);
        return response;

    }

    public Response updateUserProfile(Users updateUser) {

        Users user = getUserFromToken();
        log.info("updateUserProfile: got user from token");

        user.setAddress(updateUser.getAddress());
        user.setFullName(updateUser.getFullName());
        LineType lineType = updateUser.getLineType();
        if (lineType.getId() != user.getLineType().getId()) {
            if(user.getLineType().getId()==1){
                user.setBalance(0);
            }
            user.setLineType(lineType);
            log.info("updateUserProfile: changing line type for user and deleting all services subscribed to");
            if (!user.getUserSubscribeServiceSet().isEmpty()) {
                userSubscribeServiceRepository.deleteByUsersId(user.getId());
            }

        }

        user.setUpdatedDate(currentTimeStamp);

        log.info("updateUserProfile: saving user updates to Database");
        userRepository.save(user);

        log.info("User updated successfully");
        Response response = new Response("User profile updated Successfully", user);
        return response;
    }

    public Users getUserFromToken() {

        log.info("Get user from token");
        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }

    public Object loginUser(String inputPhoneNumber, String password) throws PhoneNumberInvalidException, NumberParseException, CredentialsNotValidException {
        // Phone number validator
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String userPhoneNumber = String.valueOf(inputPhoneNumber);
        Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber();
        number.setCountryCode(countryCode);
        number.setNationalNumber(Long.valueOf(userPhoneNumber));

        log.info(phoneNumberUtil.isPossibleNumber(number));
        if (!phoneNumberUtil.isPossibleNumber(number)) {
            throw new PhoneNumberInvalidException(inputPhoneNumber + " is not a valid phone number");
        }

        userPhoneNumber = String.valueOf(Long.valueOf(userPhoneNumber));
        Users user = userRepository.findByPhoneNumber(userPhoneNumber);
        log.info(user);

        // Password validator
        if (user == null) {
            throw new PhoneNumberInvalidException("User with phone number: " + userPhoneNumber + " does not exists!");
        }

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new CredentialsNotValidException("Incorrect Password!");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getPhoneNumber());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        Response response = new Response("User " + user.getFullName() + " logged in successfully", jwt);
        return response;
    }

    public Object checkBalance() {
        Users user = getUserFromToken();
        Set<UserSubscribeService> userSubscribeServiceSet = new HashSet<UserSubscribeService>();
        String services = "";
        for (UserSubscribeService userSubscribeService : user.getUserSubscribeServiceSet()) {
            if (userSubscribeService.getIsActive()) {
                userSubscribeServiceSet.add(userSubscribeService);
                services += userSubscribeService.toString();
            }
        }

        UserBalance userBalance = new UserBalance(user.getBalance(), services);
        return userBalance;
    }

    public Object addCredits(double credits) throws CredentialsNotValidException {
        Users user = getUserFromToken();
        if (user.getLineType().getLineType().equalsIgnoreCase("postpaid")) {
            throw new CredentialsNotValidException("User have a postpaid line type can't add credits");
        }
        user.setBalance(user.getBalance() + credits);
        userRepository.save(user);
        Response response = new Response("Added " + credits + " to user.", user.getBalance());
        return response;
    }
}
