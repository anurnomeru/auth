package com.raythonsoft.sso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_user_organization")
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserOrganization {
    @Id
    @Column(name = "user_organization_id")
    private Integer userOrganizationId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "organization_id")
    private Integer organizationId;
}