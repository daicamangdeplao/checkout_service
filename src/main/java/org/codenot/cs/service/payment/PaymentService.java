package org.codenot.cs.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.codenot.cs.entity.Order;
import org.codenot.cs.entity.OrderedItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentService {

    public void doPayment(Order order) {
        BigDecimal sum = order.orderedItems().stream()
                .map(OrderedItem::orderedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Payment of {} with total price {}", order.orderedItems().stream(), sum);
    }
}
