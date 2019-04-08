package com.sujya.prj.entity.repository;

import com.sujya.prj.entity.RegionCodeEntity;
import com.sujya.prj.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionCodeRepository extends JpaRepository<RegionCodeEntity, Integer> {
    RegionCodeEntity findByLocNm(String locNm);
    RegionCodeEntity findByLocNo(String locNo);
}
