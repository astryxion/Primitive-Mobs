package net.daveyx0.primitivemobs.entity.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;

public class EntityFlameSpit extends SmallFireball {
   public EntityFlameSpit(EntityType<? extends EntityFlameSpit> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityFlameSpit(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
      super(net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry.FLAME_SPIT.get(), worldIn);
      this.setOwner(shooter);
      this.setPos(shooter.getX(), shooter.getEyeY() - 0.5D, shooter.getZ());
      double len = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
      if (len == 0.0D) {
         len = 1.0D;
      }
      this.xPower = accelX / len * 0.1D;
      this.yPower = accelY / len * 0.1D;
      this.zPower = accelZ / len * 0.1D;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
         this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
      }

      if (this.tickCount > 80) {
         this.discard();
      }

   }
}
