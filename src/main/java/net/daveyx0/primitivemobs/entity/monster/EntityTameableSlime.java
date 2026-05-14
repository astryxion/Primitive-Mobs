package net.daveyx0.primitivemobs.entity.monster;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;

public abstract class EntityTameableSlime extends Slime implements OwnableEntity {
   protected static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(EntityTameableSlime.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityTameableSlime.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityTameableSlime.class, EntityDataSerializers.OPTIONAL_UUID);

   public EntityTameableSlime(EntityType<? extends EntityTameableSlime> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new TameableSlimeMoveHelper(this);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new AITameableSlimeFloat(this));
      this.goalSelector.addGoal(2, new AITameableSlimeAttack(this));
      this.goalSelector.addGoal(3, new AITameableSlimeFaceRandom(this));
      this.goalSelector.addGoal(5, new AITameableSlimeHop(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(TAMED, false);
      builder.define(SITTING, false);
      builder.define(OWNER_UNIQUE_ID, Optional.empty());
   }

   @Override
   public void tick() {
      if (this.isTamed() && !this.isSitting() && this.getTarget() == null) {
         this.setTarget(this.getOwner());
      }

      super.tick();
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      if (this.getOwnerId() == null) {
         compound.putString("OwnerUUID", "");
      } else {
         compound.putString("OwnerUUID", this.getOwnerId().toString());
      }

      compound.putBoolean("Sitting", this.isSitting());
   }

   public boolean isHealingItem(ItemStack stack) {
      return stack.getItem() == Items.SUGAR;
   }

   public boolean isTamingItem(ItemStack stack) {
      return stack.getItem() == Items.SUGAR;
   }

   protected void playHealEffect() {
      SimpleParticleType particletype = ParticleTypes.HEART;

      for(int i = 0; i < 7; ++i) {
         double d0 = this.getRandom().nextGaussian() * 0.02;
         double d1 = this.getRandom().nextGaussian() * 0.02;
         double d2 = this.getRandom().nextGaussian() * 0.02;
         this.level().addParticle(particletype, this.getX() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), this.getY() + (double)0.5F + (double)(this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), d0, d1, d2);
      }

   }

   protected void playSitEffect() {
      SimpleParticleType particletype = ParticleTypes.NOTE;

      for(int i = 0; i < 7; ++i) {
         double d0 = this.getRandom().nextGaussian() * 0.02;
         double d1 = this.getRandom().nextGaussian() * 0.02;
         double d2 = this.getRandom().nextGaussian() * 0.02;
         this.level().addParticle(particletype, this.getX() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), this.getY() + (double)0.5F + (double)(this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), d0, d1, d2);
      }

   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      String s;
      if (compound.contains("OwnerUUID", 8)) {
         s = compound.getString("OwnerUUID");
      } else {
         String s1 = compound.getString("Owner");
         s = s1;
      }

      if (!s.isEmpty()) {
         try {
            this.setOwnerId(UUID.fromString(s));
            this.setTamed(true);
         } catch (Throwable var4) {
            this.setTamed(false);
         }
      }

