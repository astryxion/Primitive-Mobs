package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import net.daveyx0.primitivemobs.item.ItemCamouflageArmor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MessagePrimitiveColor implements CustomPacketPayload {
   public static final CustomPacketPayload.Type<MessagePrimitiveColor> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("primitivemobs", "primitive_color"));
   public static final StreamCodec<RegistryFriendlyByteBuf, MessagePrimitiveColor> STREAM_CODEC = StreamCodec.ofMember(MessagePrimitiveColor::encode, MessagePrimitiveColor::decode);
   private int color;
   private EquipmentSlot slot;
   private String id;

   public MessagePrimitiveColor() {
   }

   public MessagePrimitiveColor(int color, EquipmentSlot slot, String id) {
      this.color = color;
      this.slot = slot;
      this.id = id;
   }

   public static void encode(MessagePrimitiveColor message, FriendlyByteBuf buf) {
      buf.writeInt(message.color);
      buf.writeByte(message.slot.ordinal());
      buf.writeUtf(message.id);
   }

   public static MessagePrimitiveColor decode(FriendlyByteBuf buf) {
      MessagePrimitiveColor message = new MessagePrimitiveColor();
      message.color = buf.readInt();
      message.slot = EquipmentSlot.values()[buf.readByte()];
      message.id = buf.readUtf(32767);
      return message;
   }

   public static void handle(MessagePrimitiveColor message, IPayloadContext ctx) {
      ctx.enqueueWork(() -> {
         ServerPlayer sender = (ServerPlayer)ctx.player();
         LivingEntity entity = (LivingEntity)sender.serverLevel().getEntity(UUID.fromString(message.id));
         if (entity != null) {
            ItemStack armor = entity.getItemBySlot(message.slot);
            if (armor.getItem() instanceof ItemCamouflageArmor) {
               ItemCamouflageArmor camo = (ItemCamouflageArmor)armor.getItem();
               camo.setColor(armor, message.color);
            }
         }
      });
   }

   @Override
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
