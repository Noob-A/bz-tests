package studio.dreamys;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BazaarAPI {

    private static final String BAZAAR_API_URL = "https://api.hypixel.net/skyblock/bazaar";
    private static final Gson gson = new Gson();

    public static Map<String, BazaarItem> fetchBazaarData() throws Exception {
        URL url = new URL(BAZAAR_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            BazaarDataFetcher data = gson.fromJson(reader, BazaarDataFetcher.class);

            Map<String, BazaarItem> items = new HashMap<>();

            for (Map.Entry<String, BazaarDataFetcher.BazaarItem> entry : data.products.entrySet()) {
                BazaarDataFetcher.BazaarItem originalItem = entry.getValue();

                // Check if OB has orders
                if (!originalItem.buy_summary.isEmpty() && !originalItem.sell_summary.isEmpty()) {
                    double buyPrice = originalItem.buy_summary.get(0).pricePerUnit;
                    double sellPrice = originalItem.sell_summary.get(0).pricePerUnit;

                    // Use the movingWeek data for volumes to get realistic transaction data
                    double buyVolume = originalItem.quick_status.buyMovingWeek / 7 / 24 / 60/60;
                    double sellVolume = originalItem.quick_status.sellMovingWeek / 7 / 24 / 60 /60;
                    System.out.println("Item: " + entry.getKey() + " Buy Price: " + buyPrice + " Sell Price: " + sellPrice + " Buy Volume: " + buyVolume + " Sell Volume: " + sellVolume);

                    BazaarItem newItem = new BazaarItem(
                            entry.getKey(),
                            buyPrice,
                            sellPrice,
                            buyVolume,
                            sellVolume
                    );

                    // Only add items that have valid volumes and prices
                    if (buyPrice > 0 && sellPrice > 0 && buyVolume > 0 && sellVolume > 0) {
                        items.put(entry.getKey(), newItem);
                    }
                }

            }
            return items;
        }
    }

    public static List<BazaarItem> getTopFlippingItems(Map<String, BazaarItem> data, int topN) {
        return data.values().stream()
                .filter(item -> item.getBuyVolume() > 0 && item.getSellVolume() > 0)
                .sorted((item1, item2) -> Double.compare(
                        (item2.getSpread() * item2.getBuyVolume() * item2.getSellVolume()) / getVolumeRatio(item2),
                        (item1.getSpread() * item1.getBuyVolume() * item1.getSellVolume()) / getVolumeRatio(item1)
                ))
                .limit(topN)
                .collect(Collectors.toList());
    }

    private static double getVolumeRatio(BazaarItem item) {
        return Math.abs(item.getBuyVolume() - item.getSellVolume()) / Math.min(item.getBuyVolume(), item.getSellVolume());
    }
}
