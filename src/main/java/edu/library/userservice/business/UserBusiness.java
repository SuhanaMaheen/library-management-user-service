package edu.library.userservice.business;

import edu.library.userservice.model.User;

import java.util.Optional;

public interface UserBusiness {
    void registerUser(User user);
    void validateUserIdAndEmail(int userId, String email) throws Exception;
    Optional<User> getUser(String userId) throws Exception;
    User updateUser(String userId, String name, String phoneNumber, String encPassword, String photoPath) throws Exception;

}
