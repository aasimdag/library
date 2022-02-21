package com.masparaga.library.services;

import com.masparaga.library.model.Book;
import com.masparaga.library.model.dto.SearchBookQuery;
import com.masparaga.library.model.requests.EditBookRequest;
import com.masparaga.library.model.requests.SearchRequest;
import com.masparaga.library.model.responses.MessageResponse;
import com.masparaga.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public MessageResponse create(EditBookRequest request) {
        Book book = new Book(request.getName(), request.getAuthor());
        bookRepository.save(book);
        return new MessageResponse("created book");
    }

    @Transactional
    public MessageResponse update(ObjectId id, EditBookRequest request) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            bookRepository.save(book.get());
            return new MessageResponse("updated book");
        } else{
            return new MessageResponse("Couldnt find book");
        }
    }

    @Transactional
    public MessageResponse delete(ObjectId id) {
        Book book = bookRepository.findById(id).get();

        bookRepository.delete(book);

        return new MessageResponse("deleted book");
    }

    @Transactional
    public Book getBook(ObjectId id) {
        return bookRepository.findById(id).get();
    }

    public MessageResponse searchBooks(SearchBookQuery query){
        return new MessageResponse(bookRepository.searchBooks(query).toString());
    }

}
