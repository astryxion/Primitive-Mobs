package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.daveyx0.primitivemobs.item.ItemGroveSpriteSap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessagePrimitiveColorSap implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<MessagePrimitiveColorSap> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("primitivemobs", "primitive_color_sap"));
   public static final StreamCodec<RegistryFriendlyByteBuf, MessagePrimitiveColorSap> STREAM_CODEC = StreamCodec.ofMember(MessagePrimitiveColorSap::encode, MessagePrimitiveColorSap::decode);
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

   public static void handle(MessagePrimitiveColorSap message, IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         ServerPlayer sender = (ServerPlayer)ctx.player();
         Entity entity = sender.serverLevel().getEntity(UUID.fromString(message.id));
         if (entity != null && entity instanceof EntityGroveSprite) {
            EntityGroveSprite sprite = (EntityGroveSprite)entity;
            if (!sprite.getOffhandItem().isEmpty()) {
               ItemGroveSpriteSap.setColor(sprite.getOffhandItem(), message.color);
               ItemGroveSpriteSap.setSapLogState(sprite, sprite.getOffhandItem());
            }
         }
      });
   }

   @Override
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
