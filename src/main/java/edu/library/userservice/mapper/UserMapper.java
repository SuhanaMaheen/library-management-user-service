package edu.library.userservice.mapper;

import edu.library.userservice.dto.UserDto;
import edu.library.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User toEntity(UserDto userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole("Student");
        user.setCreatedBy(user.getName());
        user.setUpdatedBy(user.getName());
        return user;
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setPhotoPath(user.getPhotoPath());
        dto.setPassword(user.getPassword());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedBy(user.getUpdatedBy());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }


}
