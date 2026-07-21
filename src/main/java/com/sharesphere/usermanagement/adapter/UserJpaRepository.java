package com.sharesphere.usermanagement.adapter;
import com.sharesphere.usermanagement.domain.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,String> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    @Query("SELECT u.location FROM User u WHERE u.id = :userId")
    Optional<Point> findLocationByUserId(@Param("userId") String userId);

}
