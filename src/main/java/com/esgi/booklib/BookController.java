package com.esgi.booklib;

import com.esgi.booklib.entities.*;
import com.esgi.booklib.repositories.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final SessionRepository sessionRepository;
    private final static String TXT_UNAUTHORIZED = "Vous n'êtes pas autorisé à accéder à cette requête.";

    public BookController(UserRepository userRepository, BookRepository bookRepository, ReaderRepository readerRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/sign_in")
    public ResponseEntity signIn(@RequestBody Connection connection) {
        String token = sessionRepository.signIn(connection.getLogin(), connection.getPassword());
        if(token != null) {
            return new ResponseEntity(token, HttpStatus.OK);
        } else {
            return new ResponseEntity("Vos identifiants de connexion sont invalides.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody User user){
        try{
            userRepository.save(user);
            return new ResponseEntity("Votre compte à bien été créé", HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books")
    public ResponseEntity getAllBooks(@RequestHeader HttpHeaders headers){
        int userId = Application.getAuthorization(headers, sessionRepository);
        if (userId <= 0) {
            return new ResponseEntity(TXT_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        Iterable<Book> iterableBook = bookRepository.findAll();
        List<DisplayBook> listBooks = new ArrayList<DisplayBook>();
        for(Book book : iterableBook){
            if(!book.isDelete()){
                listBooks.add(new DisplayBook(book));
            }
        }
        return new ResponseEntity(listBooks, HttpStatus.OK);
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity getDetailBook(@RequestHeader HttpHeaders headers, @PathVariable String isbn){
        int userId = Application.getAuthorization(headers, sessionRepository);
        if (userId <= 0) {
            return new ResponseEntity(TXT_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        Optional<Book> optBook = bookRepository.findById(isbn);
        if(optBook.isPresent()){
            Book book = optBook.get();
            if(!book.isDelete()){
                return new ResponseEntity(book.getDetailBook(), HttpStatus.OK);
            } else {
                return new ResponseEntity("Ce livre est introuvable.",HttpStatus.NOT_FOUND);
            }
        }
        String message = String.format("Le livre avec l'ISBN %s est inéxistant.", isbn);
        return new ResponseEntity(message,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/books")
    public ResponseEntity createBook(@RequestHeader HttpHeaders headers, @RequestBody Book book){
        int userId = Application.getAuthorization(headers, sessionRepository);
        if (userId <= 0) {
            return new ResponseEntity(TXT_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        } else if (book.getIsbn() == null){
            return new ResponseEntity("Le champ \"isbn\" est obligatoire.", HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (book.getTitle() == null){
            return new ResponseEntity("Le champ \"title\" est obligatoire.", HttpStatus.UNPROCESSABLE_ENTITY);
        } else if (book.getAuthor() == null){
            return new ResponseEntity("Le champ \"author\" est obligatoire.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Optional<Book> optBook = bookRepository.findById(book.getIsbn());
        if(optBook.isPresent()){
            Book oldBook = optBook.get();
            if(!oldBook.isDelete()){
                String message = String.format("Le livre avec l'ISBN %s éxiste déjà.", book.getIsbn());
                return new ResponseEntity(message, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            book.createBook(userId, userRepository);
        }
        book.createBook(userId, userRepository);
        try{
            bookRepository.save(book);
            return new ResponseEntity(book.getDetailBook(), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/books/{isbn}")
    public ResponseEntity modifyBook(@RequestHeader HttpHeaders headers, @PathVariable String isbn, @RequestBody Book newBook) {
        int userId = Application.getAuthorization(headers, sessionRepository);
        if (userId <= 0) {
            return new ResponseEntity(TXT_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        Optional<Book> optBook = bookRepository.findById(isbn);
        if(optBook.isPresent()){
            Book book = optBook.get();
            if(!book.isDelete()){
                book.updateBook(newBook);
                if(book.getTitle() == null){
                    return new ResponseEntity("Le champ titre doit être complété.",HttpStatus.UNPROCESSABLE_ENTITY);
                } else if(book.getAuthor() == null){
                    return new ResponseEntity("le champ auteur doit être complété.",HttpStatus.UNPROCESSABLE_ENTITY);
                }
                try{
                    bookRepository.save(book);
                    return new ResponseEntity(book.getDetailBook(), HttpStatus.OK);
                } catch(Exception ex){
                    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        String message = String.format("Ce livre d'ISBN %s n'existe pas.", isbn);
        return new ResponseEntity(message,HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/books/{isbn}")
    public ResponseEntity<String> deleteBook(@RequestHeader HttpHeaders headers, @PathVariable String isbn) {
        int userId = Application.getAuthorization(headers, sessionRepository);
        if (userId <= 0) {
            return new ResponseEntity<>(TXT_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        Optional<Book> optBook = bookRepository.findById(isbn);
        if(optBook.isPresent()){
            Book book = optBook.get();
            if(!book.isDelete()){
                book.deleteBook(userId, userRepository);
                bookRepository.save(book);
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        String message = String.format("Le livre d'ISBN %s n'éxiste pas.", isbn);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

}
