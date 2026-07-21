package com.sharesphere.post.adapter;

import com.sharesphere.post.domain.PostStatus;
import com.sharesphere.post.dto.NearbyPostItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public enum NearbyPostRowMapper implements RowMapper<NearbyPostItem> {

    INSTANCE;
    @Override
    public NearbyPostItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NearbyPostItem(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("photo_key"),
                rs.getBigDecimal("daily_rent_fee"),
                rs.getDouble("distance_km"),
                Enum.valueOf(PostStatus.class, rs.getString("status"))
        );
    }
}
