package net.daveyx0.primitivemobs.entity.item;

import net.daveyx0.primitivemobs.core.PrimitiveMobsItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EntitySpiderEgg extends EntityPrimitiveThrowable {
   public EntitySpiderEgg(EntityType<? extends EntitySpiderEgg> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntitySpiderEgg(Level worldIn, Player playerIn, Class<? extends Mob> entry, int spawnChance) {
      super(worldIn, playerIn, entry, spawnChance);
   }

   @Override
   protected void onHit(HitResult result) {
      if (!this.level().isClientSide) {
         ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY() + (double)0.5F, this.getZ(), new ItemStack(PrimitiveMobsItems.SPIDER_EGGSHELL.get()));
         entityitem.setDefaultPickUpDelay();
         this.level().addFreshEntity(entityitem);
      }

      super.onHit(result);
   }

   @Override
   public ItemStack getItemFromEntity() {
      return new ItemStack(PrimitiveMobsItems.SPIDER_EGG_ITEM.get());
   }
}
