package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import net.daveyx0.primitivemobs.entity.monster.EntityVoidEye;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessageTeleportEye implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<MessageTeleportEye> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("primitivemobs", "teleport_eye"));
   public static final StreamCodec<RegistryFriendlyByteBuf, MessageTeleportEye> STREAM_CODEC = StreamCodec.ofMember(MessageTeleportEye::encode, MessageTeleportEye::decode);
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

   public static void handle(MessageTeleportEye message, IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         ServerPlayer sender = (ServerPlayer)ctx.player();
         LivingEntity entity = (LivingEntity)sender.serverLevel().getEntity(UUID.fromString(message.id));
         if (entity != null && entity instanceof EntityVoidEye) {
            ((EntityVoidEye)entity).setTeleports(message.teleport);
         }
      });
   }

   @Override
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
