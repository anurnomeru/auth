package com.raythonsoft.sso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_permission")
@NoArgsConstructor
@AllArgsConstructor
public class AuthPermission {
    @Id
    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "system_id")
    private Integer systemId;

    @Column(name = "parent_id")
    private Integer parentId;

    private String name;

    private String type;

    @Column(name = "permission_value")
    private String permissionValue;

    private String uri;

    private String icon;

    private String status;

    @Column(name = "create_time")
    private Date createTime;

    private Integer orders;
}