package studio.dreamys;

import java.util.List;
import java.util.Map;

public class BazaarDataFetcher {

    public boolean success;
    public Map<String, BazaarItem> products;

    // Inner class to represent product information
    public static class BazaarItem {
        public BazaarQuickStatus quick_status;
        public List<OrderSummary> buy_summary;
        public List<OrderSummary> sell_summary;

        public static class BazaarQuickStatus {
            public double buyMovingWeek;
            public double sellMovingWeek;
        }

        // Represents order book entries in buy_summary and sell_summary
        public static class OrderSummary {
            public double pricePerUnit;
            public double amount;
        }
    }
}
