package com.masparaga.library.api;

import com.masparaga.library.model.Role;
import com.masparaga.library.model.User;
import com.masparaga.library.model.requests.CreateUserRequest;
import com.masparaga.library.model.requests.EditBookRequest;
import com.masparaga.library.model.requests.UpdateUserRequest;
import com.masparaga.library.model.responses.MessageResponse;
import com.masparaga.library.services.BookService;
import com.masparaga.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@RestController
@RequestMapping("api/admin")
@RolesAllowed(Role.ADMIN)
@RequiredArgsConstructor
public class AdminApi {
    private final UserService userService;

    @PostMapping
    public User create(CreateUserRequest request){
        return userService.create(request);
    }
    @PutMapping("{id}")
    public MessageResponse update(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        return userService.update(new ObjectId(id), request);
    }

    @DeleteMapping("{id}")
    public MessageResponse delete(@PathVariable String id) {
        return userService.delete(new ObjectId(id));
    }

    @GetMapping("{id}")
    public MessageResponse get(@PathVariable String id) {
        Optional<User> _user = userService.getUser(new ObjectId(id));
        if(_user.isPresent()){
            User user = _user.get();
            return new MessageResponse(user.getUsername());
        } else{
            return new MessageResponse("user couldnt found");
        }
    }
}
