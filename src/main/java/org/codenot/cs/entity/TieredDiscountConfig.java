package org.codenot.cs.entity;

import lombok.Builder;

import java.util.List;

@Builder
public record TieredDiscountConfig(
        String itemName,              // Which item this applies to
        List<PriceTier> tiers,        // Ordered list of price tiers (sorted by minQuantity)
        boolean cumulative            // false = tier-based, true = cumulative calculation
) {
}
