package com.aisw.community.model.entity;

import com.aisw.community.model.enumclass.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Accessors(chain = true)
@ToString(exclude = {"universityList", "departmentList", "councilList",
        "freeList", "qnaList", "freeCommentList", "qnaCommentList"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private Integer studentId;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    // 가입하지 않으면 0, 재학생 1, 학생회 2, 직원(조교, 교수) 3, 관리자 4
    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    private Job job;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Campus university;

    @Enumerated(EnumType.STRING)
    private CollegeName collegeName;

    @Enumerated(EnumType.STRING)
    private DepartmentName departmentName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<University> universityList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Department> departmentList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Council> councilList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Free> freeList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Qna> qnaList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<FreeComment> freeCommentList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<QnaComment> qnaCommentList;
}
