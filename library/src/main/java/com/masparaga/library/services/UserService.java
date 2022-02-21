package com.masparaga.library.services;

import com.masparaga.library.model.Role;
import com.masparaga.library.model.User;
import com.masparaga.library.model.dto.UserView;
import com.masparaga.library.model.requests.AuthRequest;
import com.masparaga.library.model.requests.CreateUserRequest;
import com.masparaga.library.model.requests.UpdateUserRequest;
import com.masparaga.library.model.responses.MessageResponse;
import com.masparaga.library.model.responses.UserInfoResponse;
import com.masparaga.library.repository.UserRepository;
import com.masparaga.library.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<?> login(AuthRequest request){
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = (User) authenticate.getPrincipal();

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
                    .body(new UserInfoResponse(user.getId(), user.getUsername()));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Transactional
    public User create(CreateUserRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new ValidationException("Username exists");
        }
        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        HashSet<Role> authorities = new HashSet<Role>();
        authorities.add(new Role(Role.USER));
        user.setAuthorities(authorities);
        return userRepository.save(user);
    }

    @Transactional
    public MessageResponse update(ObjectId id, UpdateUserRequest request){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setAuthorities(request.getAuthorities());
            userRepository.save(user.get());
            return new MessageResponse("user updated successfully");
        } else{
            return new MessageResponse("couldnt find the student");
        }
    }

    @Transactional
    public MessageResponse delete(ObjectId id){
        Optional<User> user = userRepository.findById(id);
        userRepository.delete(user.get());
        return new MessageResponse("deleted successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("user couldnt found")
        );
    }

    @Transactional
    public Optional<User> getUser(ObjectId id){
        return userRepository.findById(id);
    }
}
