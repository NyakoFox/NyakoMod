package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.placeholders.api.TextParserUtils;
import gay.nyako.nyakomod.CachedResourcePack;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.PackUpdateNotifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

class SendToPlayerException extends Exception {
    public String message;

    public SendToPlayerException(String h) {
        message = h;
    }
}

public final class IconCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("icon")
                        .then(
                                CommandManager.literal("add")
                                        .then(CommandManager.argument("model", StringArgumentType.greedyString())
                                                .executes(IconCommand::onAddCommand)
                                        )
                        )
                        .then(
                                CommandManager.literal("alter")
                                        .then(CommandManager.argument("model", StringArgumentType.greedyString())
                                                .executes(IconCommand::onAlterCommand)
                                        )
                        )
                        .then(CommandManager.literal("set").executes(IconCommand::onSetCommand))
                        .then(CommandManager.literal("delete").executes(IconCommand::onDeleteCommand))
                        .then(CommandManager.literal("check").executes(IconCommand::onCheckCommand))
                        .then(CommandManager.literal("update").executes(IconCommand::onUpdateCommand))
                        .then(CommandManager.literal("publish").executes(IconCommand::onPublishCommand))
        );
    }

    static ItemStack getItemInHand(PlayerEntity sender) throws SendToPlayerException {
        var item = sender.getInventory().getMainHandStack();

        if (item.isEmpty()) {
            throw new SendToPlayerException("Error: You must be holding an item!");
        }

        return item;
    }

    static String validateModel(String input) throws SendToPlayerException {
        var stripped = input.replaceAll("[^A-z0-9._\\-:/]", "");

        if (stripped.matches("^[A-z0-9._\\-]+$")) {
            stripped = "custom:item/" + stripped;
        }

        if (!stripped.matches("^[A-z0-9._\\-]+:item/[A-z0-9._\\-]+$")) {
            throw new SendToPlayerException("Error: Invalid model name. May only contain alphanumeric characters, as well as . _ and -");
        }

        return stripped;
    }

    static boolean checkModelInUse(int id, String model, JSONArray array) {
        for (var object : array) {
            var obj = (JSONObject) object;
            var m = (String) obj.get("model");

            var predicate = (JSONObject) obj.get("predicate");
            int modelId = 0;

            if (predicate.has("custom_model_data")) {
                modelId = ((Number) predicate.get("custom_model_data")).intValue();
            }

            if (m.equals(model) && id != modelId) {
                return true;
            }
        }

        return false;
    }

    static private Identifier getKey(ItemStack item) {
        return new Identifier(item.getOrCreateNbt().getString("id"));
    }

    static int onAddCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        var player = source.getPlayer();

        try {
            var item = getItemInHand(player);

            var key = getKey(item);

            var path = "ResourcePack/assets/minecraft/models/item/" + key + ".json";

            var modelName = validateModel(context.getArgument("model", String.class));
            // AllyCatPlugin.INSTANCE.saveResource(path, false);

            var file = new File(FabricLoader.getInstance().getGameDir().toString(), path);
            var contents = Files.readString(file.toPath());

            var obj = new JSONObject(contents);
            var overrides = obj.get("overrides");
            int id = 1;

            JSONArray overridesArr;

            if (overrides == null) {
                overridesArr = new JSONArray();
            } else {
                overridesArr = (JSONArray) overrides;

                List<Integer> usedIds = new ArrayList<>();
                for (var object : overridesArr) {
                    JSONObject overrideObj = (JSONObject) object;
                    JSONObject predicateObj = (JSONObject) overrideObj.get("predicate");
                    if (predicateObj.has("custom_model_data")) {
                        int obtainedId = (int) ((Number) predicateObj.get("custom_model_data")).intValue();
                        usedIds.add(obtainedId);
                    }
                }

                int maxId = 99999;

                for (int i = 1; i < maxId; i++) {
                    if (!usedIds.contains(i)) {
                        id = i;
                        break;
                    }
                }
            }

            if (checkModelInUse(id, modelName, overridesArr)) {
                throw new SendToPlayerException("Error: This model is already in use on this item. Try using §6/icon set§f instead.");
            }

            JSONObject overrideObj = new JSONObject();
            JSONObject predicateObj = new JSONObject();
            predicateObj.put("custom_model_data", id);

            overrideObj.put("predicate", predicateObj);
            overrideObj.put("_owner", player.getUuid().toString());
            overrideObj.put("model", modelName);

            overridesArr.put(overrideObj);

            obj.put("overrides", overridesArr);

            try (PrintWriter out = new PrintWriter(file)) {
                out.println(obj.toString(2));
            }

            var nbt = item.getOrCreateNbt();
            nbt.putInt("CustomModelData", id);
            item.setNbt(nbt);

            player.sendMessage(Text.translatable("Added model as an override with ID=%s, using model=%s", id, modelName));
            player.sendMessage(Text.literal("When you're finished, run ").append("/icon publish").formatted(Formatting.GOLD).append(" to apply your changes."));
        } catch (SendToPlayerException e) {
            player.sendMessage(Text.literal(e.message));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Text.literal("Something went wrong.").formatted(Formatting.RED));
        }

        return 1;
    }

    static int onAlterCommand(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var player = source.getPlayer();

        try {
            var item = getItemInHand(player);

            var meta = item.getOrCreateNbt();

            var modelName = validateModel(context.getArgument("model", String.class));

            if (!meta.contains("CustomModelData")) {
                throw new SendToPlayerException("Error: This item doesn't have a model override set.");
            }

            var id = meta.getInt("CustomModelData");
            if (id == 0) {
                throw new SendToPlayerException("Error: This item doesn't have a model override set.");
            }

            var key = getKey(item);

            var path = "ResourcePack/assets/minecraft/models/item/" + key + ".json";

            // AllyCatPlugin.INSTANCE.saveResource(path, false);

            var file = new File(FabricLoader.getInstance().getGameDir().toString(), path);
            var contents = Files.readString(file.toPath());

            var obj = new JSONObject(contents);
            var overrides = obj.get("overrides");

            JSONArray overridesArr;

            if (overrides == null) {
                throw new SendToPlayerException("Error: A corresponding override does not exist on this item.");
            } else {
                overridesArr = (JSONArray) overrides;

                if (checkModelInUse(id, modelName, overridesArr)) {
                    throw new SendToPlayerException("Error: This model is already in use on this item. Try using §6/icon set§f instead.");
                }

                for (var object : overridesArr) {
                    JSONObject overrideObj = (JSONObject) object;
                    JSONObject predicateObj = (JSONObject) overrideObj.get("predicate");
                    if (predicateObj.has("custom_model_data")) {
                        int obtainedId = (int) ((Number) predicateObj.get("custom_model_data")).intValue();

                        if (obtainedId == id) {
                            String owner = (String) overrideObj.get("_owner");
                            var uuid = player.getUuid().toString();

                            if (!owner.equals(uuid)) {
                                throw new SendToPlayerException("Error: You were not the one to create this override, and thus cannot modify it!");
                            }

                            overrideObj.put("model", modelName);
                            break;
                        }
                    }
                }
            }

            try (PrintWriter out = new PrintWriter(file)) {
                out.println(obj.toString(2));
            }

            player.sendMessage(Text.translatable("Updated model as an override with ID=%s, using model=%s", id, modelName));
            player.sendMessage(Text.literal("When you're finished, run ").append(Text.literal("/icon publish").formatted(Formatting.GOLD)).append(" to apply your changes."));
        } catch (SendToPlayerException e) {
            player.sendMessage(Text.literal(e.message));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Text.literal("Something went wrong.").formatted(Formatting.RED));
        }

        return 1;
    }

    static int onSetCommand(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var player = source.getPlayer();

        // IconInventoryListener.openIconInventory((Player) sender);

        return 1;
    }

    static int onDeleteCommand(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var player = source.getPlayer();

        try {
            var item = getItemInHand(player);
            var meta = item.getOrCreateNbt();
            meta.remove("CustomModelData");
            item.setNbt(meta);
        } catch (SendToPlayerException e) {
            player.sendMessage(Text.literal(e.message));
        }

        return 1;
    }

    static int onCheckCommand(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var player = source.getPlayer();

        try {
            var item = getItemInHand(player);

            var meta = item.getOrCreateNbt();
            if (meta.contains("CustomModelData")) {
                player.sendMessage(Text.translatable("This item is currently using a custom model ID of: %d", meta.getInt("CustomModelData")));
            } else {
                player.sendMessage(Text.literal("This item is using the base model."));
            }
        } catch (SendToPlayerException e) {
            player.sendMessage(Text.literal(e.message));
        }

        return 1;
    }

    static int onUpdateCommand(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var player = source.getPlayer();

        CachedResourcePack.setPlayerResourcePack(player);
        return 1;
    }

    static int onPublishCommand(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var player = source.getPlayer();
        // AllyCatPlugin.INSTANCE.recacheResourcePack();
        PackUpdateNotifier.causePackUpdate(player);
        CachedResourcePack.setPlayerResourcePack(player);
        player.sendMessage(Text.literal("Your changes have been published!"));
        return 1;
    }

}
