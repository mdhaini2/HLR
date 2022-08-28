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
    public Object registerUser(@RequestBody Users user) throws NumberParseException, PhoneNumberInvalidException {
        return userService.registerUser(user);
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


}
