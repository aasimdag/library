package com.masparaga.library.model.requests;

import com.masparaga.library.model.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;

@Data
public class UpdateUserRequest {
    @NotBlank
    private String username;
    private HashSet<Role> authorities;
}
