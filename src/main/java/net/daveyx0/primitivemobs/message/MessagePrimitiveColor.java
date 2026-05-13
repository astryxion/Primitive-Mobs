package net.daveyx0.primitivemobs.message;

import java.util.UUID;
import java.util.function.Supplier;
import net.daveyx0.primitivemobs.item.ItemCamouflageArmor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class MessagePrimitiveColor {
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

   public static void handle(MessagePrimitiveColor message, Supplier<NetworkEvent.Context> ctx) {
      ctx.get().enqueueWork(() -> {
         LivingEntity entity = (LivingEntity)ctx.get().getSender().serverLevel().getEntity(UUID.fromString(message.id));
         if (entity != null) {
            ItemStack armor = entity.getItemBySlot(message.slot);
            if (armor.getItem() instanceof ItemCamouflageArmor) {
               ItemCamouflageArmor camo = (ItemCamouflageArmor)armor.getItem();
               camo.setColor(armor, message.color);
            }
         }
      });
      ctx.get().setPacketHandled(true);
   }
}
