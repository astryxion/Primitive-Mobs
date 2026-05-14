package net.daveyx0.primitivemobs.message;

import net.daveyx0.multimob.message.MMMessageRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PrimitiveMobsMessageRegistry extends MMMessageRegistry {
   private static final String PROTOCOL_VERSION = "1";

   @SubscribeEvent
   public static void registerPayloads(RegisterPayloadHandlersEvent event) {
      PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
      registrar.playToServer(MessagePrimitiveColor.TYPE, MessagePrimitiveColor.STREAM_CODEC, MessagePrimitiveColor::handle);
      registrar.playToServer(MessagePrimitiveColorSap.TYPE, MessagePrimitiveColorSap.STREAM_CODEC, MessagePrimitiveColorSap::handle);
      registrar.playToServer(MessagePrimitiveJumping.TYPE, MessagePrimitiveJumping.STREAM_CODEC, MessagePrimitiveJumping::handle);
      registrar.playToServer(MessageTeleportEye.TYPE, MessageTeleportEye.STREAM_CODEC, MessageTeleportEye::handle);
   }

   public static void registerMessages() {
   }
}
