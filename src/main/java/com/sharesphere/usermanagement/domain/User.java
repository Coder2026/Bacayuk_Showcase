package com.sharesphere.usermanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users",schema = "public")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    @Column(name = "photo_key")
    private String photoKey;
    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;
    boolean emailVerified;
    @Enumerated(EnumType.STRING)
    private Role role;
}


