package net.daveyx0.primitivemobs.entity.ai;

import java.util.Set;
import net.daveyx0.multimob.entity.ai.EntityAITemptItemStack;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;

public class EntityAIGroveSpriteTempt extends EntityAITemptItemStack {
   public EntityAIGroveSpriteTempt(PathfinderMob temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, Set<ItemStack> temptItemIn) {
      super(temptedEntityIn, speedIn, scaredByPlayerMovementIn, temptItemIn);
   }

   @Override
   public void start() {
      super.start();
      if (this.temptedEntity instanceof EntityGroveSprite) {
         ((EntityGroveSprite)this.temptedEntity).setIsBegging(true);
      }
   }

   @Override
   public void stop() {
      super.stop();
      if (this.temptedEntity instanceof EntityGroveSprite) {
         ((EntityGroveSprite)this.temptedEntity).setIsBegging(false);
      }
   }
}
