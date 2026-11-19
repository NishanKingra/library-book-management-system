package edu.library.service;

import edu.library.model.Book;
import edu.library.model.Student;
import edu.library.repository.BookRepository;
import edu.library.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryServiceTest {
    private BookRepository books;
    private LibraryRepository data;
    private LibraryService service;

    @BeforeEach void setUp(){books=mock(BookRepository.class);data=mock(LibraryRepository.class);service=new LibraryService(books,data,mock(JdbcTemplate.class));}

    @Test void newBookStartsWithAllCopiesAvailable(){Book book=new Book(null,"Title","Author","Category",4,0);service.saveBook(book);assertEquals(4,book.getAvailableCopies());verify(books).save(book);}

    @Test void updatingBookPreservesCopiesCurrentlyBorrowed(){Book old=new Book(8L,"Title","Author","Category",5,3);when(books.findById(8L)).thenReturn(Optional.of(old));Book update=new Book(8L,"Title","Author","Category",7,7);service.saveBook(update);assertEquals(5,update.getAvailableCopies());verify(books).save(update);}

    @Test void cannotReduceTotalBelowBorrowedCopies(){when(books.findById(8L)).thenReturn(Optional.of(new Book(8L,"Title","Author","Category",5,3)));Book update=new Book(8L,"Title","Author","Category",1,1);assertThrows(IllegalArgumentException.class,()->service.saveBook(update));verify(books,never()).save(any());}

    @Test void issuingRejectsStudentAtLoanLimitBeforeInventoryChange(){when(data.lockStudent(3L)).thenReturn(true);when(data.activeCount(3L)).thenReturn(3);assertThrows(IllegalArgumentException.class,()->service.issue(3L,9L));verify(data,never()).issueBook(3L,9L);}

    @Test void issuingRejectsUnavailableBook(){when(data.lockStudent(3L)).thenReturn(true);when(data.activeCount(3L)).thenReturn(0);when(data.issueBook(3L,9L)).thenReturn(false);assertThrows(IllegalArgumentException.class,()->service.issue(3L,9L));}

    @Test void duplicateStudentEmailIsExplained(){Student student=new Student(null,"A Student","not-an-email","Science");assertThrows(IllegalArgumentException.class,()->service.saveStudent(student));verify(data,never()).saveStudent(any());}
}
