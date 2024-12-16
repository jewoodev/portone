package com.portone.domain.repository;

import com.portone.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findById(String id);
    Optional<Member> findByUsername(String username);
}
