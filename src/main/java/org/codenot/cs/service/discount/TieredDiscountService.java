package org.codenot.cs.service.discount;

import lombok.extern.slf4j.Slf4j;
import org.codenot.cs.entity.OrderedItem;
import org.codenot.cs.repository.TieredDiscountConfigRepository;
import org.codenot.cs.entity.PriceTier;
import org.codenot.cs.entity.TieredDiscountConfig;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Optional;

@Service
@Slf4j
public class TieredDiscountService implements DiscountService {

    private final TieredDiscountConfigRepository tieredDiscountConfigRepository;

    public TieredDiscountService() {
        this.tieredDiscountConfigRepository = new TieredDiscountConfigRepository();
    }

    public Boolean supports(String discountLogic) {
        return discountLogic.equals("tiered");
    }

    @Override
    public Optional<OrderedItem> apply(OrderedItem item) {
        Optional<TieredDiscountConfig> discountConfig = tieredDiscountConfigRepository.findByItemName(item.name());

        if (discountConfig.isEmpty()) {
            return Optional.empty();
        }

        // On which tier is the item?
        PriceTier appliedTier = discountConfig.get().tiers().stream()
                .filter(tier -> tier.minQuantity() <= item.quantity())
                .max(Comparator.comparing(PriceTier::minQuantity))
                .orElse(null);

        if (appliedTier == null) {
            return Optional.empty();
        }

        return switch (appliedTier.strategy()) {
            case FIXED_PER_ITEM -> Optional.of(OrderedItem.builder()
                    .name(item.name())
                    .quantity(item.quantity())
                    .orderedPrice(item.orderedPrice().multiply(appliedTier.value()))
                    .build());
            case FIXED_TOTAL -> {
                BigDecimal pricePerItem = appliedTier.value().divide(BigDecimal.valueOf(item.quantity()), 2, RoundingMode.HALF_UP);
                BigDecimal discountedPrice = pricePerItem.multiply(BigDecimal.valueOf(item.quantity()));
                yield Optional.of(OrderedItem.builder()
                        .name(item.name())
                        .quantity(item.quantity())
                        .orderedPrice(discountedPrice)
                        .build());
            }
            case PERCENTAGE_OFF -> {
                BigDecimal percentage = appliedTier.value().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                BigDecimal saveOffValue = item.orderedPrice().multiply(percentage);
                BigDecimal discountedPrice = item.orderedPrice().subtract(saveOffValue);
                yield Optional.of(OrderedItem.builder()
                        .name(item.name())
                        .quantity(item.quantity())
                        .orderedPrice(discountedPrice.setScale(2, RoundingMode.HALF_UP))
                        .build());
            }
        };
    }
}
