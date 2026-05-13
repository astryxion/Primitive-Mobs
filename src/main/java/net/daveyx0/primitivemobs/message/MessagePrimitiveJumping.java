package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import java.util.function.Supplier;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class MessagePrimitiveJumping {
   private String text;

   public MessagePrimitiveJumping() {
   }

   public MessagePrimitiveJumping(String text) {
      this.text = text;
   }

   public static void encode(MessagePrimitiveJumping message, FriendlyByteBuf buf) {
      buf.writeUtf(message.text);
   }

   public static MessagePrimitiveJumping decode(FriendlyByteBuf buf) {
      MessagePrimitiveJumping message = new MessagePrimitiveJumping();
      message.text = buf.readUtf(32767);
      return message;
   }

   public static void handle(MessagePrimitiveJumping message, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         UUID id = UUID.fromString(message.text);
         if (ctx.get().getSender() != null) {
            Player truePlayer = ctx.get().getSender().level().getPlayerByUUID(id);
            if (truePlayer.getVehicle() != null && truePlayer.getVehicle() instanceof EntityBabySpider) {
               ((EntityBabySpider)truePlayer.getVehicle()).setIsJumping(true);
            }
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
