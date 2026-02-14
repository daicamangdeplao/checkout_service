package org.codenot.cs.repository;

import org.codenot.cs.service.discount.domain.PriceTier;
import org.codenot.cs.service.discount.domain.TieredDiscountConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.codenot.cs.service.discount.domain.PricingStrategy.*;

public class TieredDiscountConfigRepository {
    /**
     * Predefined tiered discount configurations for common items
     */
    public static final List<TieredDiscountConfig> DEFAULT_CONFIGS = List.of(
            // Apple: 1 apple = €0.30, 2 apples = €0.45 total, 5 apples = €1.00 total
            new TieredDiscountConfig(
                    "apple",
                    List.of(
                            PriceTier.builder()
                                    .minQuantity(1)
                                    .strategy(FIXED_PER_ITEM)
                                    .value(new BigDecimal("0.30"))
                                    .build(),
                            PriceTier.builder()
                                    .minQuantity(2)
                                    .strategy(FIXED_TOTAL)
                                    .value(new BigDecimal("0.45"))
                                    .build(),
                            PriceTier.builder()
                                    .minQuantity(5)
                                    .strategy(FIXED_TOTAL)
                                    .value(new BigDecimal("1.00"))
                                    .build()
                    ),
                    false
            ),

            // Orange: Volume discount with decreasing per-item price
            // 1-2 oranges = €0.50 each, 3-5 oranges = €0.40 each, 6+ oranges = €0.35 each
            new TieredDiscountConfig(
                    "orange",
                    List.of(
                            PriceTier.builder()
                                    .minQuantity(1)
                                    .strategy(FIXED_PER_ITEM)
                                    .value(new BigDecimal("0.50"))
                                    .build(),
                            PriceTier.builder()
                                    .minQuantity(3)
                                    .strategy(FIXED_PER_ITEM)
                                    .value(new BigDecimal("0.40"))
                                    .build(),
                            PriceTier.builder()
                                    .minQuantity(6)
                                    .strategy(FIXED_PER_ITEM)
                                    .value(new BigDecimal("0.35"))
                                    .build()
                    ),
                    false
            ),

            // Banana: Percentage-based discount tiers
            // 1-2 bananas = full price, 3-4 bananas = 10% off, 5+ bananas = 20% off
            new TieredDiscountConfig(
                    "banana",
                    List.of(
                            PriceTier.builder()
                                    .minQuantity(1)
                                    .strategy(PERCENTAGE_OFF)
                                    .value(BigDecimal.ZERO)
                                    .build(),
                            PriceTier.builder()
                                    .minQuantity(3)
                                    .strategy(PERCENTAGE_OFF)
                                    .value(new BigDecimal("10"))
                                    .build(),
                            PriceTier.builder()
                                    .minQuantity(5)
                                    .strategy(PERCENTAGE_OFF)
                                    .value(new BigDecimal("20"))
                                    .build()
                    ),
                    false
            )
    );

    public Optional<TieredDiscountConfig> findByItemName(String itemName) {
        return DEFAULT_CONFIGS.stream()
                .filter(config -> config.itemName().equals(itemName))
                .findFirst();
    }
}
