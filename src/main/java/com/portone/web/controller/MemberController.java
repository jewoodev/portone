package com.portone.web.controller;

import com.portone.domain.common.Role;
import com.portone.domain.dto.LoginDto;
import com.portone.domain.dto.SignupDto;
import com.portone.domain.entity.Member;
import com.portone.web.controller.validator.SignUpValidator;
import com.portone.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SignUpValidator signUpValidator;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupDto", new SignupDto());
        return "members/signup";
    }

    @PostMapping("/signup")
    public String signup(
            @Validated @ModelAttribute SignupDto signupDto,
            BindingResult result
    ) {
        if (result.hasErrors())
            return "members/signup"; // 문제가 있는 경우는 다시 signup page
        signUpValidator.validate(signupDto, result);


        String encodedPwd = passwordEncoder.encode(signupDto.getPassword1());
        Member member = Member.builder()
                .uid(UUID.randomUUID().toString())
                .username(signupDto.getUsername())
                .password(encodedPwd)
                .role(Role.USER)
                .createDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        memberService.signup(member);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(
            @ModelAttribute("loginForm") LoginDto loginDto,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "exception", required = false) String exception,
            Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginForm", loginDto);
        return "members/login";
    }
}
