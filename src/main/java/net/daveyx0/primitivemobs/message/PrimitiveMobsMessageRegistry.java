package net.daveyx0.primitivemobs.message;

import net.daveyx0.multimob.message.MMMessageRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PrimitiveMobsMessageRegistry extends MMMessageRegistry {
   private static int id = 0;
   private static final String PROTOCOL_VERSION = "1";
   private static SimpleChannel primitiveNetwork;

   public static void registerMessages() {
      primitiveNetwork = NetworkRegistry.newSimpleChannel(
         new ResourceLocation("primitivemobs", "main"),
         () -> PROTOCOL_VERSION,
         PROTOCOL_VERSION::equals,
         PROTOCOL_VERSION::equals
      );
      primitiveNetwork.registerMessage(id++, MessagePrimitiveColor.class, MessagePrimitiveColor::encode, MessagePrimitiveColor::decode, MessagePrimitiveColor::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      primitiveNetwork.registerMessage(id++, MessagePrimitiveColorSap.class, MessagePrimitiveColorSap::encode, MessagePrimitiveColorSap::decode, MessagePrimitiveColorSap::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      primitiveNetwork.registerMessage(id++, MessagePrimitiveJumping.class, MessagePrimitiveJumping::encode, MessagePrimitiveJumping::decode, MessagePrimitiveJumping::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
      primitiveNetwork.registerMessage(id++, MessageTeleportEye.class, MessageTeleportEye::encode, MessageTeleportEye::decode, MessageTeleportEye::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
   }

   public static SimpleChannel getPrimitiveNetwork() {
      return primitiveNetwork;
   }
}
