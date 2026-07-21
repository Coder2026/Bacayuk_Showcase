
package com.sharesphere.usermanagement.application;

import com.google.firebase.auth.FirebaseAuth;
import org.springframework.stereotype.Service;

@Service
public class GenerateFirebaseTokenUseCase {

    public String execute(String userId) {
        try {
            String token = FirebaseAuth.getInstance().createCustomToken(userId);
            System.out.println("Firebase custom token generated for UID: " + userId);
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate custom token", e);
        }
    }
}
