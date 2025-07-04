package net.javaguides.lms.controller;


import net.javaguides.lms.entity.Book;
import net.javaguides.lms.entity.User;
import net.javaguides.lms.repository.BookRepository;
import net.javaguides.lms.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ui/books")
public class UIBookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "list-books";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("book", book);
        return "edit-book";
    }
   
    @PostMapping("/update")
    public String updateBook(@ModelAttribute Book book) {
        bookRepository.save(book); // Will update if ID exists
        return "redirect:/ui/books";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/ui/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/ui/books";
    }

    @GetMapping("/simulate-login")
    public String simulateLogin(HttpSession session) {
        User user = new User();
        user.setId(2L); // Set to a real user ID from DB
        user.setName("Student1");
        user.setRole("USER"); // Or "ADMIN"
        session.setAttribute("loggedInUser", user);
        return "redirect:/ui/books";
    }

    @GetMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        
        if (user == null) {
            // Redirect to login or error page
            return "redirect:/simulate-login"; // or "redirect:/login"
        }

        Book book = bookRepository.findById(id).orElseThrow();

        if (!book.isBorrowed()) {
            book.setBorrowed(true);
            book.setBorrowedBy(userRepo.findById(user.getId()).orElse(null));
            bookRepository.save(book);
        }

        return "redirect:/ui/books";
    }


    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id, HttpSession session) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setBorrowed(false);
        book.setBorrowedBy(null);
        bookRepository.save(book);
        return "redirect:/ui/books";
    }
}

    



