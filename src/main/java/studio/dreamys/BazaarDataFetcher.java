package studio.dreamys;

import java.util.Map;

public class BazaarDataFetcher {

    public boolean success;
    public Map<String, BazaarItem> products;

    // Inner class to represent product information
    public static class BazaarItem {
        public BazaarQuickStatus quick_status;

        public static class BazaarQuickStatus {
            public String productId;
            public double sellPrice;
            public double sellVolume;
            public double sellMovingWeek;
            public int sellOrders;
            public double buyPrice;
            public double buyVolume;
            public double buyMovingWeek;
            public int buyOrders;
        }
    }
}
