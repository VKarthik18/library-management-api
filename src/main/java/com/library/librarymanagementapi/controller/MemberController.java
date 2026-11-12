package com.library.librarymanagementapi.controller;

import com.library.librarymanagementapi.entity.Issue;
import com.library.librarymanagementapi.entity.Member;
import com.library.librarymanagementapi.exception.ResourceNotFoundException;
import com.library.librarymanagementapi.service.IssueService;
import com.library.librarymanagementapi.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final IssueService issueService;

    public MemberController(MemberService memberService,
                            IssueService issueService) {
        this.memberService = memberService;
        this.issueService = issueService;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/issues")
    public ResponseEntity<List<Issue>> getMemberIssues(@PathVariable Long id) {

        if (memberService.getMemberById(id).isEmpty()) {
            throw new ResourceNotFoundException("Member not found");
        }

        return ResponseEntity.ok(issueService.getIssuesByMemberId(id));
    }

    @GetMapping("/{id}/issues/active")
    public List<Issue> getActiveMemberIssues(@PathVariable Long id) {
        return issueService.getActiveIssuesByMemberId(id);
    }

    @GetMapping("/{id}/issues/returned")
    public List<Issue> getReturnedMemberIssues(@PathVariable Long id) {
        return issueService.getReturnedIssuesByMemberId(id);
    }

    @PostMapping
    public Member addMember(@Valid @RequestBody Member member) {
        return memberService.addMember(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody Member member) {

        if (memberService.getMemberById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        member.setId(id);
        return ResponseEntity.ok(memberService.updateMember(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {

        if (memberService.getMemberById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}