package com.hlr.hlr.Users;


import com.google.i18n.phonenumbers.NumberParseException;
import com.hlr.hlr.Exceptions.CredentialsNotValidException;
import com.hlr.hlr.Exceptions.PhoneNumberInvalidException;
import com.hlr.hlr.Exceptions.UsersNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/register")
    public Object registerUser(@RequestBody Users user) throws NumberParseException, PhoneNumberInvalidException, CredentialsNotValidException {
        return userService.registerUser(user);
    }
    @GetMapping(value = "/login")
    public Object loginUser(@RequestParam String phoneNumber, @RequestParam String password) throws PhoneNumberInvalidException, NumberParseException, CredentialsNotValidException {
        return userService.loginUser(phoneNumber,password);
    }
    @GetMapping(value = "/getAllUsers")
    public Object getAllUsers() throws UsersNotFoundException {
        return userService.getAllUsers();
    }
    @DeleteMapping(value = "/deleteUser")
    public Object deleteUser(@RequestParam int id) throws CredentialsNotValidException {
        return userService.deleteUser(id);
    }
    @PutMapping(value = "/updateUserProfile")
    public Object updateUserProfile(@RequestBody Users user){
        return  userService.updateUserProfile(user);
    }

    @GetMapping(value = "/checkBalance")
    public Object checkBalance(){
        return userService.checkBalance();
    }
    @PostMapping(value = "/addCredits")
    public Object addCredits(@RequestParam double credits) throws CredentialsNotValidException {
        return userService.addCredits(credits);
    }

}
