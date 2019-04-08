package com.sujya.prj.entity.repository;

import com.sujya.prj.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    RegionEntity findByRegionNm(String regionNm);
    RegionEntity findByRegionCd(String regionCd);
}
