package org.codenot.cs.entity;

import lombok.Builder;
import org.codenot.cs.service.discount.domain.PricingStrategy;

import java.math.BigDecimal;

@Builder
public record PriceTier(
        Integer minQuantity,          // Minimum quantity for this tier
        PricingStrategy strategy,     // How to calculate the price
        BigDecimal value              // The price value (meaning depends on strategy)
) {
}
