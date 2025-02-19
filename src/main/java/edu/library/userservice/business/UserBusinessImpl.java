package edu.library.userservice.business;

import edu.library.userservice.model.User;
import edu.library.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserBusinessImpl implements UserBusiness {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void validateUserIdAndEmail(int userId, String email) throws Exception {
        Optional<User> userExist = userRepository.findUsersByUserId(userId);
        if (userExist.isPresent()) {
            throw new Exception("UserId already exists");
        }
        Optional<User> emailExist = userRepository.findUsersByEmail(email);
        if (emailExist.isPresent()) {
            throw new Exception("Email already exists");
        }
    }

    @Override
    public Optional<User> getUser(String userId) throws Exception {
        Optional<User> user = userRepository.findUsersByUserId(Integer.parseInt(userId));
        return user;
    }

    @Override
    public User updateUser(String userId, String name, String phoneNumber, String encPassword, String photoPath) throws Exception {
        return userRepository.findById(Long.valueOf(userId)).map(existingUser -> {
            if (name != null) {
                existingUser.setName(name);
                existingUser.setUpdatedBy(name);
            }
            if (phoneNumber != null) {
                existingUser.setPhoneNumber(phoneNumber);
            }
            if (encPassword != null) {
                existingUser.setPassword(encPassword);
            }
            if (photoPath != null) {
                existingUser.setPhotoPath(photoPath);
            }
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));

    }
}
