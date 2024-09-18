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

                BazaarItem newItem = new BazaarItem(
                        entry.getKey(),
                        originalItem.quick_status.buyPrice,
                        originalItem.quick_status.sellPrice,
                        originalItem.quick_status.buyVolume,
                        originalItem.quick_status.sellVolume
                );

                // Only add items that have valid volumes and prices
                if (originalItem.quick_status.buyPrice > 0 && originalItem.quick_status.sellPrice > 0
                        && originalItem.quick_status.buyVolume > 0 && originalItem.quick_status.sellVolume > 0) {
                    items.put(entry.getKey(), newItem);
                }
            }
            return items;
        }
    }

    public static List<BazaarItem> getTopFlippingItems(Map<String, BazaarItem> data, int topN) {
        return data.values().stream()
                // Only include items with valid buy and sell volumes
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
