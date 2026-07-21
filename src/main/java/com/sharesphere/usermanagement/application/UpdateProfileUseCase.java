package com.sharesphere.usermanagement.application;


import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import com.sharesphere.usermanagement.dto.PrivateUserResponse;
import com.sharesphere.usermanagement.dto.UpdateProfileRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProfileUseCase {

    private final UserRepository userRepository;

    public PrivateUserResponse execute(String userId, UpdateProfileRequest updateProfileRequest){

        User user = userRepository.getReferenceById(userId);
        user.setName(updateProfileRequest.name());

        if (updateProfileRequest.hasPhotoKey()) {
            user.setPhotoKey(updateProfileRequest.getProcessedPhotoKey());
        }

        User updatedUser = userRepository.save(user);

        PrivateUserResponse.Location location = Optional.ofNullable(updatedUser.getLocation())
                .map(l -> new PrivateUserResponse.Location(l.getX(), l.getY()))
                .orElse(null);


        return new PrivateUserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhotoKey(),
                location
        );
    }
}
