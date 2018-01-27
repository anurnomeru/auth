package com.raythonsoft.sso.model;

import com.raythonsoft.common.model.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Transient
    private PageModel pageModel;
}