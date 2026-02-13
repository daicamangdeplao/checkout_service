package org.codenot.cs.service.discount;

import lombok.extern.slf4j.Slf4j;
import org.codenot.cs.entity.OrderedItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class DefaultDiscountService {

    private final List<DiscountConfiguration> discountConfigurations = List.of(
            new DiscountConfiguration(2, 10.0),
            new DiscountConfiguration(3, 12.0)
    );

    public Optional<OrderedItem> apply(OrderedItem item) {
        DiscountConfiguration discountConfiguration = discountConfigurations.get(ThreadLocalRandom.current().nextInt(discountConfigurations.size()));

        if (discountConfiguration == null) {
            return Optional.empty();
        }

        if (item.quantity() < discountConfiguration.quantityThreshold()) {
            return Optional.of(item);
        }

        BigDecimal percent = BigDecimal.valueOf(discountConfiguration.discountPercent()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal discountedPrice = item.orderedPrice().multiply(percent);
        return Optional.of(OrderedItem.builder()
                .name(item.name())
                .quantity(item.quantity())
                .orderedPrice(discountedPrice)
                .build());
    }

    record DiscountConfiguration(
            Integer quantityThreshold,
            Double discountPercent
    ) {

    }
}
