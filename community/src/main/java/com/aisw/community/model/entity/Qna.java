package com.aisw.community.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"board"})
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String attachmentFile;

    // 긴급0, 상단고정1, 일반2
    private Long status;

    private Long views;

    private Long likes;

    private String subject;
    
    // 질문게시판 1
    private Long level;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;

    @ManyToOne
    private Board board; // board id
}
