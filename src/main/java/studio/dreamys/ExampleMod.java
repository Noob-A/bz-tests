package studio.dreamys;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import studio.dreamys.BazaarFlippingHUD;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MODID = "waromiv_bz";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register the event handler for rendering
        MinecraftForge.EVENT_BUS.register(new BazaarFlippingHUD());
    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Ensure DevAuth is enabled and set the account
        System.setProperty("devauth.enabled", "true");
        System.setProperty("devauth.account", "main");

        // Your other mod initialization code
    }
}
