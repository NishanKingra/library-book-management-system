package edu.library.model;

public class Book {
    private Long id; private String title, author, category; private int totalCopies, availableCopies;
    public Book() {}
    public Book(Long id, String title, String author, String category, int totalCopies, int availableCopies) {
        this.id=id; this.title=title; this.author=author; this.category=category; this.totalCopies=totalCopies; this.availableCopies=availableCopies;
    }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getAuthor(){return author;} public void setAuthor(String v){author=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public int getTotalCopies(){return totalCopies;} public void setTotalCopies(int v){totalCopies=v;}
    public int getAvailableCopies(){return availableCopies;} public void setAvailableCopies(int v){availableCopies=v;}
}
