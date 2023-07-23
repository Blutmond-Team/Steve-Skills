package de.blutmondgilde.stevesskills.client;

import de.blutmondgilde.stevesskills.client.overlay.SkillExpOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD)
public class StevesClient {

    @SubscribeEvent
    static void clientInit(final FMLClientSetupEvent e) {

    }

    @SubscribeEvent
    static void registerOverlay(final RegisterGuiOverlaysEvent e) {
        e.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "exp_overlay", SkillExpOverlay::render);
    }
}
