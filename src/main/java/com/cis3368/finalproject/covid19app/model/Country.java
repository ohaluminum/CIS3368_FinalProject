package com.cis3368.finalproject.covid19app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "id")
    private final String id;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total_cases")
    private int totalCases;

    @Column(name = "total_deaths")
    private int totalDeaths;

    @Column(name = "new_cases")
    private int newCases;

    @Column(name = "new_deaths")
    private int newDeaths;

    @Column(name = "user_id")
    private String userID;

    //Default Constructor
    public Country()
    {
        id = UUID.randomUUID().toString();
        countryName = "N/A";
        date = LocalDate.now();
        totalCases = 0;
        totalDeaths = 0;
        newCases = 0;
        newDeaths = 0;
        userID = "N/A";
    }

    //Getter and Setter
    public String getID()
    {
        return id;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public int getTotalCases()
    {
        return totalCases;
    }

    public int getTotalDeaths()
    {
        return totalDeaths;
    }

    public int getNewCases()
    {
        return newCases;
    }

    public int getNewDeaths()
    {
        return newDeaths;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public void setTotalCases(int totalCases)
    {
        this.totalCases = totalCases;
    }

    public void setTotalDeaths(int totalDeaths)
    {
        this.totalDeaths = totalDeaths;
    }

    public void setNewCases(int newCases)
    {
        this.newCases = newCases;
    }

    public void setNewDeaths(int newDeaths)
    {
        this.newDeaths = newDeaths;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }
}
