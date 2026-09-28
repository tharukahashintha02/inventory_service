package com.nsbm.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WarehouseTest {

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.add(new StockItem("SKU-1001", "Hex bolt M8", 240, 50));
        warehouse.add(new StockItem("SKU-1002", "Hex nut M8", 40, 50));
        warehouse.add(new StockItem("SKU-2010", "Bearing 6203", 12, 20));
        warehouse.add(new StockItem("SKU-3050", "Drive belt A42", 95, 25));
    }

    @Test
    @DisplayName("issuing stock reduces the quantity held")
    void issueReducesStock() {
        warehouse.issue("SKU-1001", 40);
        assertEquals(200, warehouse.find("SKU-1001").orElseThrow().getQuantity());
    }

    @Test
    @DisplayName("receiving stock increases the quantity held")
    void receiveIncreasesStock() {
        warehouse.receive("SKU-2010", 8);
        assertEquals(20, warehouse.find("SKU-2010").orElseThrow().getQuantity());
    }

    @Test
    @DisplayName("issuing more than is held is refused")
    void cannotIssueMoreThanHeld() {
        assertThrows(IllegalStateException.class,
                () -> warehouse.issue("SKU-2010", 13));
    }

    @Test
    @DisplayName("an unknown SKU is refused")
    void unknownSkuRefused() {
        assertThrows(IllegalArgumentException.class,
                () -> warehouse.issue("SKU-9999", 1));
    }

    @Test
    @DisplayName("the same SKU cannot be stocked twice")
    void duplicateSkuRefused() {
        assertThrows(IllegalArgumentException.class,
                () -> warehouse.add(new StockItem("SKU-1001", "Duplicate", 5, 1)));
    }

    @Test
    @DisplayName("the reorder report lists low stock, lowest first")
    void reorderReportOrdersByQuantity() {
        List<StockItem> report = warehouse.reorderReport();
        assertEquals(2, report.size());
        assertEquals("SKU-2010", report.get(0).getSku());
        assertEquals("SKU-1002", report.get(1).getSku());
    }

    @Test
    @DisplayName("well stocked lines stay out of the reorder report")
    void wellStockedLinesExcluded() {
        List<StockItem> report = warehouse.reorderReport();
        assertTrue(report.stream().noneMatch(i -> i.getSku().equals("SKU-1001")));
    }

    @Test
    @DisplayName("total units counts every line")
    void totalUnitsAcrossAllLines() {
        assertEquals(240 + 40 + 12 + 95, warehouse.totalUnits());
    }

    @Test
    @DisplayName("issue and receive quantities must be positive")
    void quantitiesMustBePositive() {
        assertThrows(IllegalArgumentException.class, () -> warehouse.issue("SKU-1001", 0));
        assertThrows(IllegalArgumentException.class, () -> warehouse.receive("SKU-1001", -5));
    }
}
