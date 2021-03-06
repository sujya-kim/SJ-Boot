package com.sujya.prj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LOC_TB")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LocationEntity {

    @Id
    @Column(name="LOC_ID", nullable = false)
    private String locId;

    @Column(name="REGION_CD", nullable = false)
    private String regionCd;

    @Column(name="TARGET", nullable = false)
    private String target;

    @Column(name="USAGE", nullable = false)
    private String usage;

    @Column(name="RECOM_LIMIT", nullable = false)
    private String limit;

    @Column(name="RECOM_LIMIT_NO", nullable = false)
    private Integer limitNo;

    @Column(name="RATE", nullable = false)
    private String rate;

    @Column(name="AVG_RATE", nullable = false)
    private Double avgRate;

    @Column(name="INSTITUTE", nullable = false)
    private String institute;

    @Column(name="MANAGEMENT", nullable = false)
    private String mgmt;

    @Column(name="RECEPTION", nullable = false)
    private String reception;

}

