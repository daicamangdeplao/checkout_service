package org.codenot.cs.entity;

import lombok.Builder;

import java.util.List;

@Builder
public record Order(
        List<OrderedItem> orderedItems
) {
}
