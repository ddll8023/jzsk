package com.jzsk.backendv2.pojo.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体，对应旧库 jcxx.user 表。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String name;
    private String department;
    private String type;
    private String gender;
    private String idNumber;
    private String position;
    private String technicalTitle;
    private String academicQualifications;
    private Date expirationTime;
    private String phoneNumber;
    private Date workingTime;
    private String graduationInstitution;
    private String major;
    private String address;
    private String birthplace;
    private String ethnicity;
    private String email;
    private String birthday;
    private String politicalAppearance;
    private String note;
    private Integer userOrder;
    private Date createTime;
    private Date updateTime;
}
