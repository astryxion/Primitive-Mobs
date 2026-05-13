package net.daveyx0.primitivemobs.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class PrimitiveMobsConfig {
   public static ForgeConfigSpec CONFIG_SPEC;

   static {
      ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
      PrimitiveMobsConfigMobs.buildConfig(builder);
      PrimitiveMobsConfigSpecial.buildConfig(builder);
      CONFIG_SPEC = builder.build();
   }

   public static void load() {
      reloadConfig();
   }

   private static void reloadConfig() {
      PrimitiveMobsConfigMobs.load();
      PrimitiveMobsConfigSpecial.load();
   }

   @SubscribeEvent
   public static void onConfigChanged(ModConfigEvent event) {
      reloadConfig();
   }
}
