package edu.library.userservice.validation;

import edu.library.userservice.business.UserBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RequestValidator {
    @Autowired
    private UserBusiness userBusiness;

    public void validateUserIdAndEmail(int userId,String email ) throws Exception {
            userBusiness.validateUserIdAndEmail(userId,email);
            System.out.println("inside validate try");

    }
}
