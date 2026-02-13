package org.codenot.cs.service;

import org.codenot.cs.entity.Basket;
import org.codenot.cs.entity.Item;
import org.codenot.cs.entity.Order;
import org.codenot.cs.entity.OrderedItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    private static final int ANY_INT_VALUE = ThreadLocalRandom.current().nextInt();
    private static final BigDecimal ANY_BIG_DECIMAL = new BigDecimal(ANY_INT_VALUE);
    private static final String ANY_STRING_VALUE = "Any String";

    @Mock
    private PaymentService paymentService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private CheckoutService checkoutService;

    private Basket basket;
    private OrderedItem discountedOrderedItem;
    private Order discountedOrder;

    @BeforeEach
    void setUp() {
        basket = createBasket(ANY_STRING_VALUE);
        discountedOrderedItem = OrderedItem.builder()
                .name(ANY_STRING_VALUE)
                .orderedPrice(ANY_BIG_DECIMAL)
                .quantity(ANY_INT_VALUE)
                .build();
        discountedOrder = Order.builder()
                .orderedItems(List.of(discountedOrderedItem))
                .build();
    }

    @Test
    void checkoutShouldProcessCorrectOrder() {
        BDDMockito.given(discountService.apply(BDDMockito.any())).willReturn(Optional.of(discountedOrderedItem));

        checkoutService.checkout(basket);

        BDDMockito.verify(paymentService).doPayment(discountedOrder);
    }

    @Test
    void checkoutShouldNotInvokePaymentServiceWhenOrderedItemsIsEmpty() {
        BDDMockito.given(discountService.apply(BDDMockito.any())).willReturn(Optional.empty());

        checkoutService.checkout(basket);

        BDDMockito.verify(discountService, BDDMockito.times(1)).apply(BDDMockito.any());
        BDDMockito.verify(paymentService, BDDMockito.times(0)).doPayment(discountedOrder);
    }

    private Basket createBasket(String... itemNames) {
        List<Item> items = Arrays.stream(itemNames)
                .map(name -> Item.builder()
                        .name(name)
                        .basePrice(ANY_BIG_DECIMAL)
                        .build())
                .toList();

        return Basket.builder()
                .item(items)
                .build();
    }

    // The price should correct
    // The quantity should correct
}
