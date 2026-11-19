package edu.library.repository;

import edu.library.model.Book;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/** Traditional JDBC demonstration: explicit Connection, PreparedStatement and ResultSet handling. */
@Repository
public class BookRepository {
    private final DataSource dataSource;
    public BookRepository(DataSource dataSource){this.dataSource=dataSource;}
    private Book map(ResultSet r)throws SQLException{return new Book(r.getLong("id"),r.getString("title"),r.getString("author"),r.getString("category"),r.getInt("total_copies"),r.getInt("available_copies"));}
    public List<Book> findAll(String q){String sql="SELECT * FROM books WHERE archived=FALSE AND (LOWER(title) LIKE ? OR LOWER(author) LIKE ? OR LOWER(category) LIKE ?) ORDER BY title";String term="%"+(q==null?"":q.trim().toLowerCase())+"%";List<Book> out=new ArrayList<>();try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){for(int i=1;i<=3;i++)p.setString(i,term);try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}}catch(SQLException e){throw new IllegalStateException("Could not load books",e);}return out;}
    public Optional<Book> findById(long id){try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM books WHERE id=? AND archived=FALSE")){p.setLong(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}catch(SQLException e){throw new IllegalStateException("Could not load book",e);}}
    public void save(Book b){String sql=b.getId()==null?"INSERT INTO books(title,author,category,total_copies,available_copies) VALUES(?,?,?,?,?)":"UPDATE books SET title=?,author=?,category=?,total_copies=?,available_copies=?-(total_copies-available_copies) WHERE id=? AND archived=FALSE AND ? >= (total_copies-available_copies)";try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,b.getTitle().trim());p.setString(2,b.getAuthor().trim());p.setString(3,b.getCategory().trim());p.setInt(4,b.getTotalCopies());p.setInt(5,b.getId()==null?b.getAvailableCopies():b.getTotalCopies());if(b.getId()!=null){p.setLong(6,b.getId());p.setInt(7,b.getTotalCopies());}if(p.executeUpdate()!=1)throw new IllegalArgumentException("Book not found or total copies are below the current loan count");}catch(SQLException e){throw new IllegalStateException("Could not save book",e);}}
}
