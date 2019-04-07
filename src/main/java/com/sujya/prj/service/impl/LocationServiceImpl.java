package com.sujya.prj.service.impl;

import com.sujya.prj.entity.LocationEntity;
import com.sujya.prj.entity.LocationVO;
import com.sujya.prj.entity.UserEntity;
import com.sujya.prj.entity.repository.LocationRepository;
import com.sujya.prj.entity.repository.UserRepository;
import com.sujya.prj.service.LocationService;
import com.sujya.prj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Override
    public List<LocationVO> allLocation() {
        List<LocationEntity> locations = locationRepository.findAll();

        List<LocationVO> result = locations.stream()
                .map(temp -> new LocationVO(temp)).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<LocationVO> searchLocation(String loc) {

        List<LocationEntity> locations = locationRepository.findByRegion(loc);
        List<LocationVO> result = locations.stream().map(temp -> new LocationVO(temp)).collect(Collectors.toList());
        return result;
    }

    @Override
    public LocationVO updateLocation(LocationEntity loc) {

        LocationEntity baseEntity = locationRepository.findByLocId(loc.getLocId());
        if(baseEntity == null){
            return null;
        }else{
            if(loc.getRegion() != null && !loc.getRegion().equals("")){
                baseEntity.setRegion(loc.getRegion());
            }
            if(loc.getInstitute() != null && !loc.getInstitute().equals("")){
                baseEntity.setInstitute(loc.getInstitute());
            }
            if(loc.getLimit() != null && !loc.getLimit().equals("")){
                baseEntity.setLimit(loc.getLimit());
            }
            if(loc.getMgmt() != null && !loc.getMgmt().equals("")){
                baseEntity.setMgmt(loc.getMgmt());
            }
            if(loc.getRate() != null && !loc.getRate().equals("")){
                baseEntity.setRate(loc.getRate());
            }
            if(loc.getReception() != null && !loc.getReception().equals("")){
                baseEntity.setReception(loc.getReception());
            }
            if(loc.getTarget() != null && !loc.getTarget().equals("")){
                baseEntity.setTarget(loc.getTarget());
            }
            if(loc.getUsage() != null && !loc.getUsage().equals("")){
                baseEntity.setUsage(loc.getUsage());
            }

            LocationEntity saved = locationRepository.save(baseEntity);
            return new LocationVO(saved);
        }

    }

    @Override
    public List<String> limitK(int k) {

        List<LocationEntity> list = locationRepository.findAll(new Sort(Sort.Direction.DESC, "limit"));
        List<String> result = list.stream()
                .limit(k).map(t->t.getRegion()).collect(Collectors.toList());

        return result;

    }
}
