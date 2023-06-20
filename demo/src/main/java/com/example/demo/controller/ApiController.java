package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.entity.Book;
import com.example.demo.services.BookService;
import com.example.demo.services.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/books")
public class ApiController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    private BookDto convertToBookDto(Book book)
    {
        BookDto bookDto = new BookDto();
        bookDto.setId(bookDto.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(bookDto.getAuthor());
        bookDto.setPrice(bookDto.getPrice());
        bookDto.setCategoryName(categoryService.getCategoryById(book.getCategory().getId()).getName());
        return bookDto;

    }
    @GetMapping
    @ResponseBody
    public List<BookDto> getAllBooks(){
        return bookService.getAllBooks().stream()
                .map(this::convertToBookDto)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public BookDto getBookById(@PathVariable Long id)
    {
        return convertToBookDto(bookService.getBookById(id));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteBook(@PathVariable Long id){
        if(bookService.getBookById(id) != null)
            bookService.deleteBook(id);
    }
}
