package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessagePrimitiveJumping implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<MessagePrimitiveJumping> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("primitivemobs", "primitive_jumping"));
   public static final StreamCodec<RegistryFriendlyByteBuf, MessagePrimitiveJumping> STREAM_CODEC = StreamCodec.ofMember(MessagePrimitiveJumping::encode, MessagePrimitiveJumping::decode);
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

   public static void handle(MessagePrimitiveJumping message, IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         UUID id = UUID.fromString(message.text);
         ServerPlayer sender = (ServerPlayer)ctx.player();
         Player truePlayer = sender.level().getPlayerByUUID(id);
         if (truePlayer != null && truePlayer.getVehicle() != null && truePlayer.getVehicle() instanceof EntityBabySpider) {
            ((EntityBabySpider)truePlayer.getVehicle()).setIsJumping(true);
         }
      });
   }

   @Override
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
