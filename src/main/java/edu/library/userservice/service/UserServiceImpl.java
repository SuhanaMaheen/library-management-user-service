package edu.library.userservice.service;

import edu.library.userservice.business.UserBusiness;
import edu.library.userservice.dto.UserDto;
import edu.library.userservice.model.User;
import edu.library.userservice.mapper.UserMapper;
import edu.library.userservice.util.FileUploadService;
import edu.library.userservice.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserBusiness userBusiness;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private RequestValidator requestValidator;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDto request, MultipartFile photo) throws Exception {
        String photoPath = null;
        try {
            requestValidator.validateUserIdAndEmail(request.getUserId(), request.getEmail());
            User user = userMapper.toEntity(request);
            photoPath = fileUploadService.savePhoto(photo);
            user.setPhotoPath(photoPath);
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            userBusiness.registerUser(user);
        } catch (Exception e) {
            // In case of error, clean up the photo if it was saved
            if (photoPath != null) {
                try {
                    fileUploadService.deletePhoto(photoPath);
                } catch (Exception deleteException) {
                    throw new Exception(deleteException);
                }
            }
            throw e;

        }
    }

    @Override
    public UserDto getUser(String userId) throws Exception {
        Optional<User> user = userBusiness.getUser(userId);
        if (user.isPresent()) {
            return userMapper.toUserDto(user.get());
        } else {
            throw new Exception("User not found");
        }

    }

    @Override
    public Resource downloadPhoto(String userId) throws Exception {
        UserDto user = this.getUser(userId);
        return fileUploadService.downloadPhoto(user.getPhotoPath());
    }

    @Override
    public UserDto login(String userId, String password) throws Exception {
        UserDto user = this.getUser(userId);
        if (user == null) {
            throw new Exception("User Id not found");
        }
        if (!(passwordEncoder.matches(password, user.getPassword()))) {
            throw new Exception("Password Incorrect");
        }
        return user;
    }

    @Override
    public UserDto updateUser(String userId, String name, String phoneNumber, String password, MultipartFile photo) throws Exception {
        String encPassword = null;
        String photoPath = null;
        if(name==null&&phoneNumber==null&&password==null&&photo==null){
            throw new Exception("Atleast one field is required to update user");
        }
        if (password != null) {
            encPassword = passwordEncoder.encode(password);
        }
        if (photo != null) {
            photoPath = fileUploadService.savePhoto(photo);
        }
        User user = userBusiness.updateUser(userId, name, phoneNumber, encPassword, photoPath);
        return userMapper.toUserDto(user);
    }


}
