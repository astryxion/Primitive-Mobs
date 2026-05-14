package net.daveyx0.primitivemobs.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;

public class PrimitiveMobsConfig {
   public static ModConfigSpec CONFIG_SPEC;

   static {
      ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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
