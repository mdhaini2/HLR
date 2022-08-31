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

    public Response registerUser(Users user) throws NumberParseException, PhoneNumberInvalidException, CredentialsNotValidException {
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

        if (user.getPassword() == null) {
            log.error("Password must not be null!");
            throw new CredentialsNotValidException("Password must not be null!");
        }
        log.info("Encrypting user password");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        user.setCreatedDate(currentTimeStamp);
        user.setUpdatedDate(currentTimeStamp);

        log.info("saving user to the database");
        userRepository.save(user);

        String responseMessage = "User registered successfully";
        Response response = new Response(responseMessage, user);
        return response;

    }

    public Response getAllUsers() throws UsersNotFoundException {
        log.info("Getting list of users");
        List<Users> usersList = userRepository.findAll();
        if (usersList.isEmpty()) {
            log.error("users list is empty");
            throw new UsersNotFoundException("No Users founds");
        }
        Response response = new Response("List of users retrieved successfully", usersList);
        return response;
    }

    public Response deleteUser(int id) throws CredentialsNotValidException {
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

        log.info("Checking if user want to change his lineType");
        if (lineType.getId() != user.getLineType().getId()) {
            if (user.getLineType().getId() == 1) {
                user.setBalance(0);
            }
            user.setLineType(lineType);
            log.info("updateUserProfile: changing line type for user and deleting all services subscribed to");
            if (!user.getUserSubscribeServiceSet().isEmpty()) {
                userSubscribeServiceRepository.deleteByUsersId(user.getId());
            }

        }

        log.info("updateUserProfile: saving user updates to Database");
        user.setUpdatedDate(currentTimeStamp);
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

    public Response loginUser(String inputPhoneNumber, String password) throws PhoneNumberInvalidException, NumberParseException, CredentialsNotValidException {
        // Phone number validator
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String userPhoneNumber = String.valueOf(inputPhoneNumber);
        Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber();
        number.setCountryCode(countryCode);
        number.setNationalNumber(Long.valueOf(userPhoneNumber));

        log.info("Checking if phone number is valid");
        if (!phoneNumberUtil.isPossibleNumber(number)) {
            log.error(inputPhoneNumber + " is not a valid phone number");
            throw new PhoneNumberInvalidException(inputPhoneNumber + " is not a valid phone number");
        }

        userPhoneNumber = String.valueOf(Long.valueOf(userPhoneNumber));
        Users user = userRepository.findByPhoneNumber(userPhoneNumber);
        log.info(user);

        // Password validator
        if (user == null) {
            log.error("User with phone number: " + userPhoneNumber + " does not exists!");
            throw new PhoneNumberInvalidException("User with phone number: " + userPhoneNumber + " does not exists!");
        }
        log.info("Checking if password is correct!");
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            log.error("Incorrect Password!");
            throw new CredentialsNotValidException("Incorrect Password!");
        }

        log.info("Generating new token for logged in user");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getPhoneNumber());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        Response response = new Response("User " + user.getFullName() + " logged in successfully", jwt);
        return response;
    }

    public Response checkBalance() {
        Users user = getUserFromToken();
        Set<UserSubscribeService> userSubscribeServiceSet = new HashSet<UserSubscribeService>();
        String services = "";
        log.info("adding active services to userSubscribeServiceSet");
        for (UserSubscribeService userSubscribeService : user.getUserSubscribeServiceSet()) {
            if (userSubscribeService.getIsActive()) {
                userSubscribeServiceSet.add(userSubscribeService);
                services += userSubscribeService.toString();
            }
        }

        UserBalance userBalance = new UserBalance(user.getBalance(), services);
        Response response = new Response("User balance retrieved successfully", userBalance);
        return response;
    }

    public Response addCredits(double credits) throws CredentialsNotValidException {
        Users user = getUserFromToken();
        if (user.getLineType().getLineType().equalsIgnoreCase("postpaid")) {
            log.error("User have a postpaid line type can't add credits");
            throw new CredentialsNotValidException("User have a postpaid line type can't add credits");
        }
        user.setBalance(user.getBalance() + credits);
        log.info("Saving users new credit to the DB");
        userRepository.save(user);

        Response response = new Response("Added " + credits + " to user.", user.getBalance());
        return response;
    }
}
