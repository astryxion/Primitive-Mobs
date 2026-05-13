package net.daveyx0.primitivemobs.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGoblinMace extends SwordItem {
   public static final float damagePercentage = 0.1F;

   public static final Tier GOBLIN_METAL = new Tier() {
      @Override public int getUses() { return 400; }
      @Override public float getSpeed() { return 6.0F; }
      @Override public float getAttackDamageBonus() { return 2.5F; }
      @Override public int getLevel() { return 2; }
      @Override public int getEnchantmentValue() { return 10; }
      @Override public Ingredient getRepairIngredient() { return Ingredient.of(Items.GOLD_INGOT); }
   };

   public ItemGoblinMace(Item.Properties properties) {
      super(GOBLIN_METAL, 3, -3.0F, properties);
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      return repair.getItem() == Items.GOLD_INGOT;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      tooltip.add(Component.literal("Damages armor more quickly; independent of armor strength."));
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
   }

   @Override
   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
      return true;
   }
}
