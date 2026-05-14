package net.daveyx0.primitivemobs.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class PrimitiveMobsLootTables {
   public static final ResourceKey<LootTable> ENTITIES_CHAMELEON = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/chameleon"));
   public static final ResourceKey<LootTable> ENTITIES_ROCKETCREEPER = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/rocket_creeper"));
   public static final ResourceKey<LootTable> ENTITIES_FESTIVECREEPER = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/festive_creeper"));
   public static final ResourceKey<LootTable> ENTITIES_SUPPORTCREEPER = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/support_creeper"));
   public static final ResourceKey<LootTable> ENTITIES_BLAZINGJUGGERNAUT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/blazing_juggernaut"));
   public static final ResourceKey<LootTable> ENTITIES_LILYLURKER = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/lily_lurker"));
   public static final ResourceKey<LootTable> ENTITIES_DODO = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/dodo"));
   public static final ResourceKey<LootTable> ENTITIES_MIMIC = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/mimic"));
   public static final ResourceKey<LootTable> ENTITIES_FLAMESPEWER = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/flame_spewer"));
   public static final ResourceKey<LootTable> ENTITIES_VOIDEYE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/void_eye"));
   public static final ResourceKey<LootTable> ENTITIES_HARPY = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/harpy"));
   public static final ResourceKey<LootTable> ENTITIES_GOBLIN = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/goblin"));
   public static final ResourceKey<LootTable> ENTITIES_MOTHERSPIDER = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/mother_spider"));
   public static final ResourceKey<LootTable> MIMIC_TREASURE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "chests/mimic_treasure"));
   public static final ResourceKey<LootTable> MIMIC_TRAP = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "chests/mimic_trap"));
   public static final ResourceKey<LootTable> FILCHLIZARD_STEAL = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/special/filch_lizard_steal"));
   public static final ResourceKey<LootTable> FILCHLIZARD_SPAWN = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/special/filch_lizard_spawn"));
   public static final ResourceKey<LootTable> HAUNTEDTOOL_SPAWN = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/special/haunted_tool"));
   public static final ResourceKey<LootTable> TREASURESLIME_SPAWN = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "entities/special/treasure_slime"));
   public static final ResourceKey<LootTable> EMPTY = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("primitivemobs", "empty"));

   public static void registerLootTables() {
   }
}
