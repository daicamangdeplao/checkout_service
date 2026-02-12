package org.codenot.cs.entity;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Item(
        String name,
        BigDecimal basePrice,
        String desc
) {
}
