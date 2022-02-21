package com.masparaga.library.api;

import com.masparaga.library.controller.RedisController;
import com.masparaga.library.model.Role;
import com.masparaga.library.model.User;
import com.masparaga.library.model.requests.AuthRequest;
import com.masparaga.library.model.requests.CreateUserRequest;
import com.masparaga.library.model.responses.MessageResponse;
import com.masparaga.library.model.responses.UserInfoResponse;
import com.masparaga.library.security.JwtTokenUtil;
import com.masparaga.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/home")
@RequiredArgsConstructor
public class AuthApi {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request){
        return userService.login(request);
    }

    @PostMapping("/register")
    public User register(@RequestBody @Valid CreateUserRequest request){
        return userService.create(request);
    }
}
