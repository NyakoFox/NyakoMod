package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.BlueprintWorkbenchBlockEntity;
import gay.nyako.nyakomod.block.NoteBlockPlusBlockEntity;
import gay.nyako.nyakomod.block.PresentWrapperBlockEntity;
import gay.nyako.nyakomod.entity.MonitorEntity;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.entity.TickerEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NyakoEntities {
    public static final BlockEntityType<BlueprintWorkbenchBlockEntity> BLUEPRINT_WORKBENCH_ENTITY = register("blueprint_workbench", FabricBlockEntityTypeBuilder.create(BlueprintWorkbenchBlockEntity::new, NyakoBlocks.BLUEPRINT_WORKBENCH).build(null));
    public static final BlockEntityType<NoteBlockPlusBlockEntity> NOTE_BLOCK_PLUS_ENTITY = register("note_block_plus", FabricBlockEntityTypeBuilder.create(NoteBlockPlusBlockEntity::new, NyakoBlocks.NOTE_BLOCK_PLUS).build(null));
    public static final BlockEntityType<PresentWrapperBlockEntity> PRESENT_WRAPPER_ENTITY = register("present_wrapper", FabricBlockEntityTypeBuilder.create(PresentWrapperBlockEntity::new, NyakoBlocks.PRESENT_WRAPPER).build(null));
    public static final EntityType<TickerEntity> TICKER = register("ticker", FabricEntityTypeBuilder.create(SpawnGroup.MISC, TickerEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build());
    public static final EntityType<PetSpriteEntity> PET_SPRITE = register("petsprite", FabricEntityTypeBuilder.create(SpawnGroup.MISC, PetSpriteEntity::new).dimensions(EntityDimensions.changing(0.1f, 1.8f)).trackRangeBlocks(10).build());
    public static final EntityType<PetDragonEntity> PET_DRAGON = register("petdragon", FabricEntityTypeBuilder.create(SpawnGroup.MISC, PetDragonEntity::new).dimensions(EntityDimensions.changing(0.6f, 1f)).trackRangeBlocks(10).build());
    public static final EntityType<MonitorEntity> MONITOR = register("monitor", FabricEntityTypeBuilder.<MonitorEntity>create(SpawnGroup.MISC, MonitorEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());

    public static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier("nyakomod", name), entityType);
    }

    public static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("nyakomod", name), blockEntityType);
    }
}
