package edu.library.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse<T>{

    private String status;
    private String message;
    private T data;

    public UserResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
