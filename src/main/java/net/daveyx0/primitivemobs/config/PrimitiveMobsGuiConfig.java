package net.daveyx0.primitivemobs.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PrimitiveMobsGuiConfig extends Screen {
   private final Screen parentScreen;

   public PrimitiveMobsGuiConfig(Screen parentScreen) {
      super(Component.translatable("primitivemobs.config.title"));
      this.parentScreen = parentScreen;
   }

   @Override
   public void onClose() {
      this.minecraft.setScreen(this.parentScreen);
   }
}
