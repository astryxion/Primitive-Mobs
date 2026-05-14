package net.daveyx0.primitivemobs.client;

import net.daveyx0.primitivemobs.common.PrimitiveMobsCommonProxy;
import net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry;
import net.daveyx0.primitivemobs.core.PrimitiveMobsItems;

public class PrimitiveMobsClientProxy extends PrimitiveMobsCommonProxy {
   public void clientSetup() {
      PrimitiveMobsEntityRegistry.registerRenderers();
      super.clientSetup();
   }

   public void postInit() {
      super.postInit();
   }
}
