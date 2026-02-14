package org.codenot.cs.service.discount;

import org.codenot.cs.entity.OrderedItem;
import org.codenot.cs.service.discount.domain.DiscountStrategy;

import java.util.Optional;

public interface DiscountService {
    Optional<OrderedItem> apply(OrderedItem item);
    Boolean supports(DiscountStrategy strategy);
}
