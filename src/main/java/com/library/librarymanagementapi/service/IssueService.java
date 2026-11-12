package com.library.librarymanagementapi.service;

import org.springframework.transaction.annotation.Transactional;
import com.library.librarymanagementapi.exception.ResourceNotFoundException;
import com.library.librarymanagementapi.entity.Book;
import com.library.librarymanagementapi.entity.Issue;
import com.library.librarymanagementapi.repository.BookRepository;
import com.library.librarymanagementapi.repository.IssueRepository;
import com.library.librarymanagementapi.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public IssueService(IssueRepository issueRepository,
                        BookRepository bookRepository,
                        MemberRepository memberRepository) {
        this.issueRepository = issueRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public List<Issue> getActiveIssues() {
    return issueRepository.findByReturnedFalse();
    }

    public List<Issue> getOverdueIssues() {
    return issueRepository.findByReturnedFalseAndDueDateBefore(LocalDate.now());
    }

    public List<Issue> getIssuesByMemberId(Long memberId) {
    return issueRepository.findByMemberId(memberId);
    }

    public List<Issue> getActiveIssuesByMemberId(Long memberId) {
    return issueRepository.findByMemberIdAndReturnedFalse(memberId);
    }

    public List<Issue> getReturnedIssuesByMemberId(Long memberId) {
    return issueRepository.findByMemberIdAndReturnedTrue(memberId);
    }

    public Optional<Issue> getIssueById(Long id) {
        return issueRepository.findById(id);
    }

    @Transactional
    public Issue addIssue(Issue issue) {

        Book book = bookRepository.findById(issue.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        memberRepository.findById(issue.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(issue.getIssueDate().plusDays(14));
        issue.setReturned(false);
        issue.setReturnDate(null);

        return issueRepository.save(issue);
    }
    
    @Transactional
    public Issue returnBook(Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

        if (Boolean.TRUE.equals(issue.isReturned())) {
            throw new RuntimeException("Book is already returned");
        }

        Book book = bookRepository.findById(issue.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        issue.setReturned(true);
        issue.setReturnDate(LocalDate.now());

        book.setAvailable(true);
        bookRepository.save(book);

        return issueRepository.save(issue);
    }

    @Transactional
    public Issue updateIssue(Issue issue) {

    issueRepository.findById(issue.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

    bookRepository.findById(issue.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

    memberRepository.findById(issue.getMemberId())
            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

    return issueRepository.save(issue);
}

    @Transactional
public void deleteIssue(Long id) {

    Issue issue = issueRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Issue not found"));

    if (!Boolean.TRUE.equals(issue.isReturned())) {

        Book book = bookRepository.findById(issue.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        book.setAvailable(true);
        bookRepository.save(book);
    }

    issueRepository.delete(issue);
}
}