package com.raythonsoft.sso.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Builder
@Table(name = "auth_system")
public class AuthSystem {
    @Id
    @Column(name = "system_id")
    private Integer systemId;

    private String icon;

    private String banner;

    @Column(name = "base_path")
    private String basePath;

    private String status;

    private String name;

    private String title;

    private String description;

    @Column(name = "create_time")
    private Date createTime;

    private Integer orders;

}