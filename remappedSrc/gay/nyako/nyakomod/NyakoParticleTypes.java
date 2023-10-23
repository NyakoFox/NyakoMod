package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class NyakoParticleTypes {

    public static final DefaultParticleType ECHO_PORTAL = register("echo_portal", false);

    private static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, Identifier.of("nyakomod", name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void registerClient()
    {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier("nyakomod", "particle/echo_portal"));
        }));
        ParticleFactoryRegistry.getInstance().register(ECHO_PORTAL, EchoPortalParticle.Factory::new);
    }
}
