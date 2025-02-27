package com.company.journal_app.controller;

import com.company.journal_app.entity.User;
import com.company.journal_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService ;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getAllUsers();
        if ( all != null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK) ;
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        userService.saveAdmin(user);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }
}
