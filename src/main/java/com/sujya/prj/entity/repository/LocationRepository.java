package com.sujya.prj.entity.repository;

import com.sujya.prj.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
    @Query(value="select * from LOC_TB l where l.region like %:region% ", nativeQuery=true)
    List<LocationEntity> findByRegion(@Param("region") String region);

    LocationEntity findByLocId(int locId);

    @Query(value="select * from LOC_TB order by recom_limit desc, rate asc", nativeQuery=true)
    List<LocationEntity> orderByLimitAndRateDESC();

}
