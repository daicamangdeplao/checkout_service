package org.codenot.cs.service.discount.domain;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PriceTier(
        Integer minQuantity,          // Minimum quantity for this tier
        PricingStrategy strategy,     // How to calculate the price
        BigDecimal value              // The price value (meaning depends on strategy)
) {
}
