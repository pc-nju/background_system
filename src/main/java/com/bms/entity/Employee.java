package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author 咸鱼
 * @date 2019-06-19 21:01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String gender;
    private Date birthday;
    @NotBlank
    private String idCard;
    private String wedlock;

    private Integer nationId;
    private Nation nation;

    private String nativePlace;

    private Integer politicId;
    private PoliticsStatus politicsStatus;

    private String email;
    private String phone;
    private String address;
    private String engageForm;
    private String tipTopDegree;
    private String specialty;
    private String school;
    private Date beginDate;
    private String workState;
    private String workId;
    private Double contractTerm;
    private Date conversionTime;
    private Date notWorkDate;
    private Date beginContract;
    private Date endContract;
    private Integer workAge;
}
