package com.gdu.wacdo.test.controller;


import com.gdu.wacdo.repository.EmployeeRepository;
import com.gdu.wacdo.repository.RestaurantRepository;
import com.gdu.wacdo.test.service.InsertTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class wacdoController {

    @Autowired
    private InsertTest insertTest;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Transactional
    @GetMapping("/insertPlanningGame")
    public String insertPG() throws Exception {
        insertTest.deleteAll();
        log.info("[SUCCESS] CLEAR DB !");

        insertTest.insertResponsability();
        log.info("[SUCCESS] insert 5 responsabilities role in db!");

        insertTest.insertRole();
        log.info("[SUCCESS] insert 3 role roles in db!");

        insertTest.insertRestaurant();
        log.info("[SUCCESS] insert 20 restaurants in db!");

        insertTest.insertEmployee();
        log.info("[SUCCESS] insert 25 employees in db!");

        insertTest.assignementEmployeeRestaurant();
        log.info("[SUCCESS] insert Assignement in db!");

        return "[SUCCESS] Insert matrice test  Done ! ";
    }


    @GetMapping("/insertRequired")
    public String insertRequired() {
        insertTest.insertResponsability();
        insertTest.insertRole();
        // access
        // permission
        return "[SUCCESS] insert 3 role roles in db!";
    }

    @GetMapping("/insertRestaurant")
    public String insertRestaurant() {
        insertTest.insertRestaurant();
        return "[SUCCESS] insert 20 restaurants in db!";
    }

    @GetMapping("/insertEmployee")
    public String insertEmployee() {
        insertTest.insertEmployee();
        return "[SUCCESS] insert 25 employees in db!";
    }

    @GetMapping("/insertAssignement")
    public String insertAssignement() throws Exception {
        insertTest.assignementEmployeeRestaurant();
        return "[SUCCESS] insert Assignement in db!";
    }

    // clean db
    @GetMapping("/deleteAll")
    public String deleteAll() {
        insertTest.deleteAll();
        return "[SUCCESS] clear all restaurant in db!";
    }

    @GetMapping("/insertResponsability")
    public String insertResponsability() {
        insertTest.insertResponsability();
        return "[SUCCESS] insert 5 responsabilities role in db!";
    }

    @GetMapping("/insertRole")
    public String insertRole() {
        insertTest.insertRole();
        return "[SUCCESS] insert 3 roles role in db!";
    }

}
