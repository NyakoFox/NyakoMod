# written in 3 seconds cause i hate making 500 files just to make a block don't judge my code
import json

MODID = "nyakomod"

def color(code):
    return f"\033[0;{code}m"

def main():
    block_name = input(f"{color('37;1;4')}Enter the name of the block (ex. Diamond Launcher):\n{color('31;1')}> {color('0')}")
    block_id = input(f"{color('37;1;4')}Enter the ID of the block (ex. diamond_launcher):\n{color('31;1')}> {color('0')}")
    block_class = input(f"{color('37;1;4')}Enter the class name of the block (ex. DiamondLauncherBlock):\n{color('31;1')}> {color('0')}")
    print(f"{color('37;1;4;6')}Nice block")

    with open(f"src/main/resources/assets/{MODID}/lang/en_us.json") as f:
        data = json.load(f)

    data[f"block.{MODID}.{block_id}"] = block_name

    with open(f"src/main/resources/assets/{MODID}/lang/en_us.json", "w") as write_file:
        json.dump(data, write_file, sort_keys=True, indent=4)



    with open(f"src/main/resources/assets/{MODID}/blockstates/{block_id}.json", "x") as file:
        file.write(
"""{
  \"variants\": {
    \"\": { "model": \"""" + MODID +""":block/""" + block_id + """\" }
  }
}""")

    print(f"{color('0')}src/main/resources/assets/{MODID}/blockstates/{block_id}.json")

    with open(f"src/main/resources/assets/{MODID}/models/block/{block_id}.json", "x") as file:
        file.write(
"""{
  \"parent\": \"block/cube_all\",
  \"textures\": {
    \"\": { "all": \"""" + MODID +""":block/""" + block_id + """\" }
  }
}""")

    print(f"src/main/resources/assets/{MODID}/models/block/{block_id}.json")

    with open(f"src/main/resources/assets/{MODID}/models/item/{block_id}.json", "x") as file:
        file.write(
"""{
    "parent": \"""" + MODID +""":block/""" + block_id + """\"
}""")

    print(f"src/main/resources/assets/{MODID}/models/item/{block_id}.json")

    with open(f"src/main/resources/data/{MODID}/loot_tables/blocks/{block_id}.json", "x") as file:
        file.write(
"""{
  "type": "minecraft:block",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        {
          "type": "minecraft:item",
          "name": \"""" + MODID + """:""" + block_id + """"
        }
      ],
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
        }
      ]
    }
  ]
}""")

    print(f"src/main/resources/data/{MODID}/loot_tables/blocks/{block_id}.json")

    with open(f"src/main/java/gay/nyako/{MODID}/{block_class}.java", "x") as file:
        file.write(
f"""package gay.nyako.nyakomod;

import net.minecraft.block.Block;

public class {block_class} extends Block {{
    public {block_class}(Settings settings) {{
        super(settings);
    }}
}}""")

    print(f"src/main/java/gay/nyako/{MODID}/{block_class}.java")



    print(f"{color('0')}it has been done and congratulation")


if __name__ == "__main__":
    main()
