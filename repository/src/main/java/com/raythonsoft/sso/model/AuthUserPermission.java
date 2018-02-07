package com.raythonsoft.sso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_user_permission")
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserPermission {
    @Id
    @Column(name = "user_permission_id")
    private Integer userPermissionId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "permission_id")
    private Integer permissionId;

    private String type;

}