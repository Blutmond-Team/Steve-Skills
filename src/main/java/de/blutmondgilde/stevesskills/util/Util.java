package de.blutmondgilde.stevesskills.util;

import de.blutmondgilde.stevesskills.StevesSkills;
import lombok.experimental.UtilityClass;
import net.minecraft.resources.ResourceLocation;

@UtilityClass
public class Util {
    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(StevesSkills.MODID, path);
    }
}
