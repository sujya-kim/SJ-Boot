package com.sujya.prj.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LocationCSV {

    @JsonProperty("구분")
    private String locId;

    @JsonProperty("지자체명(기관명)")
    private String region;

    @JsonProperty("지원대상")
    private String target;

    @JsonProperty("용도")
    private String usage;

    @JsonProperty("지원한도")
    private String limit;

    @JsonProperty("이차보전")
    private String rate;

    @JsonProperty("추천기관")
    private String institute;

    @JsonProperty("관리점")
    private String mgmt;

    @JsonProperty("취급점")
    private String reception;

}

