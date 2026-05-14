package net.daveyx0.primitivemobs.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class PrimitiveMobsConfigMobs {
   public static boolean enableChameleon;
   public static boolean enableTreasureSlime;
   public static boolean enableHauntedTool;
   public static boolean enableGroveSprite;
   public static boolean enableEnchantedBook;
   public static boolean enableFilchLizard;
   public static boolean enableBrainSlime;
   public static boolean enableRocketCreeper;
   public static boolean enableFestiveCreeper;
   public static boolean enableSupportCreeper;
   public static boolean enableSkeletonWarrior;
   public static boolean enableBlazingJuggernaut;
   public static boolean enableLilyLurker;
   public static boolean enableSpiderFamily;
   public static boolean enableTrollager;
   public static boolean enableLostMiner;
   public static boolean enableMerchant;
   public static boolean enableDodo;
   public static boolean enableMimic;
   public static boolean enableSheepman;
   public static boolean enableGoblin;
   public static boolean enableHarpy;
   public static boolean enableFlameSpewer;
   public static boolean enableVoidWatcher;

   static ModConfigSpec.BooleanValue ENABLE_CHAMELEON;
   static ModConfigSpec.BooleanValue ENABLE_TREASURE_SLIME;
   static ModConfigSpec.BooleanValue ENABLE_HAUNTED_TOOL;
   static ModConfigSpec.BooleanValue ENABLE_GROVE_SPRITE;
   static ModConfigSpec.BooleanValue ENABLE_ENCHANTED_BOOK;
   static ModConfigSpec.BooleanValue ENABLE_FILCH_LIZARD;
   static ModConfigSpec.BooleanValue ENABLE_BRAIN_SLIME;
   static ModConfigSpec.BooleanValue ENABLE_ROCKET_CREEPER;
   static ModConfigSpec.BooleanValue ENABLE_FESTIVE_CREEPER;
   static ModConfigSpec.BooleanValue ENABLE_SUPPORT_CREEPER;
   static ModConfigSpec.BooleanValue ENABLE_SKELETON_WARRIOR;
   static ModConfigSpec.BooleanValue ENABLE_BLAZING_JUGGERNAUT;
   static ModConfigSpec.BooleanValue ENABLE_LILY_LURKER;
   static ModConfigSpec.BooleanValue ENABLE_SPIDER_FAMILY;
   static ModConfigSpec.BooleanValue ENABLE_TROLLAGER;
   static ModConfigSpec.BooleanValue ENABLE_LOST_MINER;
   static ModConfigSpec.BooleanValue ENABLE_MERCHANT;
   static ModConfigSpec.BooleanValue ENABLE_DODO;
   static ModConfigSpec.BooleanValue ENABLE_MIMIC;
   static ModConfigSpec.BooleanValue ENABLE_SHEEPMAN;
   static ModConfigSpec.BooleanValue ENABLE_GOBLIN;
   static ModConfigSpec.BooleanValue ENABLE_HARPY;
   static ModConfigSpec.BooleanValue ENABLE_FLAME_SPEWER;
   static ModConfigSpec.BooleanValue ENABLE_VOID_WATCHER;

   public static void buildConfig(ModConfigSpec.Builder builder) {
      builder.comment("Enable/Disable mobs").push("mob_activation_settings");
      ENABLE_CHAMELEON = builder.comment("Enable/Disable the Chameleon").define("enableChameleon", true);
      ENABLE_TREASURE_SLIME = builder.comment("Enable/Disable the Treasure Slime").define("enableTreasureSlime", true);
      ENABLE_HAUNTED_TOOL = builder.comment("Enable/Disable the Haunted Tool").define("enableHauntedTool", true);
      ENABLE_GROVE_SPRITE = builder.comment("Enable/Disable the Grove Sprite").define("enableGroveSprite", true);
      ENABLE_ENCHANTED_BOOK = builder.comment("Enable/Disable the Bewitched Tome").define("enableEnchantedBook", true);
      ENABLE_FILCH_LIZARD = builder.comment("Enable/Disable the Filch Lizard").define("enableFilchLizard", true);
      ENABLE_BRAIN_SLIME = builder.comment("Enable/Disable the Brain Slime").define("enableBrainSlime", true);
      ENABLE_ROCKET_CREEPER = builder.comment("Enable/Disable the Rocket Creeper").define("enableRocketCreeper", true);
      ENABLE_FESTIVE_CREEPER = builder.comment("Enable/Disable the Festive Creeper").define("enableFestiveCreeper", true);
      ENABLE_SUPPORT_CREEPER = builder.comment("Enable/Disable the Support Creeper").define("enableSupportCreeper", true);
      ENABLE_SKELETON_WARRIOR = builder.comment("Enable/Disable the Skeleton Warrior").define("enableSkeletonWarrior", true);
      ENABLE_BLAZING_JUGGERNAUT = builder.comment("Enable/Disable the Blazing Juggernaut").define("enableBlazingJuggernaut", true);
      ENABLE_LILY_LURKER = builder.comment("Enable/Disable the Lily lurker").define("enableLilyLurker", true);
      ENABLE_SPIDER_FAMILY = builder.comment("Enable/Disable the Spider Family").define("enableSpiderFamily", true);
      ENABLE_TROLLAGER = builder.comment("Enable/Disable the Troll").define("enableTrollager", true);
      ENABLE_LOST_MINER = builder.comment("Enable/Disable the Lost Miner").define("enableLostMiner", true);
      ENABLE_MERCHANT = builder.comment("Enable/Disable the Traveling Merchant").define("enableMerchant", true);
      ENABLE_DODO = builder.comment("Enable/Disable the Dodo").define("enableDodo", true);
      ENABLE_MIMIC = builder.comment("Enable/Disable the Mimic").define("enableMimic", true);
      ENABLE_SHEEPMAN = builder.comment("Enable/Disable the Sheepman").define("enableSheepman", true);
      ENABLE_GOBLIN = builder.comment("Enable/Disable the Goblin").define("enableGoblin", true);
      ENABLE_HARPY = builder.comment("Enable/Disable the Harpy").define("enableHarpy", true);
      ENABLE_FLAME_SPEWER = builder.comment("Enable/Disable the Flame Spewer").define("enableFlameSpewer", true);
      ENABLE_VOID_WATCHER = builder.comment("Enable/Disable the Void Eye").define("enableVoidWatcher", true);
      builder.pop();
   }

   public static void load() {
      enableChameleon = ENABLE_CHAMELEON.get();
      enableTreasureSlime = ENABLE_TREASURE_SLIME.get();
      enableHauntedTool = ENABLE_HAUNTED_TOOL.get();
      enableGroveSprite = ENABLE_GROVE_SPRITE.get();
      enableEnchantedBook = ENABLE_ENCHANTED_BOOK.get();
      enableFilchLizard = ENABLE_FILCH_LIZARD.get();
      enableBrainSlime = ENABLE_BRAIN_SLIME.get();
      enableRocketCreeper = ENABLE_ROCKET_CREEPER.get();
      enableFestiveCreeper = ENABLE_FESTIVE_CREEPER.get();
      enableSupportCreeper = ENABLE_SUPPORT_CREEPER.get();
      enableSkeletonWarrior = ENABLE_SKELETON_WARRIOR.get();
      enableBlazingJuggernaut = ENABLE_BLAZING_JUGGERNAUT.get();
      enableLilyLurker = ENABLE_LILY_LURKER.get();
      enableSpiderFamily = ENABLE_SPIDER_FAMILY.get();
      enableTrollager = ENABLE_TROLLAGER.get();
      enableLostMiner = ENABLE_LOST_MINER.get();
      enableMerchant = ENABLE_MERCHANT.get();
      enableDodo = ENABLE_DODO.get();
      enableMimic = ENABLE_MIMIC.get();
      enableSheepman = ENABLE_SHEEPMAN.get();
      enableGoblin = ENABLE_GOBLIN.get();
      enableHarpy = ENABLE_HARPY.get();
      enableFlameSpewer = ENABLE_FLAME_SPEWER.get();
      enableVoidWatcher = ENABLE_VOID_WATCHER.get();
   }
}
