package com.raythonsoft.sso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_role")
@NoArgsConstructor
@AllArgsConstructor
public class AuthRole {
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    private String name;

    private String title;

    private String description;

    @Column(name = "create_time")
    private Date createTime;

    private Integer orders;
}