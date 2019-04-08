package com.sujya.prj.controller;

import com.sujya.prj.config.MultipartConfiguration;
import com.sujya.prj.entity.LocationEntity;
import com.sujya.prj.entity.LocationVO;
import com.sujya.prj.entity.RegionVO;
import com.sujya.prj.service.LocationService;
import com.sujya.prj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class PrjController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    MultipartConfiguration multipartConfiguration;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> login() {

        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile[] file){
        String result = locationService.saveFile(file[0]);

        return new ResponseEntity<String>("saved", HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<LocationVO>> allLocation() {

        List<LocationVO> result = locationService.allLocation();

        return new ResponseEntity<List<LocationVO>>(result, HttpStatus.OK);
    }

    @PostMapping("/searchLocation")
    public ResponseEntity<List<LocationVO>> searchLocation(@RequestBody RegionVO loc) {

        List<LocationVO> result = locationService.searchLocation(loc.getRegion());

        return new ResponseEntity<List<LocationVO>>(result, HttpStatus.OK);
    }

    @PostMapping("/updateLocation")
    public ResponseEntity<LocationVO> updateLocation(@RequestBody LocationVO loc) {

        LocationVO result = locationService.updateLocation(loc);

        if(result == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<LocationVO>(result, HttpStatus.OK);
    }

    @GetMapping("/limitTop/{k}")
    public ResponseEntity<List<String>> limitTop(@PathVariable int k) {

        List<String> result = locationService.limitK(k);

        return new ResponseEntity<List<String>>(result, HttpStatus.OK);
    }

    @GetMapping("/smallestRate")
    public ResponseEntity<RegionVO> smallestRate(){
        LocationVO temp = locationService.smallestRate();
        RegionVO result = new RegionVO(temp.getRegion());
        return new ResponseEntity<RegionVO>(result, HttpStatus.OK);
    }
}
