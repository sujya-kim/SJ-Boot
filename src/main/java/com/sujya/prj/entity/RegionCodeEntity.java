package com.sujya.prj.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="REGION_CODE_TB")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RegionCodeEntity {

    @Id
    @Column(name="LOC_NO", nullable = false)
    @JsonProperty("loc_no")
    private String locNo;

    @Column(name="LOC_NM", nullable = false)
    @JsonProperty("loc_nm")
    private String locNm;
}

