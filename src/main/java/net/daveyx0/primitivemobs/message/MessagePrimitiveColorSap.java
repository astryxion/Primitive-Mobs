package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import java.util.function.Supplier;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.daveyx0.primitivemobs.item.ItemGroveSpriteSap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class MessagePrimitiveColorSap {
   private int color;
   private String id;

   public MessagePrimitiveColorSap() {
   }

   public MessagePrimitiveColorSap(int color, String id) {
      this.color = color;
      this.id = id;
   }

   public static void encode(MessagePrimitiveColorSap message, FriendlyByteBuf buf) {
      buf.writeInt(message.color);
      buf.writeUtf(message.id);
   }

   public static MessagePrimitiveColorSap decode(FriendlyByteBuf buf) {
      MessagePrimitiveColorSap message = new MessagePrimitiveColorSap();
      message.color = buf.readInt();
      message.id = buf.readUtf(32767);
      return message;
   }

   public static void handle(MessagePrimitiveColorSap message, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         Entity entity = ctx.get().getSender().serverLevel().getEntity(UUID.fromString(message.id));
         if (entity != null && entity instanceof EntityGroveSprite) {
            EntityGroveSprite sprite = (EntityGroveSprite)entity;
            if (!sprite.getOffhandItem().isEmpty()) {
               ItemGroveSpriteSap.setColor(sprite.getOffhandItem(), message.color);
               ItemGroveSpriteSap.setSapLogState(sprite, sprite.getOffhandItem());
            }
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
