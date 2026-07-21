package net.daveyx0.primitivemobs.core;

import net.daveyx0.multimob.spawn.MMConfigSpawnEntry;
import net.daveyx0.multimob.spawn.MMSpawnRegistry;

/**
 * Spawn entry defaults used by Multi Mob's FinalizeSpawn filters (rarity, height, structures).
 * NaturalSpawner selection weights come from {@code data/primitivemobs/forge/biome_modifier/*.json}
 * and must stay in sync with the values below.
 */
public class PrimitiveMobsSpawnRegistry extends MMSpawnRegistry {
   public static void registerSpawns() {
      registerSpawnEntry((new MMConfigSpawnEntry("_BrainSlime_Beach", "primitivemobs:brain_slime", 20, true)).setBiomeTypes(new String[]{"BEACH"}).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_HauntedTool", "primitivemobs:haunted_tool", 15, true)).setupBaseMobSpawnEntry(false).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_RocketCreeper", "primitivemobs:rocket_creeper", 10, true)).setupBaseMobSpawnEntry(false).setHeightLevel(50, -1).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_FestiveCreeper", "primitivemobs:festive_creeper", 5, true)).setupBaseMobSpawnEntry(false).setAdditionalRarity(3));
      registerSpawnEntry((new MMConfigSpawnEntry("_FestiveCreeper_Nether", "primitivemobs:festive_creeper", 5, true)).setDimensions(new int[]{-1}).setAdditionalRarity(3));
      registerSpawnEntry((new MMConfigSpawnEntry("_SupportCreeper", "primitivemobs:support_creeper", 8, true)).setupBaseMobSpawnEntry(false).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_TreasureSlime", "primitivemobs:treasure_slime", 5, true)).setupBaseMobSpawnEntry(false).setAdditionalRarity(4));
      registerSpawnEntry((new MMConfigSpawnEntry("_BewitchedTome", "primitivemobs:bewitched_tome", 5, true)).setupBaseMobSpawnEntry(false).setAdditionalRarity(4));
      registerSpawnEntry((new MMConfigSpawnEntry("_BewitchedTome_Stronghold", "primitivemobs:bewitched_tome", 40, true)).setupBaseMobSpawnEntry(false).setHeightLevel(-1, 45).setStructures(new String[]{"Stronghold"}));
      registerSpawnEntry((new MMConfigSpawnEntry("_SkeletonWarrior", "primitivemobs:skeleton_warrior", 20, true)).setupBaseMobSpawnEntry(false).setGroupSize(1, 2).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_SpiderFamily", "primitivemobs:mother_spider", 8, true)).setupBaseMobSpawnEntry(false).setAdditionalRarity(3));
      registerSpawnEntry((new MMConfigSpawnEntry("_BlazingJuggernaut", "primitivemobs:blazing_juggernaut", 5, true)).setDimensions(new int[]{-1}).setAdditionalRarity(3));
      registerSpawnEntry((new MMConfigSpawnEntry("_Trollager", "primitivemobs:trollager", 2, true)).setupBaseMobSpawnEntry(false).setNeedsMoreSpace(true).setAdditionalRarity(5));
      registerSpawnEntry((new MMConfigSpawnEntry("_Trollager_Underground", "primitivemobs:trollager", 8, true)).setupBaseMobSpawnEntry(false).setHeightLevel(-1, 45).setNeedsMoreSpace(true).setAdditionalRarity(3));
      registerSpawnEntry((new MMConfigSpawnEntry("_VoidEye_Overworld", "primitivemobs:void_eye", 3, true)).setupBaseMobSpawnEntry(false).setDimensions(new int[]{0}).setHeightLevel(-1, 20).setAdditionalRarity(4));
      // Community: harpies too common — half-ish of old feel, singles only, light rarity gate.
      registerSpawnEntry((new MMConfigSpawnEntry("_Harpy", "primitivemobs:harpy", 8, true)).setHeightLevel(100, -1).setGroupSize(1, 1).setOverrideCanSpawnHere(true).setSpawnBlocks(new String[]{"minecraft:grass_block", "minecraft:stone", "minecraft:dirt", "minecraft:snow_block"}).setAdditionalRarity(3));
      registerSpawnEntry((new MMConfigSpawnEntry("_Goblin", "primitivemobs:goblin", 12, true)).setupBaseMobSpawnEntry(false).setHeightLevel(-1, 30).setGroupSize(1, 2).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_Goblin_Mineshaft", "primitivemobs:goblin", 40, true)).setupBaseMobSpawnEntry(false).setStructures(new String[]{"Mineshaft"}).setGroupSize(1, 2));
      registerSpawnEntry((new MMConfigSpawnEntry("_Chameleon_Forest", "primitivemobs:chameleon", 15, true)).setupBaseAnimalSpawnEntry(false).setBiomeTypes(new String[]{"FOREST"}).setGroupSize(1, 2).setCreatureType("CREATURE"));
      registerSpawnEntry((new MMConfigSpawnEntry("_Chameleon_Jungle", "primitivemobs:chameleon", 15, true)).setupBaseAnimalSpawnEntry(false).setBiomeTypes(new String[]{"JUNGLE"}).setGroupSize(1, 2).setCreatureType("CREATURE"));
      registerSpawnEntry((new MMConfigSpawnEntry("_LostMiner", "primitivemobs:lost_miner", 2, true)).setupBaseMobSpawnEntry(false).setHeightLevel(-1, 45).setAdditionalRarity(8).setCreatureType("CREATURE"));
      // Community: merchants like rare zombie villagers — very low weight + strong rarity.
      registerSpawnEntry((new MMConfigSpawnEntry("_TravelingMerchant", "primitivemobs:traveling_merchant", 1, true)).setupBaseAnimalSpawnEntry(false).setAdditionalRarity(80).setCreatureType("CREATURE"));
      registerSpawnEntry((new MMConfigSpawnEntry("_Dodo", "primitivemobs:dodo", 10, true)).setupBaseAnimalSpawnEntry(false).setSpawnBlocks(new String[]{"minecraft:mycelium"}).setGroupSize(1, 2).setCreatureType("CREATURE"));
      registerSpawnEntry((new MMConfigSpawnEntry("_FilchLizard", "primitivemobs:filch_lizard", 10, true)).setBiomeTypes(new String[]{"SANDY"}).setCreatureType("CREATURE").setGroupSize(1, 1).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_GroveSprite", "primitivemobs:grovesprite", 12, true)).setupBaseAnimalSpawnEntry(false).setBiomeTypes(new String[]{"FOREST"}).setCreatureType("CREATURE").setGroupSize(1, 1).setAdditionalRarity(2));
      registerSpawnEntry((new MMConfigSpawnEntry("_Sheepman", "primitivemobs:sheepman", 10, true)).setDimensions(new int[]{-1}).setCreatureType("CREATURE").setGroupSize(1, 2).setAdditionalRarity(2));
      // Flame spewer uses MONSTER so it shares the vanilla monster cap (Special Mobs style).
      registerSpawnEntry((new MMConfigSpawnEntry("_FlameSpewer_Overworld", "primitivemobs:flame_spewer", 1, true)).setDimensions(new int[]{0}).setNeedsMoreSpace(true).setSpawnType("LAVA").setCreatureType("MONSTER").setAdditionalRarity(40));
      registerSpawnEntry((new MMConfigSpawnEntry("_FlameSpewer_Nether", "primitivemobs:flame_spewer", 1, true)).setDimensions(new int[]{-1}).setNeedsMoreSpace(true).setSpawnType("LAVA").setCreatureType("MONSTER").setAdditionalRarity(20));
      registerSpawnEntry((new MMConfigSpawnEntry("_LilyLurker", "primitivemobs:lily_lurker", 15, true)).setHeightLevel(50, -1).setSpawnType("WATER").setBiomeTypes(new String[]{"SWAMP"}).setCreatureType("WATERCREATURE").setAdditionalRarity(2));
   }
}
