package com.aisw.community.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

// 로그인 사용자 감시
@Component
public class LoginUserAuditorAware implements AuditorAware<String> {
    // 현재 감시자를 AdminServer로 지정
    // TODO: createdBy updatedBy 수정 필요
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("AdminServer");
    }
}
