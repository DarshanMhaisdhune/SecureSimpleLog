package com.company.journal_app.service;

import com.company.journal_app.repository.UserRepository;
import com.company.journal_app.entity.User;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository ;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepository.save(user);
        }
        catch ( Exception e){
            logger.error("User not saved!, error occurred for {} :", user.getUserName(), e);
        }

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
