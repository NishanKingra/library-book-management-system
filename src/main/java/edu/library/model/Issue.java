package edu.library.model;

import java.time.LocalDate;
public class Issue {
    private Long id; private String title, studentName, email, status; private LocalDate issueDate,dueDate,returnDate;
    public Issue(Long id,String title,String studentName,String email,LocalDate issueDate,LocalDate dueDate,LocalDate returnDate,String status){this.id=id;this.title=title;this.studentName=studentName;this.email=email;this.issueDate=issueDate;this.dueDate=dueDate;this.returnDate=returnDate;this.status=status;}
    public Long getId(){return id;} public String getTitle(){return title;} public String getStudentName(){return studentName;} public String getEmail(){return email;} public LocalDate getIssueDate(){return issueDate;} public LocalDate getDueDate(){return dueDate;} public LocalDate getReturnDate(){return returnDate;} public String getStatus(){return status;}
}
