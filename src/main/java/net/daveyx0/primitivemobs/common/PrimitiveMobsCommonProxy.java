package net.daveyx0.primitivemobs.common;

import net.daveyx0.multimob.modint.MMModIntegrationRegistry;
import net.daveyx0.primitivemobs.modint.PrimitiveMobsDTIntegration;
import net.daveyx0.primitivemobs.modint.PrimitiveMobsJERIntegration;
import net.minecraftforge.fml.ModList;

public class PrimitiveMobsCommonProxy {
   public PrimitiveMobsDTIntegration DynamicTreesInt;

   public void commonSetup() {
   }

   public void clientSetup() {
   }

   public void postInit() {
      if (ModList.get().isLoaded("jeresources")) {
         MMModIntegrationRegistry.registerModIntegration(new PrimitiveMobsJERIntegration());
      }

      if (ModList.get().isLoaded("dynamictrees")) {
         this.DynamicTreesInt = (PrimitiveMobsDTIntegration)MMModIntegrationRegistry.registerModIntegration(new PrimitiveMobsDTIntegration());
      }

   }
}
