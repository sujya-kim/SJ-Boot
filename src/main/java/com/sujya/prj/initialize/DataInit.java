package com.sujya.prj.initialize;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sujya.prj.config.SJException;
import com.sujya.prj.entity.*;
import com.sujya.prj.entity.repository.LocationRepository;
import com.sujya.prj.entity.repository.RegionCodeRepository;
import com.sujya.prj.entity.repository.RoleRepository;
import com.sujya.prj.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.*;
import java.util.*;

@Component
public class DataInit implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RegionCodeRepository regionCodeRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String COMMA_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long count = userRepository.count();
        long count2 = roleRepository.count();

        if (count2 == 0) {
            RoleEntity p1 = new RoleEntity();
            p1.setRoleName("ADMIN");

            roleRepository.save(p1);
        }

        if (count == 0) {
            UserEntity p1 = new UserEntity();
            p1.setUserName("sj");
            p1.setUserId("sj");
            p1.setUserRole(1);
            p1.setPassword(bCryptPasswordEncoder.encode("sj"));

            userRepository.save(p1);
        }

        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();

            File convFile = new File("assets/location_code.csv");
            MappingIterator<RegionCodeEntity> readValues = mapper.reader(RegionCodeEntity.class).with(bootstrapSchema)
                    .readValues(new InputStreamReader(new FileInputStream(convFile), "EUC-KR"));
            List<RegionCodeEntity> result = readValues.readAll();
            regionCodeRepository.saveAll(result);
        }catch (Exception e){
            throw new SJException("parseData", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
