package com.company.journal_app.repository;

import com.company.journal_app.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUserName(String userName);
    void deleteByUserName(String userName);
}
