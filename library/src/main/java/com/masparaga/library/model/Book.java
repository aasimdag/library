package com.masparaga.library.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "books")
@Data
public class Book implements Serializable {
    @Id
    private ObjectId id;
    private String name;
    private String author;
    private boolean isTaken;

    public Book(){}
    public Book(String name, String author){
        this.name=name;
        this.author = author;
        isTaken=false;
    }

}
