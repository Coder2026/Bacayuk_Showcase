package com.sharesphere.share.security;


import com.sharesphere.usermanagement.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {

        return userRepo.findById(userId)
                .map(UserPrincipal::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User %s not found".formatted(userId)));
    }
}
