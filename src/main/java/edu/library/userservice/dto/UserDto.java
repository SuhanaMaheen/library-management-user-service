package edu.library.userservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private int userId;

    @NotBlank
    @NotEmpty(message = "Name cannot be empty")
    @NotBlank
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Email
    @NotBlank
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Phone Number cannot be empty")
    private String phoneNumber;

    @JsonIgnore
    private String photoPath;

    @NotBlank
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private String updatedBy;

    @JsonIgnore
    private LocalDateTime updatedAt;

}
