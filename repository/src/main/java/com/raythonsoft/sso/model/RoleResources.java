package com.raythonsoft.sso.model;

import com.raythonsoft.common.model.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "role_resources")
public class RoleResources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;

    @Transient
    private PageModel pageModel;
}