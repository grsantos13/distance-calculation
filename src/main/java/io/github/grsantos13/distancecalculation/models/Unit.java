package io.github.grsantos13.distancecalculation.models;

import java.math.BigDecimal;

public enum Unit {
    M(1000.0),
    MI(0.62137),
    KM(1.0);

    private final BigDecimal conversionValue;

    Unit(Double conversionValue) {
        this.conversionValue = BigDecimal.valueOf(conversionValue);
    }

    public BigDecimal getConversionValue() {
        return conversionValue;
    }

    public BigDecimal convert(BigDecimal value) {
        return value.multiply(this.getConversionValue());
    }
}