      this.setSitting(compound.getBoolean("Sitting"));
   }

   public boolean isOwnedBy(Player player) {
      return this.isTamed() && this.isOwner(player);
   }

   protected void playTameEffect(boolean play) {
      SimpleParticleType particletype = ParticleTypes.HEART;
      if (!play) {
         particletype = ParticleTypes.SMOKE;
      }

      for(int i = 0; i < 7; ++i) {
         double d0 = this.getRandom().nextGaussian() * 0.02;
         double d1 = this.getRandom().nextGaussian() * 0.02;
         double d2 = this.getRandom().nextGaussian() * 0.02;
         this.level().addParticle(particletype, this.getX() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), this.getY() + (double)0.5F + (double)(this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), d0, d1, d2);
      }

   }

   public boolean isTamed() {
      return (Boolean)this.entityData.get(TAMED);
   }

   public void setTamed(boolean tamed) {
      this.entityData.set(TAMED, tamed);
   }

   public boolean isSitting() {
      return (Boolean)this.entityData.get(SITTING);
   }

   public void setSitting(boolean sitting) {
      this.entityData.set(SITTING, sitting);
   }

   @Nullable
   public UUID getOwnerId() {
      return (UUID)((Optional)this.entityData.get(OWNER_UNIQUE_ID)).orElse(null);
   }

   @Nullable
   @Override
   public UUID getOwnerUUID() {
      return this.getOwnerId();
   }

   public void setOwnerId(@Nullable UUID uuid) {
      this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
   }

   @Nullable
   public LivingEntity getOwner() {
      try {
         UUID uuid = this.getOwnerId();
         return uuid == null ? null : this.level().getPlayerByUUID(uuid);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   public boolean isOwner(LivingEntity entityIn) {
      return entityIn == this.getOwner();
   }

   @Override
   public void playerTouch(Player entityIn) {
      if (this.isDealsDamage() && !this.isTamed()) {
         this.dealDamage(entityIn);
      }

   }

   @Override
   public PlayerTeam getTeam() {
      if (this.isTamed()) {
         LivingEntity entitylivingbase = this.getOwner();
         if (entitylivingbase != null) {
            return entitylivingbase.getTeam();
         }
      }

      return super.getTeam();
   }

   @Override
   public boolean isAlliedTo(Entity entityIn) {
      if (this.isTamed()) {
         LivingEntity entitylivingbase = this.getOwner();
         if (entityIn == entitylivingbase) {
            return true;
         }

         if (entitylivingbase != null) {
            return entitylivingbase.isAlliedTo(entityIn);
         }
      }

      return super.isAlliedTo(entityIn);
   }

   @Override
   public void die(DamageSource cause) {
      if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
         this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
      }

      super.die(cause);
   }

   static class AITameableSlimeAttack extends Goal {
      private final EntityTameableSlime slime;

      public AITameableSlimeAttack(EntityTameableSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(EnumSet.of(Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         LivingEntity entitylivingbase = this.slime.getTarget();
         if (this.slime.isSitting()) {
            return false;
         } else if (entitylivingbase == null) {
            return false;
         } else if (!entitylivingbase.isAlive()) {
            return false;
         } else if (this.slime.isTamed() && this.slime.isOwner(entitylivingbase) && this.slime.distanceTo(this.slime.getOwner()) > 2.0F) {
            return true;
         } else {
            return !this.slime.isTamed() && (!(entitylivingbase instanceof Player) || !((Player)entitylivingbase).getAbilities().invulnerable);
         }
      }

      @Override
      public void start() {
         super.start();
      }

      @Override
      public boolean canContinueToUse() {
         LivingEntity entitylivingbase = this.slime.getTarget();
         if (this.slime.isSitting()) {
            return false;
         } else if (entitylivingbase == null) {
            return false;
         } else if (!entitylivingbase.isAlive()) {
            return false;
         } else if (this.slime.isTamed() && this.slime.isOwner(entitylivingbase) && this.slime.distanceTo(this.slime.getOwner()) > 2.0F) {
            return true;
         } else {
            return !this.slime.isTamed() && (!(entitylivingbase instanceof Player) || !((Player)entitylivingbase).getAbilities().invulnerable);
         }
      }

      @Override
      public void tick() {
         this.slime.lookAt(this.slime.getTarget(), 10.0F, 10.0F);
         ((TameableSlimeMoveHelper)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), true);
      }
   }

   static class AITameableSlimeFaceRandom extends Goal {
      private final EntityTameableSlime slime;
      private float chosenDegrees;
      private int nextRandomizeTime;

      public AITameableSlimeFaceRandom(EntityTameableSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(EnumSet.of(Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         return this.slime.getTarget() == null && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION));
      }

      @Override
      public void tick() {
         if (--this.nextRandomizeTime <= 0) {
            this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
            this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
         }

         ((TameableSlimeMoveHelper)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
      }
   }

   static class AITameableSlimeFloat extends Goal {
      private final EntityTameableSlime slime;

      public AITameableSlimeFloat(EntityTameableSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
         ((GroundPathNavigation)slimeIn.getNavigation()).setCanFloat(true);
      }

      @Override
      public boolean canUse() {
         return this.slime.isInWater() || this.slime.isInLava();
      }

      @Override
      public void tick() {
         if (this.slime.getRandom().nextFloat() < 0.8F) {
            this.slime.getJumpControl().jump();
         }

         ((TameableSlimeMoveHelper)this.slime.getMoveControl()).setSpeed(1.2);
      }
   }

   static class AITameableSlimeHop extends Goal {
      private final EntityTameableSlime slime;

      public AITameableSlimeHop(EntityTameableSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         return true;
      }

      @Override
      public void tick() {
         ((TameableSlimeMoveHelper)this.slime.getMoveControl()).setSpeed((double)1.0F);
      }
   }

   static class TameableSlimeMoveHelper extends MoveControl {
      private float yRot;
      private int jumpDelay;
      private final EntityTameableSlime slime;
      private boolean isAggressive;

      public TameableSlimeMoveHelper(EntityTameableSlime slimeIn) {
         super(slimeIn);
         this.slime = slimeIn;
         this.yRot = 180.0F * slimeIn.getYRot() / (float)Math.PI;
      }

      public void setDirection(float rotationIn, boolean aggressive) {
         this.yRot = rotationIn;
         this.isAggressive = aggressive;
      }

      public void setSpeed(double speedIn) {
         this.speedModifier = speedIn;
         this.operation = MoveControl.Operation.MOVE_TO;
      }

      @Override
      public void tick() {
         this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
         this.mob.yHeadRot = this.mob.getYRot();
         this.mob.yBodyRot = this.mob.getYRot();
         if (this.slime.isSitting()) {
            if (this.mob.getTarget() != null) {
               this.mob.getLookControl().setLookAt(this.mob.getTarget(), this.mob.yHeadRot, this.mob.getXRot());
            }

            this.mob.setSpeed(0.0F);
         } else if (this.operation != MoveControl.Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
         } else {
            this.operation = MoveControl.Operation.WAIT;
            if (this.mob.onGround()) {
               this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
               if (this.jumpDelay-- <= 0) {
                  this.jumpDelay = this.slime.getJumpDelay();
                  if (this.isAggressive) {
                     this.jumpDelay /= 3;
                  }

                  this.slime.getJumpControl().jump();
                  if (this.slime.doPlayJumpSound()) {
                     this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                  }
               } else {
                  this.slime.xxa = 0.0F;
                  this.slime.zza = 0.0F;
                  this.mob.setSpeed(0.0F);
               }
            } else {
               this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            }
         }

      }
   }
}
