package com.raythonsoft.auth.model;

import com.raythonsoft.auth.bo.BaseRole;
import com.raythonsoft.auth.bo.BaseRolePermission;
import com.raythonsoft.auth.common.PageModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "role_permission")
public class RolePermission extends PageModel implements BaseRolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;

    @Transient
    private List<Permission> permissionList;
}