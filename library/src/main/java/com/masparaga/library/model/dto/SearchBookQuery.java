package com.masparaga.library.model.dto;

import lombok.Data;

@Data
public class SearchBookQuery {
    private String id;
    private String name;
    private String author;
    private String isTaken;
}
