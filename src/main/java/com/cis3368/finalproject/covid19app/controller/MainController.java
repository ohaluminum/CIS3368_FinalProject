package com.cis3368.finalproject.covid19app.controller;

import com.cis3368.finalproject.covid19app.model.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    CountryRepo countryRepo;

    @Autowired
    UserRepo userRepo;

    boolean isAdmin;
    String currUser;

    //Log in as a user
    @RequestMapping(value = "/login")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password)
    {
        //Create model and view
        ModelAndView mv;

        //Create user instance

        //Check if the user already in the user table
        Optional<User> userRecord = userRepo.findByUsername(username);

        //The user record non-exists
        if (userRecord.isEmpty())
        {
            //Set view (index.jsp)
            mv = new ModelAndView("index");

            //Set model
            mv.addObject("loginNotification", "Invalid Username! Enter Again!");
        }

        //The user record exists
        else
        {
            //The password matched
            if (password.equals(userRecord.get().getPassword()))
            {
                //Set view (home.jsp)
                mv = new ModelAndView("home");

                //Set model
                mv.addObject("countryList", countryRepo.findAll());

                //Record admin info
                isAdmin = userRecord.get().getIsAdmin();
                currUser = username;

                if (isAdmin)
                {
                    mv.addObject("status", "Current User: " + currUser + " (Administrator)");
                }
                else
                {
                    mv.addObject("status", "Current User: " + currUser + " (User)");
                }
            }

            //The password unmatched
            else
            {
                //Set view (index.jsp)
                mv = new ModelAndView("index");

                //Set model
                mv.addObject("loginNotification", "Password Incorrect! Enter Again!");
            }
        }

        return mv;
    }

    //Signup Page
    @RequestMapping(value = "/sign")
    public ModelAndView sign()
    {
        //Set view (home.jsp)
        ModelAndView mv = new ModelAndView("signup");

        return mv;
    }

    //Sign up a new account
    @RequestMapping(value = "/signup")
    public ModelAndView signup(@RequestParam("username") String username,
                               @RequestParam("password1") String password1,
                               @RequestParam("password2") String password2,
                               @RequestParam("isAdmin") String is_Admin)
    {
        //Create model and view
        ModelAndView mv = new ModelAndView("signup");

        //Create user instance

        //Check if the user already in the user table
        Optional<User> userRecord = userRepo.findByUsername(username);

        //The user record exists: Prevent create new account using the existing username
        if (userRecord.isPresent())
        {
            //Set model
            mv.addObject("signupNotification", "Username Already Exists!");
        }

        //The user record non-exists: Create a new account
        else
        {
            //The passwords matched
            if (password1.equals(password2))
            {
                //Create a user instance to store passing information (UUID will be generated automatically)
                User userToSave = new User();

                //Set other attribute
                userToSave.setUsername(username);
                userToSave.setPassword(password1);
                userToSave.setIsAdmin(is_Admin.equals("true"));

                //Save the new user record to the user repository
                userRepo.save(userToSave);

                //Set model
                mv.addObject("signupNotification", "Sign Up Successfully!");
            }

            //The passwords unmatched
            else
            {
                //Set model
                mv.addObject("signupNotification", "Password Unmatched! Enter Again!");
            }
        }

        return mv;
    }

    //Homepage
    @RequestMapping(value = "/home")
    public ModelAndView homepage()
    {
        //Set view (home.jsp)
        ModelAndView mv = new ModelAndView("home");

        //Set model
        mv.addObject("countryList", countryRepo.findAll());

        if (isAdmin)
        {
            mv.addObject("status", "Current User: " + currUser + " (Administrator)");
        }
        else
        {
            mv.addObject("status", "Current User: " + currUser + " (User)");
        }

        return mv;
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ModelAndView get(@RequestParam("requestCountry") String requestCountry,
                            @RequestParam("requestDate") String requestDate)
    {
        //Set view (home.jsp)
        ModelAndView mv = new ModelAndView("home");

        String covidRecord = getRecordByCountryAndDate(requestDate);

        try
        {
            //Get the string from API URL
            JSONObject json = new JSONObject(covidRecord);

            //Get country: change space with hyphen
            String temp = json.getJSONObject("dates").getJSONObject(requestDate).getJSONObject("countries").getJSONObject(requestCountry).get("name").toString();
            String country = temp.replace(' ', '-');

            //Set object from API
            mv.addObject("country", country);
            mv.addObject("date", json.getJSONObject("dates").getJSONObject(requestDate).getJSONObject("countries").getJSONObject(requestCountry).get("date").toString());
            mv.addObject("totalCases", json.getJSONObject("dates").getJSONObject(requestDate).getJSONObject("countries").getJSONObject(requestCountry).get("today_confirmed").toString());
            mv.addObject("totalDeaths", json.getJSONObject("dates").getJSONObject(requestDate).getJSONObject("countries").getJSONObject(requestCountry).get("today_deaths").toString());
            mv.addObject("newCases", json.getJSONObject("dates").getJSONObject(requestDate).getJSONObject("countries").getJSONObject(requestCountry).get("today_new_confirmed").toString());
            mv.addObject("newDeaths", json.getJSONObject("dates").getJSONObject(requestDate).getJSONObject("countries").getJSONObject(requestCountry).get("today_new_deaths").toString());

            mv.addObject("countryList", countryRepo.findAll());

            if (isAdmin)
            {
                mv.addObject("status", "Current User: " + currUser + " (Administrator)");
            }
            else
            {
                mv.addObject("status", "Current User: " + currUser + " (User)");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return mv;
    }

    //Helper Function
    private String getRecordByCountryAndDate(String date)
    {
        try
        {
            //COVID19 API Call
            URL APIGetRequest = new URL("https://api.covid19tracking.narrativa.com/api/" + date);

            //Create Connection to URL
            HttpURLConnection connection = (HttpURLConnection) APIGetRequest.openConnection();

            //Set Request Method to GET
            connection.setRequestMethod("GET");

            //Check Connection Status
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                //Create Buffered Reader: Read from URL
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //Create Response String
                StringBuilder response = new StringBuilder();

                //Create Temporary Line
                String line;

                //Check if the input line is empty
                while ((line = input.readLine()) != null)
                {
                    response.append(line);
                }

                //Close the input reader after finishing reading
                input.close();

                return response.toString();
            }
            else
            {
                return "Unexpected HTTP response";
            }
        }
        catch (Exception e)
        {
            return "Exception: " + e.getMessage();
        }
    }

    //Submit New COVID-19 Information: Only Admin Can Save New Record
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam("id") String id,
                             @RequestParam("country") String country,
                             @RequestParam("date") String date,
                             @RequestParam("totalCases") String totalCases,
                             @RequestParam("totalDeaths") String totalDeaths,
                             @RequestParam("newCases") String newCases,
                             @RequestParam("newDeaths") String newDeaths,
                             @RequestParam("userID") String userID)
    {
        //Parameter passing: id and user_id are empty

        //Set view (home.jsp)
        ModelAndView mv = new ModelAndView("home");

        //The user is admin
        if (isAdmin)
        {
            //Input record is not empty
            if (!country.isEmpty() && !date.isEmpty() && !totalCases.isEmpty() && !totalDeaths.isEmpty() && !newCases.isEmpty() && !newDeaths.isEmpty() )
            {
                //Create country instance: id is empty (id will not be passed in)

                //Check if the country already in the country table
                Optional<Country> countryRecord = countryRepo.findByCountryName(country);

                //Convert String to LocalDate for date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate formattedDate = LocalDate.parse(date, formatter);

                //The country record exists: Update exist record
                if (countryRecord.isPresent())
                {
                    //Update COVID-19 data from this country
                    countryRecord.get().setDate(formattedDate);
                    countryRecord.get().setTotalCases(Integer.parseInt(totalCases));
                    countryRecord.get().setTotalDeaths(Integer.parseInt(totalDeaths));
                    countryRecord.get().setNewCases(Integer.parseInt(newCases));
                    countryRecord.get().setNewDeaths(Integer.parseInt(newDeaths));

                    //Find current user id
                    Optional<User> userRecord = userRepo.findByUsername(currUser);

                    userRecord.ifPresent(user -> countryRecord.get().setUserID(user.getID()));

                    //Save the updated country record to the country repository
                    countryRepo.save(countryRecord.get());

                    //Set model
                    mv.addObject("savedNotification", "Record Updated Successfully!");
                }

                //The country record doesn't exist: Create new record
                else
                {
                    //Create a country instance to store passing information (UUID will be generated automatically)
                    Country countryToSave = new Country();

                    //Set other attribute
                    countryToSave.setCountryName(country);
                    countryToSave.setDate(formattedDate);
                    countryToSave.setTotalCases(Integer.parseInt(totalCases));
                    countryToSave.setTotalDeaths(Integer.parseInt(totalDeaths));
                    countryToSave.setNewCases(Integer.parseInt(newCases));
                    countryToSave.setNewDeaths(Integer.parseInt(newDeaths));

                    //Find current user id
                    Optional<User> userRecord = userRepo.findByUsername(currUser);

                    userRecord.ifPresent(user -> countryToSave.setUserID(user.getID()));

                    //Save the new country record to the country repository
                    countryRepo.save(countryToSave);

                    //Set model
                    mv.addObject("savedNotification", "Record Saved Successfully!");
                }
            }
            else
            {
                //Set model
                mv.addObject("savedNotification", "ERROR! Incomplete Record!");
            }

            mv.addObject("status", "Current User: " + currUser + " (Administrator)");
        }
        //The user is not admin
        else
        {
            mv.addObject("savedNotification", "Note: Only administrator can save record to the database!");
            mv.addObject("status", "Current User: " + currUser + " (User)");
        }

        //Set model
        mv.addObject("countryList", countryRepo.findAll());

        return mv;
    }

    //View COVID-19 Database By Chart
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("id") String id)
    {
        //Set view (view.jsp)
        ModelAndView mv = new ModelAndView("view");

        //Get country record
        Optional<Country> countryRecord = countryRepo.findById(id);

        //Check if the record is exist
        if (countryRecord.isPresent())
        {
            //Get event data
            Country targetCountry = countryRecord.get();

            //Set model
            mv.addObject("selectedCountry", targetCountry);

            //Get user record
            Optional<User> userRecord = userRepo.findById(targetCountry.getUserID());

            //Check if the record is exist
            if (userRecord.isPresent())
            {
                //Get user data
                User targetUser = userRecord.get();

                //Set model
                mv.addObject("selectedUser", targetUser);
            }

            //Declare data related variable
            Map<Object,Object> map1 = new HashMap<>();
            Map<Object,Object> map2 = new HashMap<>();
            Map<Object,Object> map3 = new HashMap<>();
            Map<Object,Object> map4 = new HashMap<>();
            List<Map<Object, Object>> dataPoint1 = new ArrayList<>();
            List<Map<Object, Object>> dataPoint2 = new ArrayList<>();
            List<List<Map<Object,Object>>> dataList1 = new ArrayList<List<Map<Object,Object>>>();
            List<List<Map<Object,Object>>> dataList2 = new ArrayList<List<Map<Object,Object>>>();

            //Total Cases
            map1.put("label", "Total Cases");
            map1.put("y", targetCountry.getTotalCases());
            dataPoint1.add(map1);

            //Total Deaths
            map2.put("label", "Total Deaths");
            map2.put("y", targetCountry.getTotalDeaths());
            dataPoint1.add(map2);

            dataList1.add(dataPoint1);
            mv.addObject("dataList1", dataList1);

            //New Cases
            map3.put("label", "New Cases");
            map3.put("y", targetCountry.getNewCases());
            dataPoint2.add(map3);

            //New Deaths
            map4.put("label", "New Deaths");
            map4.put("y", targetCountry.getNewDeaths());
            dataPoint2.add(map4);

            dataList2.add(dataPoint2);
            mv.addObject("dataList2", dataList2);
        }

        //Send this view with model to view.jsp
        return mv;
    }

    //Edit Event
    //Homepage -> Edit Page -> Save Function -> Homepage
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") String id)
    {
        ModelAndView mv;

        //The user is admin
        if (isAdmin)
        {
            //Set view (edit.jsp)
            mv = new ModelAndView("edit");

            //Get country record
            //Optional: may and may not find that record
            Optional<Country> countryRecord = countryRepo.findById(id);

            //Check if the record is exist
            if (countryRecord.isPresent())
            {
                //Get event data
                Country targetCountry = countryRecord.get();

                //Set model
                //Add only one country
                mv.addObject("selectedCountry", targetCountry);
            }
        }

        //The user is not admin
        else
        {
            //Set view (home.jsp)
            mv = new ModelAndView("home");

            //Set model
            mv.addObject("editNotification", "Note: Only administrator can edit record from the database!");
            mv.addObject("countryList", countryRepo.findAll());
            mv.addObject("status", "Current User: " + currUser + " (User)");
        }

        //Send this view with model to edit.jsp
        return mv;
    }

    //Delete Weather Information
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") String id)
    {
        //Set view (home.jsp)
        ModelAndView mv = new ModelAndView("home");

        //The user is admin
        if (isAdmin)
        {
            //Delete the specific country from the country repository (based on id)
            countryRepo.deleteById(id);
            mv.addObject("status", "Current User: " + currUser + " (Administrator)");
        }

        //The user is not admin
        else
        {
            mv.addObject("deletedNotification", "Note: Only administrator can delete record from the database!");
            mv.addObject("status", "Current User: " + currUser + " (User)");
        }

        //Set model
        mv.addObject("countryList", countryRepo.findAll());

        return mv;
    }

    //Logout
    @RequestMapping(value = "/logout")
    public ModelAndView logout()
    {
        //Set view (index.jsp)
        ModelAndView mv = new ModelAndView("index");

        return mv;
    }
}
