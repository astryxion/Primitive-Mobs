package net.daveyx0.primitivemobs.entity.monster;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EntityPrimitiveTameableMob extends Monster {
   protected static final EntityDataAccessor<Byte> TAMED = SynchedEntityData.defineId(EntityPrimitiveTameableMob.class, EntityDataSerializers.BYTE);
   protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityPrimitiveTameableMob.class, EntityDataSerializers.OPTIONAL_UUID);
   protected Goal aiSit;

   public EntityPrimitiveTameableMob(EntityType<? extends EntityPrimitiveTameableMob> type, Level worldIn) {
      super(type, worldIn);
      this.setupTamedAI();
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(TAMED, (byte)0);
      builder.define(OWNER_UNIQUE_ID, Optional.empty());
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

   public boolean isPreventingPlayerRest(Player player) {
      return !this.isLeashed();
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

   @OnlyIn(Dist.CLIENT)
   @Override
   public void handleEntityEvent(byte id) {
      if (id == 7) {
         this.playTameEffect(true);
      } else if (id == 6) {
         this.playTameEffect(false);
      } else {
         super.handleEntityEvent(id);
      }

   }

   public boolean isTamed() {
      return ((Byte)this.entityData.get(TAMED) & 4) != 0;
   }

   public void setTamed(boolean tamed) {
      byte b0 = (Byte)this.entityData.get(TAMED);
      if (tamed) {
         this.entityData.set(TAMED, (byte)(b0 | 4));
      } else {
         this.entityData.set(TAMED, (byte)(b0 & -5));
      }

      this.setupTamedAI();
   }

   protected void setupTamedAI() {
   }

   public boolean isSitting() {
      return ((Byte)this.entityData.get(TAMED) & 1) != 0;
   }

   public void setSitting(boolean sitting) {
      byte b0 = (Byte)this.entityData.get(TAMED);
      if (sitting) {
         this.entityData.set(TAMED, (byte)(b0 | 1));
      } else {
         this.entityData.set(TAMED, (byte)(b0 & -2));
      }

   }

   @Nullable
   public UUID getOwnerId() {
      return (UUID)((Optional)this.entityData.get(OWNER_UNIQUE_ID)).orElse(null);
   }

   public void setOwnerId(@Nullable UUID p_184754_1_) {
      this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
   }

   public void setTamedBy(Entity entity) {
      this.setTamed(true);
      this.setOwnerId(entity.getUUID());
   }

   public void setTamedByPlayer(Player player) {
      this.setTamed(true);
      this.setOwnerId(player.getUUID());
   }

   @Nullable
   public LivingEntity getOwner() {
      try {
         UUID uuid = this.getOwnerId();
         if (uuid != null) {
            Player player = this.level().getPlayerByUUID(uuid);
            if (player != null) {
               return player;
            } else {
               LivingEntity entity = EntityUtil.getLoadedEntityByUUID(uuid, this.level());
               return entity != null ? entity : null;
            }
         } else {
            return null;
         }
      } catch (IllegalArgumentException var4) {
         return null;
      }
   }

   public boolean isOwner(LivingEntity entityIn) {
      return entityIn == this.getOwner();
   }

   public Goal getAISit() {
      return this.aiSit;
   }

   public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
      return true;
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
}
