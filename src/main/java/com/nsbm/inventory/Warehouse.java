package com.nsbm.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Stock control for a single warehouse.
 *
 * The methods here are the ones a careless change is most likely to break,
 * which is what makes this class useful in a gating demonstration.
 */
public class Warehouse {

    private final Map<String, StockItem> items = new HashMap<>();

    public void add(StockItem item) {
        if (items.containsKey(item.getSku())) {
            throw new IllegalArgumentException("Already stocked: " + item.getSku());
        }
        items.put(item.getSku(), item);
    }

    public Optional<StockItem> find(String sku) {
        return Optional.ofNullable(items.get(sku));
    }

    public int size() {
        return items.size();
    }

    /** Take stock off the shelf. Throws if there is not enough. */
    public void issue(String sku, int quantity) {
        require(quantity > 0, "Issue quantity must be positive");
        item(sku).adjust(-quantity);
    }

    /** Put stock back on the shelf. */
    public void receive(String sku, int quantity) {
        require(quantity > 0, "Receive quantity must be positive");
        item(sku).adjust(quantity);
    }

    /** Everything at or below its reorder level, lowest stock first. */
    public List<StockItem> reorderReport() {
        List<StockItem> low = new ArrayList<>();
        for (StockItem item : items.values()) {
            if (item.needsReorder()) {
                low.add(item);
            }
        }
        low.sort(Comparator.comparingInt(StockItem::getQuantity));
        return low;
    }

    /** Total units held across every line. */
    public int totalUnits() {
        int total = 0;
        for (StockItem item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    private StockItem item(String sku) {
        StockItem item = items.get(sku);
        if (item == null) {
            throw new IllegalArgumentException("Not stocked: " + sku);
        }
        return item;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
