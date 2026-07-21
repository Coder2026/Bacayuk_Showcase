package com.sharesphere.usermanagement.application;

import com.sharesphere.share.service.S3Service;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import com.sharesphere.usermanagement.dto.PublicUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPublicProfileUseCase {
    private final UserRepository userRepository;
    private  final S3Service s3Service;

    public PublicUserResponse execute(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        return new PublicUserResponse(
            user.getId(),
            user.getName(),
            s3Service.getPublicUrlFromKey(user.getPhotoKey())
        );
    }
}