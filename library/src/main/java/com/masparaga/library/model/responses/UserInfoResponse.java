package com.masparaga.library.model.responses;

import java.util.List;

public class UserInfoResponse {
    private String id;
    private String username;
    public UserInfoResponse(String id, String username) {
        this.id=id;
        this.username=username;

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
