package studio.dreamys;

public class BazaarItem {

    private String productId;
    private double buyPrice;
    private double sellPrice;
    private double buyVolume;
    private double sellVolume;

    public BazaarItem(String productId, double buyPrice, double sellPrice, double buyVolume, double sellVolume) {
        this.productId = productId;
        this.buyPrice = buyPrice;  // Price at which WE buy
        this.sellPrice = sellPrice;  // Price at which WE sell
        this.buyVolume = buyVolume;
        this.sellVolume = sellVolume;
    }

    public double getSpread() {
        double mean = (sellPrice + buyPrice)/2;
        return (buyPrice- sellPrice)/mean;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getBuyVolume() {
        return buyVolume;
    }

    public double getSellVolume() {
        return sellVolume;
    }

    public double getEstimatedTimeToFill() {
        // Time to fill 1M coins for buy orders (in seconds)
        return 1_000_000 / (buyVolume * (buyPrice-0.1));  // In seconds
    }

    public double getEstimatedTimeToSell() {
        // Time to fill 1M coins for sell orders (in seconds)
        return 1_000_000 / (sellVolume * (sellPrice+0.1));  // In seconds
    }

    public String getProductId() {
        return productId;
    }
}
