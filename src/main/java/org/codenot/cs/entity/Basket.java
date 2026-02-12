package org.codenot.cs.entity;

import lombok.Builder;

import java.util.List;

@Builder
public record Basket(
        List<Item> item
) {
}
