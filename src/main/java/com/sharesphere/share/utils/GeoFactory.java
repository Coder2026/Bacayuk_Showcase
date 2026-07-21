package com.sharesphere.share.utils;


import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public final class GeoFactory {
    public static final GeometryFactory INSTANCE =
            new GeometryFactory(new PrecisionModel(), 4326);

    private GeoFactory() {

    }
}
