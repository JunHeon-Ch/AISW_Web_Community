package com.aisw.community.model.network.request;

import com.aisw.community.model.enumclass.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserApiRequest {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private Grade grade;

    private Integer studentId;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private Level level;

    private Job job;

    private Gender gender;

    private Campus university;

    private College college;

    private Department department;

    private UserRole role;
}
