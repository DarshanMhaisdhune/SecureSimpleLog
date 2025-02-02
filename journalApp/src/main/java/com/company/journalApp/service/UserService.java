package com.company.journalApp.service;

import com.company.journalApp.Repository.UserRepository;
import com.company.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository ;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll() ;
    }

    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName){
       return userRepository.findByUserName(userName);
    }

    public User updateUser(String userName, User user) {
        User userInDB = userRepository.findByUserName(userName);
        if (userInDB != null) {
            userInDB.setUserName(user.getUserName() != null && !user.getUserName().isEmpty() ? user.getUserName() : userInDB.getUserName());
            userInDB.setPassword(user.getPassword() != null && !user.getPassword().isEmpty() ? passwordEncoder.encode(user.getPassword()) : userInDB.getPassword());
            userRepository.save(userInDB);
            return userInDB;
        }
        return null;
    }

}
