package edu.library.model;

import java.math.BigDecimal;
public class Fine {
    private Long id, issueId; private String studentName, title; private BigDecimal amount; private int overdueDays; private boolean paid;
    public Fine(Long id,Long issueId,String studentName,String title,BigDecimal amount,int overdueDays,boolean paid){this.id=id;this.issueId=issueId;this.studentName=studentName;this.title=title;this.amount=amount;this.overdueDays=overdueDays;this.paid=paid;}
    public Long getId(){return id;} public Long getIssueId(){return issueId;} public String getStudentName(){return studentName;} public String getTitle(){return title;} public BigDecimal getAmount(){return amount;} public int getOverdueDays(){return overdueDays;} public boolean isPaid(){return paid;}
}
