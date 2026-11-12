package com.library.librarymanagementapi.controller;

import jakarta.validation.Valid;
import com.library.librarymanagementapi.entity.Issue;
import com.library.librarymanagementapi.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<Issue> getAllIssues() {
        return issueService.getAllIssues();
    }

    @GetMapping("/active")
public List<Issue> getActiveIssues() {
    return issueService.getActiveIssues();
    }

    @GetMapping("/overdue")
    public List<Issue> getOverdueIssues() {
        return issueService.getOverdueIssues();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Issue addIssue(@Valid @RequestBody Issue issue) {
        return issueService.addIssue(issue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(
            @PathVariable Long id,
            @Valid @RequestBody Issue issue) {

        if (issueService.getIssueById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        issue.setId(id);
        return ResponseEntity.ok(issueService.updateIssue(issue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {

        if (issueService.getIssueById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/return")
public ResponseEntity<Issue> returnBook(@PathVariable Long id) {

    return ResponseEntity.ok(issueService.returnBook(id));
}
}