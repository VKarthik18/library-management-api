package com.library.librarymanagementapi.repository;

import com.library.librarymanagementapi.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByReturnedFalse();
    List<Issue> findByReturnedFalseAndDueDateBefore(java.time.LocalDate date);
    List<Issue> findByMemberId(Long memberId);
    List<Issue> findByMemberIdAndReturnedFalse(Long memberId);
    List<Issue> findByMemberIdAndReturnedTrue(Long memberId);
    
}