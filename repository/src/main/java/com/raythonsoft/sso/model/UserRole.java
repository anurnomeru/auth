package com.raythonsoft.sso.model;

import com.raythonsoft.common.model.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Transient
    private PageModel pageModel;
}