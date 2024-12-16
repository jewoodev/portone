package com.portone.web.service;

import com.portone.domain.entity.Member;
import com.portone.domain.repository.MemberRepository;
import com.portone.domain.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> userData = memberRepository.findByUsername(username);

        return userData.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
    }
}
