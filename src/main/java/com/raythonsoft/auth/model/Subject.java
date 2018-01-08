package com.raythonsoft.auth.model;

import com.raythonsoft.auth.bo.BaseSubject;
import com.raythonsoft.auth.common.PageModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class Subject extends PageModel implements BaseSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String project;

    private String sign;

    @Transient
    private List<Permission> permissionList;
}