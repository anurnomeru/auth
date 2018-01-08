package com.raythonsoft.auth.model;

import com.raythonsoft.auth.bo.BaseSubjectRole;
import com.raythonsoft.auth.common.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "subject_role")
public class SubjectRole extends PageModel implements BaseSubjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "subject_id")
    private Integer subjectId;
}