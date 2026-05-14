package net.daveyx0.primitivemobs.config;

import java.util.Arrays;
import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PrimitiveMobsConfigSpecial {
   public static String[] treasureSlimeLoot;
   public static String[] hauntedToolLoot;
   public static String[] filchStealLoot;
   public static boolean minerInVillage;
   public static boolean festiveCreeperDestruction;
   public static boolean lostMinerSounds;
   public static int maxSpiderFamilySize;
   public static boolean trollDestruction;
   public static int tameableSlimeChance;
   public static int filchLizardLootChance;
   public static boolean merchantCanSettle;
   public static boolean mimicGenerates;
   public static boolean dodoMycelium;
   public static boolean travelerVisit;
   public static boolean rocketCreeperAlwaysJump;
   public static boolean hauntedToolFullDurability;
   public static boolean groveSpritesPlant;
   public static int mimicSpawnRate;
   public static int[] lostMinerLootRange;

   static ModConfigSpec.BooleanValue MINER_IN_VILLAGE;
   static ModConfigSpec.BooleanValue FESTIVE_CREEPER_DESTRUCTION;
   static ModConfigSpec.BooleanValue LOST_MINER_SOUNDS;
   static ModConfigSpec.IntValue MAX_SPIDER_FAMILY_SIZE;
   static ModConfigSpec.BooleanValue TROLL_DESTRUCTION;
   static ModConfigSpec.IntValue TAMEABLE_SLIME_CHANCE;
   static ModConfigSpec.IntValue FILCH_LIZARD_LOOT_CHANCE;
   static ModConfigSpec.BooleanValue MERCHANT_CAN_SETTLE;
   static ModConfigSpec.BooleanValue MIMIC_GENERATES;
   static ModConfigSpec.BooleanValue DODO_MYCELIUM;
   static ModConfigSpec.BooleanValue TRAVELER_VISIT;
   static ModConfigSpec.BooleanValue ROCKET_CREEPER_ALWAYS_JUMP;
   static ModConfigSpec.IntValue LOST_MINER_LOOT_MIN;
   static ModConfigSpec.IntValue LOST_MINER_LOOT_VARIABLE;
   static ModConfigSpec.BooleanValue HAUNTED_TOOL_FULL_DURABILITY;
   static ModConfigSpec.IntValue MIMIC_SPAWN_RATE;
   static ModConfigSpec.BooleanValue GROVE_SPRITES_PLANT;

   public static void buildConfig(ModConfigSpec.Builder builder) {
      builder.comment("Settings specific to certain mobs.").push("mob_specific_settings");
      MINER_IN_VILLAGE = builder.comment("Enable/Disable if the Miner Villager profession should also spawn naturally in villages").define("minerInVillage", false);
      FESTIVE_CREEPER_DESTRUCTION = builder.comment("Enable/Disable if the Festive Creeper throws tnt that harms the terrain (this way you do not have to disable ALL Creeper explosions with mobGriefing)").define("festiveCreeperDestruction", true);
      MAX_SPIDER_FAMILY_SIZE = builder.comment("Set the maximum amount of Baby Spiders that can potentially spawn with a Mother Spider").defineInRange("maxSpiderFamilySize", 6, 0, 20);
      LOST_MINER_SOUNDS = builder.comment("Enable/Disable if the Lost Miner should make villager sounds").define("lostMinerSounds", true);
      TROLL_DESTRUCTION = builder.comment("Enable/Disable if the Trollager can destroy terrain with its attack").define("trollDestruction", true);
      TAMEABLE_SLIME_CHANCE = builder.comment("Set the chance that a tameable Treasure Slime can spawn in percentage").defineInRange("tameableSlimeChance", 5, 0, 100);
      FILCH_LIZARD_LOOT_CHANCE = builder.comment("Set the chance that a Filch Lizard spawns holding loot in percentage").defineInRange("filchLizardLootChance", 25, 0, 100);
      MERCHANT_CAN_SETTLE = builder.comment("Enable/Disable if the Traveling Merchant can settle after being bribed with an Emerald Block").define("merchantCanSettle", true);
      MIMIC_GENERATES = builder.comment("Enable/Disable if the Mimic chests should generate in caves, instead of spawn like mobs").define("mimicGenerates", true);
      DODO_MYCELIUM = builder.comment("Enable/Disable if Dodos should convert Grass into Mycelium blocks overtime").define("dodoMycelium", true);
      TRAVELER_VISIT = builder.comment("Enable/Disable if the Traveling Merchant should enter houses").define("travelerVisit", true);
      ROCKET_CREEPER_ALWAYS_JUMP = builder.comment("Enable/Disable if rocket creepers should always jump at the player, even if they would hit the ceiling").define("rocketCreeperAlwaysJump", false);
      LOST_MINER_LOOT_MIN = builder.comment("Minimum emeralds received when saving a Lost Miner").defineInRange("lostMinerLootMin", 2, 0, 64);
      LOST_MINER_LOOT_VARIABLE = builder.comment("Variable addition to emeralds received when saving a Lost Miner").defineInRange("lostMinerLootVariable", 2, 0, 64);
      HAUNTED_TOOL_FULL_DURABILITY = builder.comment("Enable/Disable if Haunted Tools should drop full durability items.").define("hauntedToolFullDurability", false);
      MIMIC_SPAWN_RATE = builder.comment("How often a Mimic chest generates. Higher number = rarer. 0 = no mimics chests.").defineInRange("mimicSpawnRate", 25, 0, 1000);
      GROVE_SPRITES_PLANT = builder.comment("Enable/Disable if Grove Sprites should plant sapling they hold.").define("groveSpritesPlant", true);
      builder.pop();
   }

   public static void load() {
      minerInVillage = MINER_IN_VILLAGE.get();
      festiveCreeperDestruction = FESTIVE_CREEPER_DESTRUCTION.get();
      maxSpiderFamilySize = MAX_SPIDER_FAMILY_SIZE.get();
      lostMinerSounds = LOST_MINER_SOUNDS.get();
      trollDestruction = TROLL_DESTRUCTION.get();
      tameableSlimeChance = TAMEABLE_SLIME_CHANCE.get();
      filchLizardLootChance = FILCH_LIZARD_LOOT_CHANCE.get();
      merchantCanSettle = MERCHANT_CAN_SETTLE.get();
      mimicGenerates = MIMIC_GENERATES.get();
      dodoMycelium = DODO_MYCELIUM.get();
      travelerVisit = TRAVELER_VISIT.get();
      rocketCreeperAlwaysJump = ROCKET_CREEPER_ALWAYS_JUMP.get();
      lostMinerLootRange = new int[]{LOST_MINER_LOOT_MIN.get(), LOST_MINER_LOOT_VARIABLE.get()};
      hauntedToolFullDurability = HAUNTED_TOOL_FULL_DURABILITY.get();
      mimicSpawnRate = MIMIC_SPAWN_RATE.get();
      groveSpritesPlant = GROVE_SPRITES_PLANT.get();
   }

   public static String[] getTreasureSlimeLoot() {
      return treasureSlimeLoot;
   }

   public static String[] getHauntedToolLoot() {
      return hauntedToolLoot;
   }

   public static String[] getFilchStealLoot() {
      return filchStealLoot;
   }

   public static boolean getMinerInVillage() {
      return minerInVillage;
   }

   public static boolean getFestiveCreeperDestruction() {
      return festiveCreeperDestruction;
   }

   public static int getMaxSpiderFamilySize() {
      return maxSpiderFamilySize;
   }

   public static int getMimicRarity() {
      return mimicSpawnRate;
   }

   public static boolean getLostMinerSounds() {
      return lostMinerSounds;
   }

   public static boolean getTrollDestruction() {
      return trollDestruction;
   }

   public static int getTameableSlimeChance() {
      return tameableSlimeChance;
   }

   public static int getFilchLizardLootChance() {
      return filchLizardLootChance;
   }

   public static boolean getMimicGeneratesInCaves() {
      return mimicGenerates;
   }

   public static boolean getDodoMycelium() {
      return dodoMycelium;
   }

   public static boolean getTravelerVisit() {
      return travelerVisit;
   }

   public static boolean getRocketCreeperAlwaysJump() {
      return rocketCreeperAlwaysJump;
   }

   public static int[] getLostMinerLootRange() {
      return lostMinerLootRange;
   }

   public static boolean getHauntedToolDurability() {
      return hauntedToolFullDurability;
   }

   public static boolean getGroveSpritesPlant() {
      return groveSpritesPlant;
   }
}
