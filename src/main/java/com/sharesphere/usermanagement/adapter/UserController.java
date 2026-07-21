package com.sharesphere.usermanagement.adapter;


import com.sharesphere.usermanagement.application.*;
import com.sharesphere.share.dto.Response;
import com.sharesphere.usermanagement.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final SetLocationUseCase setLocationUseCase;
    private final SetProfilePhotoUseCase setProfilePhotoUseCase;
    private final GetOwnProfileUseCase getOwnProfileUseCase;
    private final GetPublicProfileUseCase getPublicProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final GenerateFirebaseTokenUseCase generateFirebaseTokenUseCase;


    @PatchMapping("me/location")
    public ResponseEntity<Response<String>> setLocation(
            @RequestBody @Valid LocationRequest locationRequest,
            @AuthenticationPrincipal UserDetails currentUser) {

        setLocationUseCase.execute(currentUser.getUsername(), locationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Lokasi berhasil diperbarui",null));
    }

    @PatchMapping("me/profile-photo")
    public ResponseEntity<Response<String>> setProfilePhoto(
            @RequestBody PhotoKey photoKey,
            @AuthenticationPrincipal UserDetails currentUser) {

        setProfilePhotoUseCase.execute(
                currentUser.getUsername(),
                photoKey
        );

        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Photo profile berhasil diperbarui", null));
    }

    @PatchMapping("me/profile")
    public ResponseEntity<Response<PrivateUserResponse>> updateProfile(@AuthenticationPrincipal UserDetails currentUser, @RequestBody @Valid UpdateProfileRequest updateProfileRequest) {

        PrivateUserResponse privateUserResponse = updateProfileUseCase.execute(currentUser.getUsername(), updateProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Profile berhasil diperbarui", privateUserResponse));
    }


    @GetMapping("/firebase-token")
    public ResponseEntity<?> getFirebaseToken(@AuthenticationPrincipal UserDetails currentUser) {

        String token = generateFirebaseTokenUseCase.execute(currentUser.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Firebase token berhasil dibuat", token));
    }


    @GetMapping("me")
    public ResponseEntity<Response<PrivateUserResponse>> getUserProfile(
            @AuthenticationPrincipal UserDetails currentUser) {

        PrivateUserResponse privateUserResponse = getOwnProfileUseCase.execute(currentUser.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Profile berhasil diambil", privateUserResponse));
    }

    @GetMapping("{userId}")
    public ResponseEntity<Response<PublicUserResponse>> getUserProfileById(
            @PathVariable String userId) {
        PublicUserResponse publicUserResponse = getPublicProfileUseCase.execute(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new Response<>("Profile berhasil diambil", publicUserResponse));
    }
}