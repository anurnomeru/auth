package com.raythonsoft.sso.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_user_role")
public class AuthUserRole {
    @Id
    @Column(name = "user_role_id")
    private Integer userRoleId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;
}