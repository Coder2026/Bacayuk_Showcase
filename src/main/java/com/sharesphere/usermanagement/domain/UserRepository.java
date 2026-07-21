package com.sharesphere.usermanagement.domain;


import org.locationtech.jts.geom.Point;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findById(String userId);
    Optional<User> findByEmail(String email);
    User getReferenceById(String userId);
    Optional<Point> findLocationByUserId(String userId);
}
