package net.daveyx0.primitivemobs.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraft.client.gui.screens.Screen;

@OnlyIn(Dist.CLIENT)
public class PrimitiveMobsFactoryGui {
   public static ConfigScreenHandler.ConfigScreenFactory getFactory() {
      return new ConfigScreenHandler.ConfigScreenFactory((mc, parentScreen) -> new PrimitiveMobsGuiConfig(parentScreen));
   }
}
