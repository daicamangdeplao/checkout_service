package org.codenot.cs.entity;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderedItem(
        String name,
        BigDecimal orderedPrice,
        Integer quantity
) {
}
