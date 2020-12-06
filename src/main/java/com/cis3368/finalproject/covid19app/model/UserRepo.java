package com.cis3368.finalproject.covid19app.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, String> {

    //Customize function to find by username
    Optional<User> findByUsername(String username);

}