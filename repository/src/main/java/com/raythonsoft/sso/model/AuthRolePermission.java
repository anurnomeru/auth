package com.raythonsoft.sso.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_role_permission")
public class AuthRolePermission {
    @Id
    @Column(name = "role_permission_id")
    private Integer rolePermissionId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;

}