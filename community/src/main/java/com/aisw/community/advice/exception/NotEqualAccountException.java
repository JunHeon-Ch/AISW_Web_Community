package com.aisw.community.advice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotEqualAccountException extends RuntimeException {

    private Long id;
}
