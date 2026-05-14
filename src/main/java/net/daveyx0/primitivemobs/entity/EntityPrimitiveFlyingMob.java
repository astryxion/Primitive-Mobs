package net.daveyx0.primitivemobs.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EntityPrimitiveFlyingMob extends Monster implements FlyingAnimal {
   public float flap;
   public float flapSpeed;
   public float oFlapSpeed;
   public float oFlap;
   public float flapping = 1.0F;

   public EntityPrimitiveFlyingMob(EntityType<? extends EntityPrimitiveFlyingMob> type, Level level) {
      super(type, level);
      this.moveControl = new FlyingMoveControl(this, 10, false);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.FLYING_SPEED, 0.4D)
         .add(Attributes.MAX_HEALTH, 6.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.2D);
   }

   @Override
   protected PathNavigation createNavigation(Level level) {
      FlyingPathNavigation pathnavigateflying = new FlyingPathNavigation(this, level);
      pathnavigateflying.setCanOpenDoors(false);
      pathnavigateflying.setCanFloat(true);
      pathnavigateflying.setCanPassDoors(true);
      return pathnavigateflying;
   }

   protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
      return size.height() * 0.6F;
   }

   @Override
   public void aiStep() {
      super.aiStep();
      this.calculateFlapping();
   }

   private void calculateFlapping() {
      this.oFlap = this.flap;
      this.oFlapSpeed = this.flapSpeed;
      this.flapSpeed = (float)((double)this.flapSpeed + (double)(this.onGround() ? -1 : 6) * 0.3D);
      this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
      if (!this.onGround() && this.flapping < 1.0F) {
         this.flapping = 1.0F;
      }

      this.flapping = (float)((double)this.flapping * 0.9D);
      this.flap += this.flapping * 2.0F;
   }

   @Override
   public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
      int i = Mth.floor(this.getX());
      int j = Mth.floor(this.getBoundingBox().minY);
      int k = Mth.floor(this.getZ());
      BlockPos blockpos = new BlockPos(i, j, k);
      BlockState blockstate = level.getBlockState(blockpos.below());
      Block block = blockstate.getBlock();
      return block instanceof LeavesBlock || block == Blocks.GRASS_BLOCK || blockstate.is(BlockTags.LOGS) || block == Blocks.AIR && level.getMaxLocalRawBrightness(blockpos) > 8 && super.checkSpawnRules(level, spawnType);
   }

   @Override
   public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
      return false;
   }

   @Override
   protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
   }

   @Override
   public boolean doHurtTarget(Entity target) {
      return target.hurt(this.damageSources().mobAttack(this), 3.0F);
   }

   @Override
   public boolean isFlapping() {
      return true;
   }

   @Override
   protected void doPush(Entity entity) {
      if (!(entity instanceof Player)) {
         super.doPush(entity);
      }
   }

   @Override
   public boolean isFlying() {
      return !this.onGround();
   }
}
