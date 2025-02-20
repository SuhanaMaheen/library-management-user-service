package edu.library.userservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse<T>{

    private String status;
    private String message;
    private T data;

    public UserResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

}
