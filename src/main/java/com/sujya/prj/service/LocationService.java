package com.sujya.prj.service;

import com.sujya.prj.entity.LocationEntity;
import com.sujya.prj.entity.LocationVO;
import com.sujya.prj.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LocationService {

    String saveFile(MultipartFile file);
    List<LocationVO> allLocation();
    List<LocationVO> searchLocation(String loc);
    LocationVO updateLocation(LocationVO loc);
    List<String> limitK(int k);
    LocationVO smallestRate();
}
