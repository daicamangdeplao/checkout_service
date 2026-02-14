package org.codenot.cs.service.discount;

import org.assertj.core.api.Assertions;
import org.codenot.cs.entity.OrderedItem;
import org.codenot.cs.service.discount.domain.DiscountStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TieredDiscountServiceTest {

    @InjectMocks
    private TieredDiscountService tieredDiscountService;

    @Test
    void supportsShouldRecognizeCorrectlyDiscountServiceStrategy() {
        assertTrue(tieredDiscountService.supports(DiscountStrategy.TIERED));
        assertFalse(tieredDiscountService.supports(DiscountStrategy.DEFAULT));
    }

    @Test
    void applyShouldReturnEmptyOptionalWhenNoDiscountConfigIsFound() {
        assertTrue(tieredDiscountService.apply(null).isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "2, 0.45",   // 2 apples trigger tier 2 (FIXED_TOTAL: €0.45)
            "3, 0.45",   // 3 apples still in tier 2 (FIXED_TOTAL: €0.45)
            "4, 0.45",   // 4 apples still in tier 2 (FIXED_TOTAL: €0.45)
            "5, 1.00"    // 5 apples trigger tier 3 (FIXED_TOTAL: €1.00)
    })
    void applyShouldReturnDiscountedPriceWhenDiscountConfigIsFound(int quantity, double expectedPrice) {
        OrderedItem orderedApple = OrderedItem.builder()
                .name("apple")
                .quantity(quantity)
                .build();

        Optional<OrderedItem> discountedOrderedApple = tieredDiscountService.apply(orderedApple);

        Assertions.assertThat(discountedOrderedApple).isPresent();
        Assertions.assertThat(discountedOrderedApple.get().orderedPrice())
                .isEqualTo(BigDecimal.valueOf(expectedPrice).setScale(2, RoundingMode.HALF_UP));
    }
}
