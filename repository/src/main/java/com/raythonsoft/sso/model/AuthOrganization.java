package com.raythonsoft.sso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_organization")
@NoArgsConstructor
@AllArgsConstructor
public class AuthOrganization {
    @Id
    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "parent_id")
    private Integer parentId;

    private String name;

    private String description;

    @Column(name = "create_time")
    private Date createTime;
}