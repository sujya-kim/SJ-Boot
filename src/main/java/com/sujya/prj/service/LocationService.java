package com.sujya.prj.service;

import com.sujya.prj.entity.LocationEntity;
import com.sujya.prj.entity.LocationVO;
import com.sujya.prj.entity.UserEntity;

import java.util.List;

public interface LocationService {

    List<LocationVO> allLocation();
    List<LocationVO> searchLocation(String loc);
    LocationVO updateLocation(LocationEntity loc);
    List<String> limitK(int k);
}
