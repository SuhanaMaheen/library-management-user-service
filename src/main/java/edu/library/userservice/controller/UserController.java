package edu.library.userservice.controller;

import edu.library.userservice.dto.UserDto;
import edu.library.userservice.dto.UserResponse;
import edu.library.userservice.model.User;
import edu.library.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

import static org.springframework.http.MediaType.*;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user and stores their information in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/register", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser(
            @Valid @RequestPart("User") UserDto request,
            @Parameter(description = "User Image", required = true, content = @Content(mediaType = "multipart/form-data"))
            @RequestPart("photo") MultipartFile photo) throws Exception {
        try {
            userService.registerUser(request, photo);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Login", description = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse<UserDto>> userLogin(
            @Parameter(description = "User Id", required = true)
            @RequestParam("userId") String userId,
            @Parameter(description = "Password", required = true)
            @RequestParam("password") String password) throws Exception {
        UserDto user = null;
        try {
            user = userService.login(userId, password);
            return ResponseEntity.ok(new UserResponse<>(HttpStatus.OK.name(), "User login successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserResponse<>(HttpStatus.NOT_FOUND.name(), e.getMessage(), user));
        }
    }

    @Operation(summary = "View User Details", description = "View the User Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data Fetched Successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/getUser", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse<UserDto>> getUser(
            @Parameter(description = "User Id", required = true)
            @RequestParam("userId") String userId) throws Exception {
        UserDto user = null;
        try {
            user = userService.getUser(userId);
            return ResponseEntity.ok(new UserResponse<>(HttpStatus.FOUND.name(), "User fetched successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserResponse<>(HttpStatus.NOT_FOUND.name(), e.getMessage(), user));
        }
    }

    @Operation(summary = "Update User", description = "Update the User Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/updateUser/{userId}",consumes = MULTIPART_FORM_DATA_VALUE,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse<UserDto>> updateUser(
            @PathVariable String userId,
            @Parameter(description = "Name")
            @RequestParam(value = "name",required = false) String name,
            @Parameter(description = "phoneNumber")
            @RequestParam(value = "phoneNumber",required = false) String phoneNumber,
            @Parameter(description = "photo")
            @RequestPart(value = "photo",required = false) MultipartFile photo,
            @Parameter(description = "password")
            @RequestParam(value = "password",required = false) String password
            ) throws Exception {

        UserDto user = null;
        try {
            user = userService.updateUser(userId, name, phoneNumber, password, photo);
            return ResponseEntity.ok(new UserResponse<>(HttpStatus.OK.name(), "User Updated successfully", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserResponse<>(HttpStatus.NOT_FOUND.name(), e.getMessage(), user));
        }
    }

    @Operation(summary = "Download Profile Image of User", description = "Profile Image of User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image downloaded successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/downloadPhoto/{userId}")
    public ResponseEntity<UserResponse<UserDto>> downloadPhoto(@PathVariable String userId, HttpServletResponse response) throws FileNotFoundException, Exception {
        try {
            UserDto user = userService.getUser(userId);
            InputStream inputStream = new FileInputStream(Objects.requireNonNull(user.getPhotoPath()));
            response.setContentType(String.valueOf(MediaType.APPLICATION_OCTET_STREAM));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + user.getPhotoPath() + "\"");
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserResponse<>(HttpStatus.NOT_FOUND.name(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserResponse<>(HttpStatus.NOT_FOUND.name(), e.getMessage(), null));
        }
        return null;
    }


}



