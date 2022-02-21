package com.masparaga.library.model.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class EditBookRequest {
    @NotNull
    private String name;
    private String author;
    private boolean isTaken;
}
