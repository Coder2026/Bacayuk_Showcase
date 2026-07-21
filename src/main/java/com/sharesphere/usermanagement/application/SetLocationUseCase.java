package com.sharesphere.usermanagement.application;

import com.sharesphere.share.dto.Response;
import com.sharesphere.share.utils.GeoFactory;
import com.sharesphere.usermanagement.domain.User;
import com.sharesphere.usermanagement.domain.UserRepository;
import com.sharesphere.usermanagement.dto.LocationRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class SetLocationUseCase {
    final private UserRepository userRepository;

    @CacheEvict(value = "user-locations", key = "#userId")
    public void execute(String userId, LocationRequest locationRequest){

        Point location = GeoFactory.INSTANCE.createPoint(new Coordinate(locationRequest.longitude(),locationRequest.latitude()));
        User user = userRepository.getReferenceById(userId);
        user.setLocation(location);
        userRepository.save(user);
    }
}
