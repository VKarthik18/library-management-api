package com.library.librarymanagementapi.repository;

import com.library.librarymanagementapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}