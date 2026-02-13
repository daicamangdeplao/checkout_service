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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CheckoutService {

    private final DiscountService discountService;
    private final PaymentService paymentService;

    public CheckoutService(DiscountService discountService, PaymentService paymentService) {
        this.discountService = discountService;
        this.paymentService = paymentService;
    }

    public void checkout(Basket basket) {
        log.info("Checkout basket");
        Optional<Order> originalOrder = createOrder(basket);

        if (originalOrder.isEmpty()) {
            log.info("Order is empty, checkout is cancelled");
            return;
        }

        Order discountOrder = applyDiscount(originalOrder.get());
        paymentService.doPayment(discountOrder);
        log.info("Finish checkout basket");
    }

    private Order applyDiscount(Order originalOrder) {
        List<OrderedItem> discountedOrderedItems = originalOrder.orderedItems().stream()
                .map(discountService::apply)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return Order.builder().orderedItems(discountedOrderedItems).build();
    }

    private Optional<Order> createOrder(Basket basket) {
        Map<String, PriceAndQuant> order = basket.item().stream()
                .collect(Collectors.toMap(
                        Item::name,
                        item -> new PriceAndQuant(item.basePrice(), 1),
                        (pq1, pg2) -> new PriceAndQuant(pq1.price.add(pg2.price()), Math.max(pq1.quantity(), pg2.quantity()) + 1)
                ));

        if (order.isEmpty()) {
            return Optional.empty();
        }

        List<OrderedItem> orderedItems = order.entrySet().stream()
                .map(entry -> OrderedItem.builder()
                        .name(entry.getKey())
                        .orderedPrice(entry.getValue().price())
                        .quantity(entry.getValue().quantity())
                        .build())
                .toList();

        if (orderedItems.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(Order.builder()
                .orderedItems(orderedItems)
                .build());
    }

    record PriceAndQuant(
            BigDecimal price,
            Integer quantity
    ) {
    }
}
