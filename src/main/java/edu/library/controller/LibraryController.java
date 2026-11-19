package edu.library.controller;

import edu.library.model.*; import edu.library.service.LibraryService; import jakarta.validation.Valid; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.validation.BindingResult; import org.springframework.web.bind.annotation.*; import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LibraryController {
    private final LibraryService service; public LibraryController(LibraryService service){this.service=service;}
    private void shell(Model m,String active){m.addAttribute("active",active);m.addAttribute("stats",service.dashboard());}
    @GetMapping("/") public String home(Model m){shell(m,"dashboard");m.addAttribute("recent",service.issues(null).stream().limit(6).toList());return "dashboard";}
    @GetMapping("/books") public String books(@RequestParam(defaultValue="")String q,Model m){shell(m,"books");m.addAttribute("q",q);m.addAttribute("books",service.books(q));return "books";}
    @GetMapping("/books/new") public String newBook(Model m){shell(m,"books");m.addAttribute("book",new Book());return "book-form";}
    @GetMapping("/books/{id}/edit") public String editBook(@PathVariable long id,Model m){shell(m,"books");m.addAttribute("book",service.book(id).orElseThrow(()->new IllegalArgumentException("Book not found")));return "book-form";}
    @PostMapping("/books/save") public String saveBook(@ModelAttribute Book book,RedirectAttributes ra){try{service.saveBook(book);ra.addFlashAttribute("success","Book saved successfully");return "redirect:/books";}catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());return "redirect:/books";}}
    @PostMapping("/books/{id}/delete") public String deleteBook(@PathVariable long id,RedirectAttributes ra){try{service.archiveBook(id);ra.addFlashAttribute("success","Book archived");}catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/books";}
    @GetMapping("/students") public String students(@RequestParam(defaultValue="")String q,Model m){shell(m,"students");m.addAttribute("q",q);m.addAttribute("students",service.students(q));return "students";}
    @GetMapping("/students/new") public String newStudent(Model m){shell(m,"students");m.addAttribute("student",new Student());return "student-form";}
    @GetMapping("/students/{id}/edit") public String editStudent(@PathVariable long id,Model m){shell(m,"students");m.addAttribute("student",service.student(id).orElseThrow(()->new IllegalArgumentException("Student not found")));return "student-form";}
    @PostMapping("/students/save") public String saveStudent(@ModelAttribute Student student,RedirectAttributes ra){try{service.saveStudent(student);ra.addFlashAttribute("success","Student saved successfully");return "redirect:/students";}catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());return "redirect:/students";}}
    @PostMapping("/students/{id}/delete") public String deleteStudent(@PathVariable long id,RedirectAttributes ra){try{service.deleteStudent(id);ra.addFlashAttribute("success","Student deleted");}catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/students";}
    @GetMapping("/students/{id}/history") public String history(@PathVariable long id,Model m){Student s=service.student(id).orElseThrow(()->new IllegalArgumentException("Student not found"));shell(m,"students");m.addAttribute("student",s);m.addAttribute("history",service.history(id));m.addAttribute("fines",service.finesForStudent(id));return "history";}
    @GetMapping("/issues/new") public String issueForm(Model m){shell(m,"issues");m.addAttribute("students",service.studentOptions());m.addAttribute("books",service.books(""));return "issue-form";}
    @PostMapping("/issues") public String issue(@RequestParam long studentId,@RequestParam long bookId,RedirectAttributes ra){try{service.issue(studentId,bookId);ra.addFlashAttribute("success","Book issued. Due in 14 days.");}catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/issues/new";}
    @GetMapping("/transactions") public String transactions(@RequestParam(required=false)String status,Model m){shell(m,"transactions");m.addAttribute("issues",service.issues(status));m.addAttribute("filter",status==null?"":status);return "transactions";}
    @PostMapping("/transactions/{id}/return") public String returnBook(@PathVariable long id,RedirectAttributes ra){try{service.returnBook(id);ra.addFlashAttribute("success","Return processed and fine calculated");}catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/transactions";}
    @GetMapping("/fines") public String fines(Model m){shell(m,"fines");m.addAttribute("fines",service.fines());return "fines";}
    @PostMapping("/fines/{id}/pay") public String pay(@PathVariable long id,RedirectAttributes ra){try{service.payFine(id);ra.addFlashAttribute("success","Fine marked as paid");}catch(RuntimeException e){ra.addFlashAttribute("error",e.getMessage());}return "redirect:/fines";}
    @GetMapping("/reports") public String reports(@RequestParam(required=false)String status,Model m){shell(m,"reports");m.addAttribute("issues",service.issues(status));m.addAttribute("filter",status==null?"":status);m.addAttribute("students",service.studentOptions());m.addAttribute("fines",service.fines());return "reports";}
    @ExceptionHandler(Exception.class) public String handle(Exception e,RedirectAttributes ra){ra.addFlashAttribute("error","The requested record could not be loaded.");return "redirect:/";}
}
