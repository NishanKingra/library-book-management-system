package edu.library.model;

public class Student {
    private Long id; private String fullName, email, department;
    public Student() {}
    public Student(Long id,String fullName,String email,String department){this.id=id;this.fullName=fullName;this.email=email;this.department=department;}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getDepartment(){return department;} public void setDepartment(String v){department=v;}
}
