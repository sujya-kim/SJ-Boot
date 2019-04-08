package com.sujya.prj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="REGION_TB")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RegionEntity {

    @Id
    @Column(name="REGION_CD", nullable = false)
    private String regionCd;

    @Column(name="REGION_NM", nullable = false)
    private String regionNm;
}

