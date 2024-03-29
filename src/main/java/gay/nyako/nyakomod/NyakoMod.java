package gay.nyako.nyakomod;

import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.access.ServerPlayerEntityAccess;
import gay.nyako.nyakomod.behavior.CoinBagItemDispenserBehavior;
import gay.nyako.nyakomod.behavior.NetherPortalStructureItemDispenserBehavior;
import gay.nyako.nyakomod.behavior.SoulJarItemDispenserBehavior;
import gay.nyako.nyakomod.block.SingleCoinBlock;
import gay.nyako.nyakomod.command.*;
import gay.nyako.nyakomod.entity.HerobrineEntity;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.mixin.ScoreboardCriterionMixin;
import gay.nyako.nyakomod.utils.ChatUtils;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import io.github.tropheusj.milk.Milk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceType;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NyakoMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("nyakomod");

    public static final gay.nyako.nyakomod.NyakoConfig CONFIG = gay.nyako.nyakomod.NyakoConfig.createAndLoad();

    public static final IntProperty COINS_PROPERTY = IntProperty.of("coins", 1, SingleCoinBlock.MAX_COINS);
    public static SlimeSkyManager SLIME_SKY_MANAGER;
    public static final ArmorMaterial DRIP_ARMOR_MATERIAL = new DripArmorMaterial();
    public static final ArmorMaterial JEAN_ARMOR_MATERIAL = new JeanArmorMaterial();
    public static final ScoreboardCriterion COIN_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:coins");
    public static final ScoreboardCriterion TIMES_MILKED_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:times_milked");
    public static final ScoreboardCriterion PLAYERS_MILKED_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:players_milked");
    public static final ScoreboardCriterion PLAYER_MILK_CONSUMED_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:player_milk_consumed");
    public static final ScoreboardCriterion MILK_CONSUMED_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:milk_consumed");
    public static Enchantment CUNKLESS_CURSE_ENCHANTMENT = Registry.register(Registries.ENCHANTMENT, new Identifier("nyakomod", "cunkless_curse"), new CunkCurseEnchantment());

    public static RegistryKey<World> ECHOLANDS_KEY = RegistryKey.of(RegistryKeys.WORLD, new Identifier("nyakomod", "echolands"));
    public static RegistryKey<DimensionType> ECHOLANDS_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier("nyakomod", "echolands"));

    //@Environment(EnvType.SERVER)
    public static CachedResourcePack CACHED_RESOURCE_PACK = new CachedResourcePack();

    //@Environment(EnvType.SERVER)
    public static ModelManager MODEL_MANAGER = new ModelManager();

    public static final TrackedDataHandler<StickerPackCollection> STICKER_PACK_COLLECTION_DATA = new TrackedDataHandler<>(){

        @Override
        public void write(PacketByteBuf packetByteBuf, StickerPackCollection stickerPackCollection) {
            packetByteBuf.writeInt(stickerPackCollection.size());
            for (String string : stickerPackCollection) {
                packetByteBuf.writeString(string);
            }
        }

        @Override
        public StickerPackCollection read(PacketByteBuf packetByteBuf) {
            int size = packetByteBuf.readInt();
            StickerPackCollection collection = new StickerPackCollection();
            for (int i = 0; i < size; i++) {
                collection.add(packetByteBuf.readString());
            }
            return collection;
        }

        @Override
        public StickerPackCollection copy(StickerPackCollection stickerPackCollection) {
            StickerPackCollection newCollection = new StickerPackCollection();
            newCollection.addAll(stickerPackCollection);
            return newCollection;
        }

    };

    @Override
    public void onInitialize() {

        TrackedDataHandlerRegistry.register(STICKER_PACK_COLLECTION_DATA);

        Milk.enableMilkFluid();
        //Milk.enableCauldron();
        Milk.enableMilkPlacing();
        Milk.finiteMilkFluid();
        Milk.enableAllMilkBottles();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ShopDataResourceReloadListener());
        NyakoNetworking.registerGlobalReceivers();
        NyakoLoot.register();
        NyakoGacha.register();
        InstrumentRegistry.register();
        NyakoSoundEvents.register();
        NyakoCriteria.register();
        NyakoScreenHandlers.register();
        NyakoPaintingVariants.register();
        NyakoItemGroups.register();
        NyakoFeatures.register();

        StrippableBlockRegistry.register(NyakoBlocks.ECHO_SPINE, NyakoBlocks.STRIPPED_ECHO_SPINE);
        StrippableBlockRegistry.register(NyakoBlocks.ECHO_SPUR, NyakoBlocks.STRIPPED_ECHO_SPUR);

        FabricDefaultAttributeRegistry.register(NyakoEntities.PET_SPRITE, PetSpriteEntity.createPetAttributes());
        FabricDefaultAttributeRegistry.register(NyakoEntities.PET_DRAGON, PetDragonEntity.createPetAttributes());
        FabricDefaultAttributeRegistry.register(NyakoEntities.HEROBRINE, HerobrineEntity.createHerobrineAttributes());
        FabricDefaultAttributeRegistry.register(NyakoEntities.DECAYED, ZombieEntity.createZombieAttributes());

        DispenserBlock.registerBehavior(NyakoItems.SOUL_JAR, new SoulJarItemDispenserBehavior());
        DispenserBlock.registerBehavior(NyakoItems.BAG_OF_COINS, new CoinBagItemDispenserBehavior());
        DispenserBlock.registerBehavior(NyakoItems.HUNGRY_BAG_OF_COINS, new CoinBagItemDispenserBehavior());
        DispenserBlock.registerBehavior(NyakoItems.NETHER_PORTAL_STRUCTURE, new NetherPortalStructureItemDispenserBehavior());

        NyakoPotions.registerPotionsRecipes();

        CunkCoinUtils.registerCoinAmounts();
        registerCommands();

        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            CachedResourcePack.setPlayerResourcePack(handler.player);
            ((ServerPlayerEntityAccess)handler.player).setSafeMode(true);

            // Pool of strings:
            String[][] randomText = {
                    {
                            "You have caught <gold>%player:statistic minecraft:fish_caught%</gold> fish!",
                            "Maybe today you could make that more."
                    },
                    {
                            "You've jumped <gold>%player:statistic minecraft:jump%</gold> times!",
                            "Maybe today you could make that more."
                    },
                    {
                            "You've killed <gold>%player:statistic minecraft:player_kills%</gold> players!",
                            "Maybe today you could make that more...?"
                    },
                    {
                            "Did you know that <gold>80%</gold> of gamblers",
                            "quit right before they're about to hit it big?"
                    },
                    {
                            "You're hiding something. That's okay.",
                            "We are, too."
                    },
                    {
                            "You can place milk in cauldrons.",
                            "It's easier to drink that way."
                    },
                    {
                            "Creepers drop <gold>100%</gold> of the blocks they explode!",
                            "This is because Ally got tired of losing things."
                    },
                    {
                            "You miss <gold>99%</gold> of the shots you don't take.",
                            "And around <gold>74%</gold> of the ones you do."
                    },
                    {
                            "Make sure to check out the shop every once in a while!",
                            "Maybe you'll find something you like."
                    },
                    {
                            "You have new mail!",
                            "...just kidding, we don't have mail."
                    },
                    {
                        "True!", "True!"
                    },
                    {
                        "You can't milk a milk bucket.",
                        "You can't milk a milk bucket.",
                    },
                    {
                        "Give a man some milk, his bar will be filled for 30 minutes.",
                        "Teach a man to milk, his bar will be filled for life."
                    },
                    {
                        "Teach a milk to man, his minutes will be 30 bars.",
                        "Milk a man for life, his teach will be bars filled."
                    }
            };

            // Pick a random array from the pool
            String[] randomTextArray = randomText[(int) (Math.random() * randomText.length)];

            //if (server.getServerIp() != null && server.getServerIp().equals("51.222.14.126"))
            //{
                ChatUtils.send(handler.player, "Welcome back to <gradient:aqua:light_purple>Allybox</gradient>!", ChatPrefixes.INFO);

                for (String string : randomTextArray) {
                    ChatUtils.send(handler.player, string, ChatPrefixes.INFO);
                }
            //}
        }));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SLIME_SKY_MANAGER = SlimeSkyManager.forWorld(server.getWorld(World.OVERWORLD));
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (ServerPlayerEntity player : world.getPlayers()) {
                var access = (ServerPlayerEntityAccess) player;
                if (access.isInSafeMode()) {
                    if (!access.getJoinPos().equals(player.getPos())) {
                        access.setSafeMode(false);
                    }
                }
            }

            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.abilities.invulnerable) continue;
                if (player.isDead()) continue;

                var access = (PlayerEntityAccess) player;

                int increase = 1;
                if (player.isSprinting()) increase += 1;
                access.setMilkTimer(access.getMilkTimer() + increase);

                if (access.getMilkTimer() >= (10 * 60 * 20)) { // every 10 minutes (unless u sprint
                    access.setMilkTimer(0);
                    if (access.getMilkSaturation() > 0) {
                        access.setMilkSaturation(access.getMilkSaturation() - 1);
                        continue;
                    }
                    if (access.getMilk() > 0) {
                        access.setMilk(access.getMilk() - 1);
                    }
                }
            }

            // Loop through all entities in all loaded worlds
            for (ServerWorld serverWorld : world.getServer().getWorlds()) {
                for (ItemEntity entity : serverWorld.getEntitiesByType(EntityType.ITEM, entity -> entity instanceof ItemEntity)) {
                    if (entity.isSubmergedInWater())
                    {
                        ItemStack stack = entity.getStack();
                        if (stack.getItem() == NyakoItems.FOAM_ZOMBIE) {
                            // Change the item to a grown foam zombie
                            ItemStack newStack = new ItemStack(NyakoItems.GROWN_FOAM_ZOMBIE);
                            newStack.setCount(stack.getCount());
                            newStack.setNbt(stack.getNbt());
                            entity.setStack(newStack);
                        }
                    }
                }
            }

            if (world.getRegistryKey() == World.OVERWORLD) {
                if (SLIME_SKY_MANAGER == null) return;

                SLIME_SKY_MANAGER.tick();
            }
        });
    }

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BackCommand.register(dispatcher);
            XpCommand.register(dispatcher);
            FakeCountCommand.register(dispatcher);
            PackCommand.register(dispatcher);
            SmiteCommand.register(dispatcher);
            SlimeDebugCommand.register(dispatcher);
            ShopCommand.register(dispatcher);
            AFKCommand.register(dispatcher);
            DumpNbtCommand.register(dispatcher);
            HelpCommand.register(dispatcher);
            IconsCommand.register(dispatcher);
        });
    }

}