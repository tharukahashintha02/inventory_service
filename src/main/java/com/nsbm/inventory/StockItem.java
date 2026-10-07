package com.nsbm.inventory;

import java.util.Objects;

/**
 * A single line of stock held in the warehouse.
 */
public class StockItem {

    private final String sku;
    private final String name;
    private int quantity;
    private final int reorderLevel;

    public StockItem(String sku, String name, int quantity, int reorderLevel) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is required");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (reorderLevel < 0) {
            throw new IllegalArgumentException("Reorder level cannot be negative");
        }
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    /** True when stock has fallen to or below the level at which we reorder. */
    public boolean needsReorder() {
        return quantity < reorderLevel; // return quantity < reorderLevel;
    }

    void adjust(int delta) {
        int updated = quantity + delta;
        if (updated < 0) {
            throw new IllegalStateException(
                    "Cannot remove " + Math.abs(delta) + " of " + sku
                            + "; only " + quantity + " in stock");
        }
        quantity = updated;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StockItem)) {
            return false;
        }
        return sku.equals(((StockItem) other).sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    @Override
    public String toString() {
        return sku + " (" + name + ") x" + quantity;
    }
}
