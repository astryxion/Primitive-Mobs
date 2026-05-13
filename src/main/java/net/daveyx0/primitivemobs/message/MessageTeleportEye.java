package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import java.util.function.Supplier;
import net.daveyx0.primitivemobs.entity.monster.EntityVoidEye;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class MessageTeleportEye {
   private boolean teleport;
   private String id;

   public MessageTeleportEye() {
   }

   public MessageTeleportEye(boolean tele, String id) {
      this.teleport = tele;
      this.id = id;
   }

   public static void encode(MessageTeleportEye message, FriendlyByteBuf buf) {
      buf.writeBoolean(message.teleport);
      buf.writeUtf(message.id);
   }

   public static MessageTeleportEye decode(FriendlyByteBuf buf) {
      MessageTeleportEye message = new MessageTeleportEye();
      message.teleport = buf.readBoolean();
      message.id = buf.readUtf(32767);
      return message;
   }

   public static void handle(MessageTeleportEye message, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         LivingEntity entity = (LivingEntity)ctx.get().getSender().serverLevel().getEntity(UUID.fromString(message.id));
         if (entity != null && entity instanceof EntityVoidEye) {
            ((EntityVoidEye)entity).setTeleports(message.teleport);
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
