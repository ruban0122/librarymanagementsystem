package net.javaguides.lms.controller;

import jakarta.servlet.http.HttpSession;
import net.javaguides.lms.entity.Book;
import net.javaguides.lms.entity.User;
import net.javaguides.lms.repository.BookRepository;
import net.javaguides.lms.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/books")
public class UserBookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listBooks(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"USER".equals(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("loggedInUser", user); // for checking ownership
        return "user/list-books";
    }

    @GetMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) return "redirect:/login";

        Book book = bookRepository.findById(id).orElseThrow();
        if (!book.isBorrowed()) {
            book.setBorrowed(true);
            book.setBorrowedBy(userRepository.findById(user.getId()).orElse(null));
            bookRepository.save(book);
        }

        return "redirect:/user/books";
    }

    @GetMapping("/my-borrowed")
        public String myBorrowedBooks(Model model, HttpSession session) {
            User user = (User) session.getAttribute("loggedInUser");

            if (user == null || !"USER".equals(user.getRole())) {
                return "redirect:/login";
            }

            List<Book> borrowedBooks = bookRepository.findAll().stream()
                .filter(b -> b.isBorrowed() && b.getBorrowedBy() != null && b.getBorrowedBy().getId().equals(user.getId()))
                .toList();

            model.addAttribute("books", borrowedBooks);
            return "user/my-borrowed-books";
        }

    

    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        Book book = bookRepository.findById(id).orElseThrow();

        if (book.getBorrowedBy() != null && book.getBorrowedBy().getId().equals(user.getId())) {
            book.setBorrowed(false);
            book.setBorrowedBy(null);
            bookRepository.save(book);
        }

        return "redirect:/user/books";
    }
}
