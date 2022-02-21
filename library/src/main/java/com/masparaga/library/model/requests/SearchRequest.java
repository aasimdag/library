package com.masparaga.library.model.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SearchRequest<T> {

    public SearchRequest(T query) {
        this.query = query;
    }

    @Valid
    @NotNull
    private T query;
}
