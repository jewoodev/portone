package com.portone.web.controller.validator;

import com.portone.domain.dto.SignupDto;
import com.portone.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignUpValidator implements Validator {
    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SignupDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupDto dto = (SignupDto) target;

        // 비밀번호 검증
        if (!dto.getPassword1().equals(dto.getPassword2())) {
            errors.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
        }

        // 아이디 검증
        try {
            memberService.existByUsername(dto.getUsername());
        } catch (IllegalStateException | InvalidDataAccessApiUsageException e) {
            errors.rejectValue("username", "duplicatedUsername", e.getMessage());
        }
    }
}
