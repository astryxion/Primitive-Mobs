package net.daveyx0.primitivemobs.item;

import net.daveyx0.primitivemobs.entity.item.EntityPrimitiveThrowable;
import net.daveyx0.primitivemobs.entity.item.EntitySpiderEgg;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemPrimitiveEgg extends ItemPrimitive {
   Class<? extends Mob> entry;
   int spawnChance;

   public ItemPrimitiveEgg(Class<? extends Mob> entry, int spawnChance, Item.Properties properties) {
      super(properties.stacksTo(16));
      this.entry = entry;
      this.spawnChance = spawnChance;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      if (!playerIn.getAbilities().instabuild) {
         itemstack.shrink(1);
      }

      worldIn.playSound((Player)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
      if (!worldIn.isClientSide) {
         EntityPrimitiveThrowable entityegg;
         if (this.entry.equals(EntityBabySpider.class)) {
            entityegg = new EntitySpiderEgg(worldIn, playerIn, this.entry, this.spawnChance);
         } else {
            entityegg = new EntityPrimitiveThrowable(worldIn, playerIn, this.entry, this.spawnChance);
         }

         entityegg.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.5F, 1.0F);
         worldIn.addFreshEntity(entityegg);
      }

      playerIn.awardStat(Stats.ITEM_USED.get(this));
      return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
   }
}
