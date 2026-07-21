package com.sharesphere.usermanagement.application;

import com.sharesphere.share.dto.Response;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import com.sharesphere.usermanagement.dto.PhotoKey;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SetProfilePhotoUseCase {


    private  final UserRepository userRepository;

    public void execute(String userId, PhotoKey photoKey){

        User user = userRepository.getReferenceById(userId);
        user.setPhotoKey(photoKey.photoKey());
        userRepository.save(user);
    }
}
