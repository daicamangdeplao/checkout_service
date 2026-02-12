package org.codenot.cs.service;

import lombok.extern.slf4j.Slf4j;
import org.codenot.cs.entity.OrderedItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class DiscountService {

    private final List<DiscountConfiguration> discountConfigurations = List.of(
            new DiscountConfiguration(2, 10.0),
            new DiscountConfiguration(3, 12.0)
    );

    public OrderedItem apply(OrderedItem item) {
        DiscountConfiguration discountConfiguration = discountConfigurations.get(ThreadLocalRandom.current().nextInt(discountConfigurations.size()));
        if (item.quantity() < discountConfiguration.quantityThreshold()) {
            return item;
        }
        BigDecimal percent = BigDecimal.valueOf(discountConfiguration.discountPercent()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal discountedPrice = item.orderedPrice().multiply(percent);
        return OrderedItem.builder()
                .name(item.name())
                .quantity(item.quantity())
                .orderedPrice(discountedPrice)
                .build();
    }

    record DiscountConfiguration(
            Integer quantityThreshold,
            Double discountPercent
    ) {

    }
}
