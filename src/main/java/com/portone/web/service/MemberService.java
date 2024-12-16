package com.portone.web.service;

import com.portone.domain.entity.Member;
import com.portone.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findbyUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정입니다."));
    }

    public void existByUsername(String username) {
        if (!memberRepository.findByUsername(username).isEmpty())
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
    }

    @Transactional
    public String signup(Member member) {
        memberRepository.save(member);
        return member.getUsername();
    }
}
