package io.github.grsantos13.distancecalculation.resources;

import io.github.grsantos13.distancecalculation.models.DistanceRespose;
import io.github.grsantos13.distancecalculation.models.GeographicLocation;
import io.github.grsantos13.distancecalculation.models.Unit;
import org.apache.commons.math3.util.FastMath;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
public class DistanceResource {

    private static final double EARTH_RADIUS = 6378.0;

    @GetMapping("/distances")
    public DistanceRespose distance(
            @RequestParam("latitudeStart") BigDecimal latitudeStart,
            @RequestParam("longitudeStart") BigDecimal longitudeStart,
            @RequestParam("latitudeEnd") BigDecimal latitudeEnd,
            @RequestParam("longitudeEnd") BigDecimal longitudeEnd,
            @RequestParam(value = "unit", required = false, defaultValue = "KM") Unit unit
    ) {
        var start = new GeographicLocation(latitudeStart, longitudeStart);
        var end = new GeographicLocation(latitudeEnd, longitudeEnd);

        var deltaLatitude = Math.toRadians(end.latitude().subtract(start.latitude()).doubleValue());
        var deltaLongitude = Math.toRadians(end.longitude().subtract(start.longitude()).doubleValue());
        var latitudeStartRadians = Math.toRadians(start.latitude().doubleValue());
        var latitudeEndRadians = Math.toRadians(end.latitude().doubleValue());

        // Haversine formula
        var a = Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(latitudeStartRadians) * Math.cos(latitudeEndRadians)
                * Math.pow(Math.sin(deltaLongitude / 2), 2);
        var c = 2 * FastMath.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = BigDecimal.valueOf(EARTH_RADIUS * c);

        // business rule to round always up
        var convertedDistance = unit.convert(d)
                .setScale(0, RoundingMode.CEILING);
        return new DistanceRespose(convertedDistance, unit);
    }
}
