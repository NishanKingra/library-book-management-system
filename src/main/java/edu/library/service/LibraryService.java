package edu.library.service;

import edu.library.model.*; import edu.library.repository.*; import org.springframework.dao.DuplicateKeyException; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class LibraryService {
    private final BookRepository books; private final LibraryRepository data; private final JdbcTemplate jdbc;
    public LibraryService(BookRepository books,LibraryRepository data,JdbcTemplate jdbc){this.books=books;this.data=data;this.jdbc=jdbc;}
    public List<Book> books(String q){return books.findAll(q);} public Optional<Book> book(long id){return books.findById(id);}
    @Transactional public void saveBook(Book b){validateBook(b);if(b.getId()==null){b.setAvailableCopies(b.getTotalCopies());books.save(b);return;}Book old=books.findById(b.getId()).orElseThrow(()->new IllegalArgumentException("Book not found"));int borrowed=old.getTotalCopies()-old.getAvailableCopies();if(b.getTotalCopies()<borrowed)throw new IllegalArgumentException("Total copies cannot be lower than copies currently on loan ("+borrowed+")");b.setAvailableCopies(b.getTotalCopies()-borrowed);books.save(b);}
    private void validateBook(Book b){if(blank(b.getTitle())||blank(b.getAuthor())||blank(b.getCategory()))throw new IllegalArgumentException("Title, author and category are required");if(b.getTotalCopies()<0)throw new IllegalArgumentException("Total copies cannot be negative");}
    @Transactional public void archiveBook(long id){int active=jdbc.queryForObject("SELECT COUNT(*) FROM book_issues WHERE book_id=? AND status='ACTIVE'",Integer.class,id);if(active>0)throw new IllegalArgumentException("Return all active copies before archiving this book");if(jdbc.update("UPDATE books SET archived=TRUE WHERE id=? AND archived=FALSE",id)!=1)throw new IllegalArgumentException("Book not found");}
    public List<Student> students(String q){return data.students(q);} public List<Student> studentOptions(){return data.studentOptions();} public Optional<Student> student(long id){return data.student(id);}
    @Transactional public void saveStudent(Student s){if(blank(s.getFullName())||blank(s.getDepartment())||s.getEmail()==null||!s.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))throw new IllegalArgumentException("Enter a name, department and valid email");try{data.saveStudent(s);}catch(DuplicateKeyException ex){throw new IllegalArgumentException("That email address is already registered");}}
    @Transactional public void deleteStudent(long id){if(!data.deleteStudent(id))throw new IllegalArgumentException("Student has borrowing history or does not exist; history is retained");}
    public List<Issue> issues(String status){return data.issues(status);} public List<Issue> history(long id){return data.history(id);} public List<Fine> fines(){return data.fines();} public List<Fine> finesForStudent(long id){return data.finesForStudent(id);} public Map<String,Object> dashboard(){return data.dashboard();}
    @Transactional public void issue(long studentId,long bookId){if(!data.lockStudent(studentId))throw new IllegalArgumentException("Select an existing student");if(data.activeCount(studentId)>=3)throw new IllegalArgumentException("A student may have at most 3 active books");if(!data.issueBook(studentId,bookId))throw new IllegalArgumentException("This book is unavailable");}
    @Transactional public void returnBook(long id){if(!data.returnIssue(id))throw new IllegalArgumentException("This issue is not active or was already returned");}
    @Transactional public void payFine(long id){data.payFine(id);}
    private boolean blank(String s){return s==null||s.isBlank();}
}
