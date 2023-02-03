package mvc.controller;

import mvc.entity.BookEntity;
import mvc.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api")
public class BookController {
    @Autowired
    BookRepository bookRepository;
    @RequestMapping(method = GET)
    public List<BookEntity> showBooks(){

        return (List<BookEntity>) bookRepository.findAll();
    }
    @PostMapping
    public Object addBook(@RequestBody BookEntity newBook) {
        BookEntity result = bookRepository.save(newBook);
        return result;
    }

    @PutMapping
    public Object updateBook(@RequestBody BookEntity updateBook) {
        BookEntity result = bookRepository.findById(updateBook.getId()).get();
        if (result == null) {
            Map<String, String> error = new HashMap<String, String>() {
                {
                    put("Error", updateBook.getId() + " not exist");
                }
            };
            return error;
        }
        result = updateBook;
        bookRepository.save(result);
        return result;
    }

    @DeleteMapping(value = "/{id}")
    public Object deleteBook(@PathVariable(value = "id") int id) {
        BookEntity foundBook = null;
        for(BookEntity bookEntity: bookRepository.findAll()) {
            if (bookEntity.getId() == id) {
                foundBook = bookEntity;
                break;
            }
        }
        if (foundBook != null) {
            bookRepository.deleteById(foundBook.getId());
            Map<String, String> success = new HashMap<String, String>() {{
                put("success", "A Book Which ID =" + id + " has been deleted successfully");
            }};
            return success;
        } else {
            Map<String, String> error = new HashMap<String, String>() {{
                put("error", "A Book which ID = " + id + " does not exist");
            }};
            return error;
        }
    }
}
