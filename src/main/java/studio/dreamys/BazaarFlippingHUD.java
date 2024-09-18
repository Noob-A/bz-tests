package studio.dreamys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

import java.util.List;
import java.util.Map;

public class BazaarFlippingHUD extends Gui {

    private Minecraft mc = Minecraft.getMinecraft();
    private int scrollOffset = 0;
    private int maxItemsPerPage = 10; // Items to show at once
    private int itemHeight = 12; // Height of each item in pixels
    private List<BazaarItem> currentItems;

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        render();
    }

    public void render() {
        try {
            if (currentItems == null || currentItems.isEmpty()) {
                Map<String, BazaarItem> data = BazaarAPI.fetchBazaarData();
                currentItems = BazaarAPI.getTopFlippingItems(data, 50);
            }

            int x = 10;
            int y = 10 - scrollOffset * itemHeight;

            // Display items with scrolling support
            for (int i = 0; i < Math.min(maxItemsPerPage, currentItems.size() - scrollOffset); i++) {
                BazaarItem item = currentItems.get(i + scrollOffset);
                if (y + itemHeight > 0 && y < mc.displayHeight) {
                    drawRect(x - 2, y - 2, x + 200, y + itemHeight + 2, 0x90000000);
                    drawString(mc.fontRendererObj, item.getProductId() +
                            ": Spread: " + String.format("%.2f", item.getSpread()) +
                            " Buy Vol: " + String.format("%.2f", item.getBuyVolume()) +
                            " Sell Vol: " + String.format("%.2f", item.getSellVolume()) +
                            " Fill Time: " + String.format("%.2f", item.getEstimatedTimeToFill()) + " sec", x, y, 0xFFFFFF);
                }
                y += itemHeight;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onMouseScroll(InputEvent.MouseInputEvent event) {
        // Ensure scrolling is within bounds
        if (Mouse.hasWheel()) {
            int scroll = Mouse.getDWheel();
            if (scroll != 0) {
                // Adjust scrolling based on mouse wheel
                int newOffset = scrollOffset + (scroll > 0 ? -1 : 1);
                scrollOffset = Math.max(0, Math.min(newOffset, currentItems.size() - maxItemsPerPage));
            }
        }
    }
}
