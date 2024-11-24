package io.github.grsantos13.distancecalculation.models;

import java.math.BigDecimal;

public record DistanceRespose(
        BigDecimal distance,
        Unit unit
) {
}
