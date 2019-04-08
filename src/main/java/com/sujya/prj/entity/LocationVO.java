package com.sujya.prj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LocationVO {

    private String locId;

    private String region;

    private String target;

    private String usage;

    private String limit;

    private String rate;

    private String institute;

    private String mgmt;

    private String reception;

    public LocationVO(LocationEntity entity, RegionEntity regionEntity){
        this.region = regionEntity.getRegionNm();
        this.target = entity.getTarget();
        this.usage = entity.getUsage();
        this.limit = entity.getLimit();
        this.rate = entity.getRate();
        this.institute = entity.getInstitute();
        this.mgmt = entity.getMgmt();
        this.reception = entity.getReception();

    }

}

