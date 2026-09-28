package com.nsbm.inventory;

/**
 * Prints a short stock report. Enough to show the application runs.
 */
public class App {

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        warehouse.add(new StockItem("SKU-1001", "Hex bolt M8", 240, 50));
        warehouse.add(new StockItem("SKU-1002", "Hex nut M8", 40, 50));
        warehouse.add(new StockItem("SKU-2010", "Bearing 6203", 12, 20));
        warehouse.add(new StockItem("SKU-3050", "Drive belt A42", 95, 25));

        warehouse.issue("SKU-1001", 40);
        warehouse.receive("SKU-2010", 5);

        System.out.println("Inventory service");
        System.out.println("Lines stocked: " + warehouse.size());
        System.out.println("Units held:    " + warehouse.totalUnits());
        System.out.println();
        System.out.println("Reorder now:");
        for (StockItem item : warehouse.reorderReport()) {
            System.out.printf("  %-10s %-16s %4d in stock, reorder at %d%n",
                    item.getSku(), item.getName(),
                    item.getQuantity(), item.getReorderLevel());
        }
    }
}
