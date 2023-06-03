//package com.example.demo;
//
//import com.example.demo.entity.Book;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootApplication
//@ComponentScan(basePackages = "com.example.demo")
//@Configuration
//public class AppConfig {
//    @Bean
//    public List<Book> getBooks(){
//        List<Book> books = new ArrayList<>();
//
//        Book book1 = new Book();
//        book1.setId(1L);
//        book1.setTitle("Lap trinh");
//        book1.setAuthor("Tam");
//        book1.setPrice(10.99);
//        book1.setCategory("Cong nghe thong tin");
//        books.add(book1);
//
//        Book book2 = new Book();
//        book2.setId(2L);
//        book2.setTitle("Lap trinh");
//        book2.setAuthor("Tam");
//        book2.setPrice(10.99);
//        book2.setCategory("Cong nghe thong tin");
//        books.add(book2);
//
//        Book book3 = new Book();
//        book3.setId(3L);
//        book3.setTitle("Lap trinh");
//        book3.setAuthor("Tam");
//        book3.setPrice(10.99);
//        book3.setCategory("Cong nghe thong tin");
//        books.add(book3);
//
//        return books;
//    }
//}
