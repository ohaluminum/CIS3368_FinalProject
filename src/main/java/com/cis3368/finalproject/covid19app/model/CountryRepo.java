package com.cis3368.finalproject.covid19app.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CountryRepo extends CrudRepository<Country, String> {

    //Customize function to find by country name
    Optional<Country> findByCountryName(String countryName);


}
