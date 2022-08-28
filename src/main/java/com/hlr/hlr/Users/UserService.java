package com.hlr.hlr.Users;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hlr.hlr.Exceptions.CredentialsNotValidException;
import com.hlr.hlr.Exceptions.PhoneNumberInvalidException;
import com.hlr.hlr.Exceptions.UsersNotFoundException;
import com.hlr.hlr.Security.JwtUtil;
import com.hlr.hlr.Utils.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

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

    private long currentTimeStamp = System.currentTimeMillis();

    public Object registerUser(Users user) throws NumberParseException, PhoneNumberInvalidException {
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

        user.setAddress(updateUser.getAddress());
        user.setFullName(updateUser.getFullName());
        user.setUpdatedDate(currentTimeStamp);
        userRepository.save(user);

        Response response = new Response("User profile updated Successfully", user);
        return response;
    }
    public Users getUserFromToken() {

        String token = jwtTokenUtil.getToken(request);
        String phoneNumber = jwtTokenUtil.extractUsername(token);
        Users user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }
}
