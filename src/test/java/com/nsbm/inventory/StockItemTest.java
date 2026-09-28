package com.nsbm.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StockItemTest {

    @Test
    @DisplayName("stock at the reorder level counts as needing reorder")
    void reorderAtTheBoundary() {
        assertTrue(new StockItem("SKU-1", "Bolt", 50, 50).needsReorder());
        assertTrue(new StockItem("SKU-1", "Bolt", 49, 50).needsReorder());
        assertFalse(new StockItem("SKU-1", "Bolt", 51, 50).needsReorder());
    }

    @Test
    @DisplayName("a blank SKU is rejected")
    void blankSkuRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new StockItem("  ", "Bolt", 10, 5));
    }

    @Test
    @DisplayName("negative quantities are rejected")
    void negativeQuantityRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new StockItem("SKU-1", "Bolt", -1, 5));
    }

    @Test
    @DisplayName("items are equal when their SKUs match")
    void equalityBySku() {
        assertEquals(new StockItem("SKU-1", "Bolt", 10, 5),
                     new StockItem("SKU-1", "Different name", 99, 1));
    }
}
