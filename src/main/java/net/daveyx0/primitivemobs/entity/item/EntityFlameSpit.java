package net.daveyx0.primitivemobs.entity.item;

import net.daveyx0.multimob.client.particle.MMParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityFlameSpit extends SmallFireball {
   public EntityFlameSpit(EntityType<? extends EntityFlameSpit> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityFlameSpit(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
      super(worldIn, shooter, accelX, accelY, accelZ);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         for(int i = 0; i < 10; ++i) {
            MMParticles.spawnParticle("flame", (ClientLevel)this.level(), this.getX(), this.getY(), this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F, new float[3]);
         }
      }

      if (this.tickCount > 30) {
         this.discard();
      }

   }
}
