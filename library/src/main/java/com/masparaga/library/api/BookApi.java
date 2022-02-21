package com.masparaga.library.api;

import com.masparaga.library.controller.RedisController;
import com.masparaga.library.model.Book;
import com.masparaga.library.model.dto.SearchBookQuery;
import com.masparaga.library.model.requests.BuyingStepsRequest;
import com.masparaga.library.model.requests.EditBookRequest;
import com.masparaga.library.model.requests.SearchRequest;
import com.masparaga.library.model.responses.MessageResponse;
import com.masparaga.library.services.BookService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api/home/shop")
@RequiredArgsConstructor
public class BookApi {
    private final BookService bookService;
    private final RedisController redisController;

    @PostMapping("/search")
    public MessageResponse searchByName(@RequestBody SearchRequest<SearchBookQuery> request){
        return bookService.searchBooks(request.getQuery());
    }

    @PostMapping("/buy/{id}")
    public MessageResponse buy(@PathVariable String id, @RequestBody BuyingStepsRequest buyingStepsRequest, Model model, HttpServletRequest request, HttpSession session){
        Book bookToBuy = bookService.getBook(new ObjectId(id));
        if(bookToBuy.isTaken()){
            return new MessageResponse("book is taken");
        }
        if(redisController.buyStep(buyingStepsRequest.getAddress(), buyingStepsRequest.getCardInfos(), request, model, session)){
            EditBookRequest editBookRequest = new EditBookRequest();
            editBookRequest.setName(bookToBuy.getName());
            editBookRequest.setAuthor(bookToBuy.getAuthor());
            editBookRequest.setTaken(true);
            bookService.update(bookToBuy.getId(), editBookRequest);
            return new MessageResponse("buy stage for :" + bookToBuy.getName() + " is completed");
        }

        else{
            return new MessageResponse("some steps are missing");
        }

    }
}
