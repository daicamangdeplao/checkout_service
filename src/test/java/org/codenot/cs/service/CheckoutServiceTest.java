package org.codenot.cs.service;

import org.codenot.cs.entity.Basket;
import org.codenot.cs.entity.Item;
import org.codenot.cs.entity.Order;
import org.codenot.cs.entity.OrderedItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void checkoutShouldProcessCorrectOrder() {
        Basket basket = createBasket("apple");
        OrderedItem discountedOrderedItem = OrderedItem.builder()
                .name("apple")
                .orderedPrice(BigDecimal.valueOf(1.1))
                .quantity(1)
                .build();
        Order discountedOrder = Order.builder()
                .orderedItems(List.of(discountedOrderedItem))
                .build();
        BDDMockito.given(discountService.apply(BDDMockito.any())).willReturn(discountedOrderedItem);

        checkoutService.checkout(basket);

        BDDMockito.verify(paymentService).doPayment(discountedOrder);
    }

    private Basket createBasket(String... itemNames) {
        List<Item> items = Arrays.stream(itemNames)
                .map(name -> Item.builder()
                        .name(name)
                        .basePrice(BigDecimal.valueOf(1.1))
                        .build())
                .toList();

        return Basket.builder()
                .item(items)
                .build();
    }
}
