package gay.nyako.nyakomod;

import gay.nyako.nyakomod.screens.*;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class NyakoScreenHandlers {
    public static final ScreenHandlerType<FletchingTableScreenHandler> FLETCHING_TABLE = register("fletching_table", new ScreenHandlerType<>(FletchingTableScreenHandler::new, FeatureSet.empty()));
    public static final ScreenHandlerType<CunkShopScreenHandler> CUNK_SHOP_SCREEN_HANDLER_TYPE = register("cunk_shop", new ExtendedScreenHandlerType<>(CunkShopScreenHandler::new));
    public static final ScreenHandlerType<BlueprintWorkbenchScreenHandler> BLUEPRINT_WORKBENCH_SCREEN_HANDLER_TYPE = register("blueprint_workbench", new ScreenHandlerType<>(BlueprintWorkbenchScreenHandler::new, FeatureSet.empty()));
    public static final ScreenHandlerType<PresentWrapperScreenHandler> PRESENT_WRAPPER_SCREEN_HANDLER_TYPE = register("present_wrapper", new ScreenHandlerType<>(PresentWrapperScreenHandler::new, FeatureSet.empty()));
    public static final ScreenHandlerType<NBPScreenHandler> NBP_SCREEN_HANDLER_TYPE = register("note_block_plus", new ExtendedScreenHandlerType<>(NBPScreenHandler::new));
    public static final ScreenHandlerType<IconScreenHandler> ICON_SCREEN_HANDLER_TYPE = register("icon_menu", new ExtendedScreenHandlerType<>(IconScreenHandler::new));

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType<T> screenHandlerType) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier("nyakomod", id), screenHandlerType);
    }

    public static void register() {
        // include the class
    }
}
