package com.sujya.prj.initialize;

import com.sujya.prj.entity.LocationEntity;
import com.sujya.prj.entity.RoleEntity;
import com.sujya.prj.entity.UserEntity;
import com.sujya.prj.entity.repository.LocationRepository;
import com.sujya.prj.entity.repository.RoleRepository;
import com.sujya.prj.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
    private LocationRepository locationRepository;

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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("assets/test_sample.csv"), "EUC-KR"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                if (Character.isDigit(values[0].charAt(0))) {
                    LocationEntity loc = new LocationEntity();
                    loc.setLocId(Integer.parseInt(values[0]));
                    loc.setRegion(values[1]);
                    loc.setTarget(values[2]);
                    loc.setUsage(values[3]);
                    loc.setLimit(values[4]);
                    loc.setRate(values[5]);
                    loc.setInstitute(values[6].replace("\"", ""));
                    loc.setMgmt(values[7]);
                    loc.setReception(values[8].replace("\"", ""));
                    locationRepository.save(loc);
                }
            }
        }
    }

}
