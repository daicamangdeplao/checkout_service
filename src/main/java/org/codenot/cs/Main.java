package org.codenot.cs;

import org.codenot.cs.entity.Basket;
import org.codenot.cs.entity.Item;
import org.codenot.cs.entity.Order;
import org.codenot.cs.entity.OrderedItem;
import org.codenot.cs.service.checkout.CheckoutService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final List<String> PRODUCT_NAMES = List.of("apple", "orange", "banana");
    private final Map<String, BigDecimal> PRODUCT_PRICES = Map.of(
            "apple", BigDecimal.valueOf(1.1),
            "orange", BigDecimal.valueOf(2.1),
            "banana", BigDecimal.valueOf(3.1)
    );

    private final CheckoutService checkoutService;

    public Main(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        Basket basket = createBasket();
        checkoutService.checkout(basket, "default");
    }

    private Basket createBasket() {
        return Basket.builder()
                .item(randomizeItems())
                .build();
    }

    private List<Item> randomizeItems() {
        return IntStream.range(0, ThreadLocalRandom.current().nextInt(10))
                .mapToObj(i -> {
                    String productName = PRODUCT_NAMES.get(ThreadLocalRandom.current().nextInt(PRODUCT_NAMES.size()));
                    return Item.builder()
                            .name(productName)
                            .basePrice(PRODUCT_PRICES.get(productName))
                            .build();
                })
                .toList();
    }
}
