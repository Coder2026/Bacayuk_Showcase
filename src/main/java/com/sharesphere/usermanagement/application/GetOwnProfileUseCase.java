package com.sharesphere.usermanagement.application;

import com.sharesphere.share.service.S3Service;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import com.sharesphere.usermanagement.dto.PrivateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetOwnProfileUseCase {
    private final UserRepository userRepository;
    private final S3Service s3Service;
    public PrivateUserResponse execute(String userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        PrivateUserResponse.Location location = Optional.ofNullable(user.getLocation())
                .map(l -> new PrivateUserResponse.Location(l.getX(), l.getY()))
                .orElse(null);

        
        return new PrivateUserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            s3Service.getPublicUrlFromKey(user.getPhotoKey()),
                location
        );
    }
}