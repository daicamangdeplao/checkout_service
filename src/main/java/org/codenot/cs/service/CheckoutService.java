package org.codenot.cs.service;

import lombok.extern.slf4j.Slf4j;
import org.codenot.cs.entity.Basket;
import org.codenot.cs.entity.Item;
import org.codenot.cs.entity.Order;
import org.codenot.cs.entity.OrderedItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CheckoutService {

    public void checkout(Basket basket) {
        Order order = createOrder(basket);
        log.info("Checkout started");
    }

    private Order createOrder(Basket basket) {
        Map<String, PriceAndQuant> order = basket.item().stream()
                .collect(Collectors.toMap(
                        Item::name,
                        item -> new PriceAndQuant(item.basePrice(), 1),
                        (u1, u2) -> new PriceAndQuant(u1.price.add(u2.price()), Math.max(u1.quantity(), u2.quantity()) + 1)
                ));

        List<OrderedItem> orderedItems = order.entrySet().stream()
                .map(entry -> OrderedItem.builder()
                        .name(entry.getKey())
                        .orderedPrice(entry.getValue().price())
                        .quantity(entry.getValue().quantity())
                        .build())
                .toList();

        return Order.builder()
                .orderedItems(orderedItems)
                .build();
    }

    record PriceAndQuant(
            BigDecimal price,
            Integer quantity
    ) {
    }

}
