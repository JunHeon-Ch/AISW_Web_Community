package com.aisw.community.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Level {
    NOT_SUBSCRIBED(0, "미가입", ""),
    STUDENT(1, "재학생", ""),
    COUNCIL(2, "학생회", ""),
    FACULTY(3, "교직원", "교수 및 조교"),
    ADMINISTRATOR(4, "관리자", "");

    private Integer id;
    private String title;
    private String description;
}
