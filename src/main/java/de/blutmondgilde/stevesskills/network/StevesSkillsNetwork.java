package de.blutmondgilde.stevesskills.network;

import com.google.common.base.Suppliers;
import de.blutmondgilde.stevesskills.StevesSkills;
import de.blutmondgilde.stevesskills.network.packet.clientbound.SyncEntitySkillsPacket;
import lombok.experimental.UtilityClass;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

import static de.blutmondgilde.stevesskills.util.Util.resourceLocation;

@UtilityClass
public class StevesSkillsNetwork {
    private static final Supplier<String> PROTOCOL_VERSION = Suppliers.memoize(() -> ModList.get().getMods()
            .stream()
            .filter(iModInfo -> iModInfo.getModId().equals(StevesSkills.MODID))
            .findFirst()
            .map(IModInfo::getVersion)
            .map(MavenVersionStringHelper::artifactVersionToString)
            .orElse("1"));
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(resourceLocation("main"), PROTOCOL_VERSION, s -> PROTOCOL_VERSION.get().equals(s), s -> PROTOCOL_VERSION.get().equals(s));

    public static void registerPackets() {
        int packetId = 1;
        INSTANCE.registerMessage(packetId++, SyncEntitySkillsPacket.class, SyncEntitySkillsPacket::toBytes, SyncEntitySkillsPacket::new, SyncEntitySkillsPacket::handle);
    }
}
