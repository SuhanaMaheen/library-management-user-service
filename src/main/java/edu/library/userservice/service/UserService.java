package edu.library.userservice.service;

import edu.library.userservice.dto.UserDto;
import edu.library.userservice.model.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void registerUser(UserDto request, MultipartFile photo) throws Exception;
    UserDto getUser(String userId) throws Exception;
    Resource downloadPhoto(String userId) throws Exception;
    UserDto login(String userId,String password) throws Exception;
    UserDto updateUser(String userId,String name,String phoneNumber,String password,MultipartFile photo) throws Exception;

}
