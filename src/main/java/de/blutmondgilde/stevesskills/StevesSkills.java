package de.blutmondgilde.stevesskills;

import com.mojang.logging.LogUtils;
import de.blutmondgilde.stevesskills.network.StevesSkillsNetwork;
import de.blutmondgilde.stevesskills.registrate.StevesRegistrate;
import de.blutmondgilde.stevesskills.skill.Skills;
import lombok.Getter;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(StevesSkills.MODID)
public class StevesSkills {
    public static final String MODID = "stevesskills";
    @Getter
    private static final Logger logger = LogUtils.getLogger();
    public static final StevesRegistrate REGISTRATE = new StevesRegistrate();

    public StevesSkills() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);
        Skills.init();
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        StevesSkillsNetwork.registerPackets();
    }
}
