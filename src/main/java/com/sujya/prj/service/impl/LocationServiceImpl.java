package com.sujya.prj.service.impl;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sujya.prj.config.MultipartConfiguration;
import com.sujya.prj.config.SJException;
import com.sujya.prj.entity.*;
import com.sujya.prj.entity.repository.LocationRepository;
import com.sujya.prj.entity.repository.RegionCodeRepository;
import com.sujya.prj.entity.repository.RegionRepository;
import com.sujya.prj.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    MultipartConfiguration multipartConfiguration;

    @Autowired
    RegionCodeRepository regionCodeRepository;

    @Override
    public String saveFile(MultipartFile file){
        try{
            String path = multipartConfiguration.getUploadDir();
            Path rootLocation = Paths.get(path);
            String allowedExtensions = "csv";

            if(file.getOriginalFilename() != null || !file.getOriginalFilename().equals("")){
                int idx = file.getOriginalFilename().lastIndexOf(".");
                String extension = file.getOriginalFilename().substring(idx+1);
                if(!extension.equals(allowedExtensions)){
                    throw new SJException("Not Allowed Extension", HttpStatus.BAD_REQUEST.value());
                }
            }
            File tt = new File("assets/" + file.getOriginalFilename());
            Files.deleteIfExists(tt.toPath());


            Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
        }catch(Exception e){
            throw new SJException("Not Allowed Extension", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            parseData(LocationCSV.class, file);
        }

        return file.getOriginalFilename();
    }

    private List<LocationCSV> parseData(Class<LocationCSV> type, MultipartFile file){
        List<LocationCSV> result = new ArrayList<>();
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();

            File convFile = new File("assets/" + file.getOriginalFilename());
            MappingIterator<LocationCSV> readValues = mapper.reader(type).with(bootstrapSchema)
                    .readValues(new InputStreamReader(new FileInputStream(convFile), "EUC-KR"));
            result = readValues.readAll();
        }catch (Exception e){
            throw new SJException("parseData", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            saveData(result);

        }
        return result;
    }

    private void saveData(List<LocationCSV> list){
        Map<String, List<LocationCSV>> temp = list.stream().collect(Collectors.groupingBy(LocationCSV::getRegion));

        int[] idx = {0};
        List<RegionEntity> regionList = temp.entrySet().stream().map(x->{
            RegionCodeEntity rc = regionCodeRepository.findByLocNm(x.getKey());
            if(rc == null){
                rc = regionCodeRepository.findByLocNo("99");
                idx[0]++;
                return new RegionEntity("reg"+rc.getLocNo() + idx[0], x.getKey());
            }else{
                return new RegionEntity("reg"+rc.getLocNo(), x.getKey());
            }
        }).collect(Collectors.toList());
        regionRepository.saveAll(regionList);

        List<LocationEntity> locList = list.stream().map(l->{
            RegionEntity regionEntity = regionRepository.findByRegionNm(l.getRegion());
            String regCd = regionEntity.getRegionCd();
            int noIdx = l.getLimit().indexOf("억");
            int limitNo = 0;
            if(noIdx > 0){
                limitNo = Integer.parseInt(l.getLimit().substring(0, noIdx));
            }

            String[] avgArr = l.getRate().split("~");
            if(!Character.isDigit(avgArr[0].substring(0, avgArr[0].length()-1).charAt(0))){
                return new LocationEntity(l.getLocId(), regCd, l.getTarget(), l.getUsage(), l.getLimit(), limitNo, l.getRate(), 0.0,
                        l.getInstitute(), l.getMgmt(), l.getReception());
            }
            double top = Double.parseDouble(avgArr[0].substring(0, avgArr[0].length()-1));
            double avgRate = top;
            if(avgArr.length > 1){
                double bottom = Double.parseDouble(avgArr[1].substring(0, avgArr[1].length()-1));
                avgRate = (top + bottom)/2;
            }

            return new LocationEntity(l.getLocId(), regCd, l.getTarget(), l.getUsage(), l.getLimit(), limitNo, l.getRate(), avgRate,
                    l.getInstitute(), l.getMgmt(), l.getReception());
        }).collect(Collectors.toList());

        locationRepository.saveAll(locList);
    }



    @Override
    public List<LocationVO> allLocation() {
        List<LocationEntity> locations = locationRepository.findAll();

        List<LocationVO> result = locations.stream()
                .map(temp -> {
                    RegionEntity reg = regionRepository.findByRegionCd(temp.getRegionCd());
                    return new LocationVO(temp, reg);
                }).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<LocationVO> searchLocation(String loc) {
        RegionEntity regionEntity = regionRepository.findByRegionNm(loc);
        List<LocationEntity> locations = locationRepository.findByRegionCd(regionEntity.getRegionCd());
        List<LocationVO> result = locations.stream().map(temp -> new LocationVO(temp, regionEntity)).collect(Collectors.toList());
        return result;
    }

    @Override
    public LocationVO updateLocation(LocationVO loc) {

        LocationEntity baseEntity = locationRepository.findByLocId(loc.getLocId());
        RegionEntity regionEntity = regionRepository.findByRegionCd(baseEntity.getRegionCd());
        if(baseEntity == null){
            return null;
        }else{
            if(loc.getRegion() != null && !loc.getRegion().equals("")){
                regionEntity.setRegionNm(loc.getRegion());
                regionRepository.save(regionEntity);
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
            return new LocationVO(saved, regionEntity);
        }

    }

    @Override
    public List<String> limitK(int k) {

        List<LocationEntity> list = locationRepository.orderByLimitAndRateDESC();
        List<String> result = list.stream()
                .limit(k).map(t->regionRepository.findByRegionCd(t.getRegionCd()).getRegionNm()).collect(Collectors.toList());
        return result;

    }

    @Override
    public LocationVO smallestRate(){
        List<LocationEntity> list = locationRepository.findAll(new Sort(Sort.Direction.ASC, "rate"));
        LocationEntity result = list.get(0);
        return new LocationVO(result, regionRepository.findByRegionCd(result.getRegionCd()));
    }
}
