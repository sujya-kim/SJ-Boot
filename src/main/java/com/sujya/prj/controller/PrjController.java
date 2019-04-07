package com.sujya.prj.controller;

import com.sujya.prj.entity.LocationEntity;
import com.sujya.prj.entity.LocationVO;
import com.sujya.prj.service.LocationService;
import com.sujya.prj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PrjController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> login() {

        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<LocationVO>> allLocation() {

        List<LocationVO> result = locationService.allLocation();

        return new ResponseEntity<List<LocationVO>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchLocation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<LocationVO>> searchLocation(@RequestBody LocationEntity loc) {

        List<LocationVO> result = locationService.searchLocation(loc.getRegion());

        return new ResponseEntity<List<LocationVO>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateLocation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<LocationVO> updateLocation(@RequestBody LocationEntity loc) {

        LocationVO result = locationService.updateLocation(loc);

        if(result == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<LocationVO>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/limitTop/{k}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<String>> limitTop(@PathVariable int k) {

        List<String> result = locationService.limitK(k);

        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }
}
