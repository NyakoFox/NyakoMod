package gay.nyako.nyakomod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gay.nyako.nyakomod.data.CondensedMatterOutcome;
import gay.nyako.nyakomod.data.CondensedMatterOutcomes;
import gay.nyako.nyakomod.outcomes.*;
import gay.nyako.nyakomod.outcomes.fields.*;
import gay.nyako.nyakomod.outcomes.positions.AbsoluteOutcomePosition;
import gay.nyako.nyakomod.outcomes.positions.BlockOutcomePosition;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import gay.nyako.nyakomod.outcomes.positions.PlayerOutcomePosition;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CondensedMatterBlockResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return new Identifier("nyakomod", "condensed_matter_block_outcomes");
    }

    private boolean isNull(JsonElement element) {
        return (element == null || element.isJsonNull());
    }

    private OutcomeField<Identifier> getIdentifier(JsonElement element) {
        if (isNull(element))
        {
            throw new IllegalStateException("Expected identifier");
        }

        if (element.isJsonPrimitive())
        {
            var primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return new ConstantOutcomeField<>(new Identifier(primitive.getAsString()));
            }
            throw new IllegalStateException("Expected string");
        }

        var jsonObject = element.getAsJsonObject();

        var typeElement = jsonObject.get("type");
        if (typeElement == null || typeElement.isJsonNull())
        {
            throw new IllegalStateException("Type missing for outcome object");
        }
        return switch (typeElement.getAsString())
        {
            case "nyakomod:constant" -> getIdentifier(jsonObject.get("value"));
            case "nyakomod:choice" -> {
                var choices = new ArrayList<OutcomeField<Identifier>>();
                jsonObject.get("choices").getAsJsonArray().forEach(choiceElement -> {
                    choices.add(getIdentifier(choiceElement));
                });
                yield new ChoiceOutcomeField<>(choices);
            }
            default -> throw new IllegalStateException("Unexpected type: " + typeElement.getAsString());
        };
    }

    private OutcomeField<Double> getDouble(JsonElement element) {
        return getDouble(element, 0.0);
    }

    private OutcomeField<Double> getDouble(JsonElement element, double defaultValue)
    {
        if (isNull(element))
        {
            return new ConstantOutcomeField<>(defaultValue);
        }

        if (element.isJsonPrimitive())
        {
            var primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return new ConstantOutcomeField<>(primitive.getAsDouble());
            }
            throw new IllegalStateException("Expected number");
        }

        var jsonObject = element.getAsJsonObject();

        var typeElement = jsonObject.get("type");
        if (typeElement == null || typeElement.isJsonNull())
        {
            throw new IllegalStateException("Type missing for outcome object");
        }
        return switch (typeElement.getAsString())
        {
            case "nyakomod:constant" -> getDouble(jsonObject.get("value"));
            case "nyakomod:range" -> {
                var min = getDouble(jsonObject.get("min"));
                var max = getDouble(jsonObject.get("max"));
                yield new RangeOutcomeNumber(min, max);
            }
            case "nyakomod:choice" -> {
                var choices = new ArrayList<OutcomeField<Double>>();
                jsonObject.get("choices").getAsJsonArray().forEach(choiceElement -> {
                    choices.add(getDouble(choiceElement));
                });
                yield new ChoiceOutcomeField<>(choices);
            }
            default -> throw new IllegalStateException("Unexpected type: " + typeElement.getAsString());
        };
    }

    private OutcomeField<Integer> getInteger(JsonElement element) {
        return getInteger(element, 0);
    }

    private OutcomeField<Integer> getInteger(JsonElement element, int defaultValue)
    {
        if (isNull(element))
        {
            return new ConstantOutcomeField<>(defaultValue);
        }

        if (element.isJsonPrimitive())
        {
            var primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return new ConstantOutcomeField<>(primitive.getAsInt());
            }
            throw new IllegalStateException("Expected number");
        }

        var jsonObject = element.getAsJsonObject();

        var typeElement = jsonObject.get("type");
        if (typeElement == null || typeElement.isJsonNull())
        {
            throw new IllegalStateException("Type missing for outcome object");
        }
        return switch (typeElement.getAsString())
        {
            case "nyakomod:constant" -> getInteger(jsonObject.get("value"));
            case "nyakomod:range" -> {
                var min = getInteger(jsonObject.get("min"));
                var max = getInteger(jsonObject.get("max"));
                yield new RangeOutcomeInteger(min, max);
            }
            case "nyakomod:choice" -> {
                var choices = new ArrayList<OutcomeField<Integer>>();
                jsonObject.get("choices").getAsJsonArray().forEach(choiceElement -> {
                    choices.add(getInteger(choiceElement));
                });
                yield new ChoiceOutcomeField<>(choices);
            }
            default -> throw new IllegalStateException("Unexpected type: " + typeElement.getAsString());
        };
    }

    private OutcomeField<String> getString(JsonElement element) {
        return getString(element, null);
    }

    private OutcomeField<String> getString(JsonElement element, @Nullable String defaultValue)
    {
        if (isNull(element))
        {
            if (defaultValue == null)
            {
                throw new IllegalStateException("Expected string");
            }
            return new ConstantOutcomeField<>(defaultValue);
        }

        if (element.isJsonPrimitive())
        {
            var primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return new ConstantOutcomeField<>(primitive.getAsString());
            }
            throw new IllegalStateException("Expected string");
        }

        var jsonObject = element.getAsJsonObject();

        var typeElement = jsonObject.get("type");
        if (typeElement == null || typeElement.isJsonNull())
        {
            throw new IllegalStateException("Type missing for outcome object");
        }
        return switch (typeElement.getAsString())
        {
            case "nyakomod:constant" -> getString(jsonObject.get("value"));
            case "nyakomod:range" -> throw new IllegalStateException("Range is not supported for strings");
            case "nyakomod:choice" -> {
                var choices = new ArrayList<OutcomeField<String>>();
                jsonObject.get("choices").getAsJsonArray().forEach(choiceElement -> {
                    choices.add(getString(choiceElement));
                });
                yield new ChoiceOutcomeField<>(choices);
            }
            default -> throw new IllegalStateException("Unexpected type: " + typeElement.getAsString());
        };
    }

    private OutcomePosition getPosition(JsonElement element)
    {
        if (element == null || element.isJsonNull())
        {
            return new BlockOutcomePosition();
        }

        var jsonObject = element.getAsJsonObject();

        var offsetElement = jsonObject.get("offset");
        Vec3OutcomeNumber offset;

        if (offsetElement == null || offsetElement.isJsonNull())
        {
            offset = new Vec3OutcomeNumber(0, 0, 0);
        }
        else
        {
            var offsetArray = offsetElement.getAsJsonArray();
            if (offsetArray.size() != 3)
            {
                offset = new Vec3OutcomeNumber(0, 0, 0);
            }
            else
            {
                offset = new Vec3OutcomeNumber(
                        getDouble(offsetArray.get(0), 0),
                        getDouble(offsetArray.get(1), 0),
                        getDouble(offsetArray.get(2), 0)
                );
            }
        }

        var typeElement = jsonObject.get("type");
        if (typeElement == null || typeElement.isJsonNull())
        {
            return new BlockOutcomePosition(offset);
        }
        return switch (typeElement.getAsString())
        {
            case "nyakomod:block" -> new BlockOutcomePosition(offset);
            case "nyakomod:absolute" -> new AbsoluteOutcomePosition(offset);
            case "nyakomod:player" -> new PlayerOutcomePosition(offset);
            default -> new BlockOutcomePosition(offset);
        };
    }

    @Override
    public void reload(ResourceManager manager) {
        CondensedMatterOutcomes.clear();

        manager.findResources("condensed_matter_block_outcomes", identifier -> identifier.getPath().endsWith(".json")).forEach((resourceId, resource) -> {
            try {
                var outcomeId = new Identifier(
                        resourceId.getNamespace(),
                        resourceId.getPath().substring(32, resourceId.getPath().length() - 5)
                );

                // Use GSon to parse the JSON file into a JsonObject
                JsonObject shopJson = JsonParser.parseReader(new InputStreamReader(resource.getInputStream())).getAsJsonObject();

                var container = new CondensedMatterOutcomeContainer();

                shopJson.get("outcomes").getAsJsonArray().forEach(outcomeJsonElement -> {
                    var outcomeJson = outcomeJsonElement.getAsJsonObject();
                    container.add(getOutcome(outcomeJson));
                });

                CondensedMatterOutcomes.add(outcomeId, container);

            } catch (Exception e) {
                NyakoMod.LOGGER.error("Error occurred while loading resource json {}", resourceId, e);
            }
        });
    }

    private CondensedMatterOutcome getOutcome(JsonObject outcomeJson) {
        var outcomeType = outcomeJson.get("type").getAsString();
        return switch (outcomeType)
        {
            case "nyakomod:modify_inventory":
                yield new InventoryOutcome(getString(outcomeJson.get("action")));
            case "nyakomod:repeat":
            {
                var count = getInteger(outcomeJson.get("count"), 1);
                var outcomes = new CondensedMatterOutcomeContainer();
                outcomeJson.get("outcomes").getAsJsonArray().forEach(outcomeJsonElement -> {
                    var outcomeJsonElementObject = outcomeJsonElement.getAsJsonObject();
                    outcomes.add(getOutcome(outcomeJsonElementObject));
                });
                yield new RepeatOutcome(count, outcomes);
            }
            case "nyakomod:message":
                yield new MessageOutcome(outcomeJson.get("value"));
            case "nyakomod:command":
                yield new CommandOutcome(getPosition(outcomeJson.get("position")), getString(outcomeJson.get("value")));
            case "nyakomod:function":
                yield new FunctionOutcome(getPosition(outcomeJson.get("position")), getIdentifier(outcomeJson.get("value")));
            case "nyakomod:sound":
                yield new SoundOutcome(getPosition(outcomeJson.get("position")), getIdentifier(outcomeJson.get("sound")));
            case "nyakomod:effect": {
                var showParticlesElement = outcomeJson.get("show_particles");
                if (showParticlesElement == null || showParticlesElement.isJsonNull()) {
                    throw new IllegalStateException("Show particles cannot be null");
                }
                if (!showParticlesElement.isJsonPrimitive()) {
                    throw new IllegalStateException("Show particles must be a boolean");
                }
                var showParticles = showParticlesElement.getAsBoolean();

                var showIconElement = outcomeJson.get("show_icon");
                if (showIconElement == null || showIconElement.isJsonNull()) {
                    throw new IllegalStateException("Show icon cannot be null");
                }
                if (!showIconElement.isJsonPrimitive()) {
                    throw new IllegalStateException("Show icon must be a boolean");
                }
                var showIcon = showIconElement.getAsBoolean();

                yield new EffectOutcome(getIdentifier(outcomeJson.get("effect")), getInteger(outcomeJson.get("duration"), 0), getInteger(outcomeJson.get("amplifier"), 1), showParticles, showIcon);
            }
            case "nyakomod:item": {
                var id = getIdentifier(outcomeJson.get("item"));
                var count = getInteger(outcomeJson.get("count"), 1);
                var nbt = outcomeJson.get("nbt");

                var pos = getPosition(outcomeJson.get("position"));

                if (nbt == null || nbt.isJsonNull()) {
                    yield new ItemOutcome(pos, id, count);
                } else {
                    NbtCompound nbtObject;
                    try {
                        nbtObject = StringNbtReader.parse(nbt.getAsString());
                    } catch (CommandSyntaxException e) {
                        throw new IllegalStateException("NBT is invalid", e);
                    }
                    yield new ItemOutcome(pos, id, count, nbtObject);
                }
            }
            case "nyakomod:block": {
                var blockId = getIdentifier(outcomeJson.get("block"));

                HashMap<String, String> properties = new HashMap<>();

                var propertiesJson = outcomeJson.get("properties");
                if (propertiesJson != null && !propertiesJson.isJsonNull()) {
                    var propertiesObject = propertiesJson.getAsJsonObject();
                    propertiesObject.entrySet().forEach(entry -> {
                        if (entry.getValue().isJsonPrimitive()) {
                            properties.put(entry.getKey(), entry.getValue().getAsString());
                        }
                    });
                }

                var nbt = outcomeJson.get("nbt");

                var pos = getPosition(outcomeJson.get("position"));

                if (nbt == null || nbt.isJsonNull()) {
                    yield new BlockOutcome(pos, blockId, properties);
                } else {
                    NbtCompound nbtObject;
                    try {
                        nbtObject = StringNbtReader.parse(nbt.getAsString());
                    } catch (CommandSyntaxException e) {
                        throw new IllegalStateException("NBT is invalid", e);
                    }
                    yield new BlockOutcome(pos, blockId, properties, nbtObject);
                }
            }
            case "nyakomod:entity":
            {
                var entityId = getIdentifier(outcomeJson.get("entity"));
                var nbt = outcomeJson.get("nbt");

                var pos = getPosition(outcomeJson.get("position"));

                if (nbt == null || nbt.isJsonNull()) {
                    yield new EntityOutcome(pos, entityId);
                } else {
                    NbtCompound nbtObject;
                    try {
                        nbtObject = StringNbtReader.parse(nbt.getAsString());
                    } catch (CommandSyntaxException e) {
                        throw new IllegalStateException("NBT is invalid", e);
                    }
                    yield new EntityOutcome(pos, entityId, nbtObject);
                }
            }
            default:
                throw new IllegalStateException("Unexpected value: " + outcomeType);
        };
    }
}
