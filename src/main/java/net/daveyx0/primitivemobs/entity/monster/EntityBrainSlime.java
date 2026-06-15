package net.daveyx0.primitivemobs.entity.monster;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.daveyx0.multimob.client.particle.MMParticles;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMParticle;
import net.daveyx0.multimob.network.MMNetworkWrapper;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.core.PrimitiveMobsSoundEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.PacketDistributor;

public class EntityBrainSlime extends Slime implements IMultiMob {
   public int attackDelay;
   public float suckingb;
   public float suckingc;
   public float suckingd;
   public float suckinge;
   public float suckingh;
   private boolean wasOnGround;
   public int maxStack;
   private boolean checkedAI = false;
   private final NearestAttackableTargetGoal<Animal> hostilityAI = new NearestAttackableTargetGoal<>(this, Animal.class, true);
   private static final EntityDataAccessor<Integer> SATURATION = SynchedEntityData.defineId(EntityBrainSlime.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> ATTACK_DELAY = SynchedEntityData.defineId(EntityBrainSlime.class, EntityDataSerializers.INT);
   protected static final EntityDataAccessor<Optional<UUID>> VICTIM_UNIQUE_ID = SynchedEntityData.defineId(EntityBrainSlime.class, EntityDataSerializers.OPTIONAL_UUID);
   protected Block spawnableBlock;

   public EntityBrainSlime(EntityType<? extends EntityBrainSlime> type, Level worldIn) {
      super(type, worldIn);
      this.spawnableBlock = Blocks.GRASS_BLOCK;
      this.moveControl = new SlimeMoveHelper(this);
      this.setAttackDelay(0);
      this.suckingb = 0.0F;
      this.suckingc = 0.0F;
      this.suckingh = 1.0F;
      this.maxStack = 10;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new AISlimeFloat(this));
      this.goalSelector.addGoal(2, new AISlimeAttack(this));
      this.goalSelector.addGoal(3, new AISlimeFaceRandom(this));
      this.goalSelector.addGoal(4, new AISlimeHop(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ATTACK_DELAY, 0);
      this.entityData.define(SATURATION, 0);
      this.entityData.define(VICTIM_UNIQUE_ID, Optional.empty());
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(net.minecraft.world.level.ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
      this.setSize(this.random.nextInt(3) + 1, true);
      return livingdata;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Mob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 10.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.3D)
         .add(Attributes.ATTACK_DAMAGE, 2.0D);
   }

   @Override
   public void tick() {
      if (this.getSaturation() >= 10) {
         this.setSaturation(this.getSaturation() + 1);
         if (this.getSaturation() >= 100) {
            this.setSize(this.getSize() + 1, true);
            this.setSaturation(0);
            this.setAttackDelay(50);
            if (!this.level().isClientSide) {
               MMMessageRegistry.getNetwork().send(PacketDistributor.ALL.noArg(), new MessageMMParticle(9, 10, (float)this.getX() + 0.5F, (float)this.getY() + 0.5F, (float)this.getZ() + 0.5F, (double)0.0F, (double)0.0F, (double)0.0F, 0));
            }
         }
      }

      this.suckinge = this.suckingb;
      this.suckingd = this.suckingc;
      this.setAttackDelay(this.getAttackDelay() - 1);
      if (this.getTarget() != null) {
         this.setVictimId(this.getTarget().getUUID());
         this.entityData.set(VICTIM_UNIQUE_ID, this.entityData.get(VICTIM_UNIQUE_ID));
      }

      if (!this.isPassenger() && this.getVictim() != null) {
         this.suckingb = 0.0F;
         this.suckingc = 0.0F;
         this.suckingh = 1.0F;
         if (this.getAttackDelay() <= 0 && this.distanceToSqr(this.getVictim()) < (double)6.0F) {
            this.startRidingTopEntity(this.getVictim());
            this.setAttackDelay(5);
         }
      }

      if (this.isPassenger()) {
         if (this.getAttackDelay() <= 0) {
            this.attackRiddenEntity();
            this.setAttackDelay(20);
         }

         this.suckingc = (float)((double)this.suckingc + 3.2);
         if (this.suckingc < 0.0F) {
            this.suckingc = 0.0F;
         }

         if (this.suckingc > 0.2F) {
            this.suckingc = 0.2F;
         }

         if (this.suckingh < 0.2F) {
            this.suckingh = 0.2F;
         }

         this.suckingh = (float)((double)this.suckingh * 0.9);
         this.suckingb += this.suckingh * 2.0F;
         if (this.getVictim() == null || this.isRemoved()) {
            this.dismountSelf();
         }
      }

      super.tick();
   }

   public boolean hasBrainToSuck(LivingEntity entity) {
      if (this.getSaturation() >= 10) {
         return false;
      } else {
         if (entity instanceof net.minecraft.world.entity.TamableAnimal) {
            net.minecraft.world.entity.TamableAnimal tameable = (net.minecraft.world.entity.TamableAnimal)entity;
            if (tameable.isTame()) {
               return false;
            }
         }

         if (entity instanceof net.minecraft.world.entity.PathfinderMob) {
            net.minecraft.world.entity.PathfinderMob creature = (net.minecraft.world.entity.PathfinderMob)entity;
            if (creature.getMobType() == MobType.UNDEAD) {
               return false;
            }
         }

         return !(entity instanceof Slime);
      }
   }

   public void startRidingTopEntity(Entity entity) {
      Entity top = getTopPassenger(entity);
      if (top != null && !top.isRemoved()) {
         if (top instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)top;
            this.startRiding(player, false);
            MMNetworkWrapper.sendPacket(player, new ClientboundSetPassengersPacket(player));
         } else {
            this.startRiding(top, false);
         }
      }

   }

   public static Entity getTopPassenger(Entity entity) {
      Entity top = entity;

      while(entity.isVehicle()) {
         List<Entity> list = entity.getPassengers();
         if (!list.isEmpty()) {
            entity = (Entity)list.get(0);
            top = entity;
         }
      }

      return top;
   }

   public static Entity getBottomPassenger(Entity entity) {
      Entity top = entity;

      while(entity.isPassenger()) {
         Entity ridingEntity = entity.getVehicle();
         if (ridingEntity != null && !ridingEntity.isRemoved()) {
            entity = ridingEntity;
            top = ridingEntity;
         }
      }

      return top;
   }

   public void attackRiddenEntity() {
      Entity entity = getBottomPassenger(this);
      if (entity != null && !entity.isRemoved() && entity instanceof LivingEntity) {
         LivingEntity entityLiving = (LivingEntity)entity;
         if (this.hasBrainToSuck(entityLiving)) {
            this.damageHelmetOrEntity(entityLiving);
            this.setSaturation(this.getSaturation() + 1);
         } else {
            this.dismountSelf();
            this.setAttackDelay(20);
         }
      }

   }

   public void dismountSelf() {
      this.setVictimId((UUID)null);
      this.setAttackDelay(20);
      this.stopRiding();
   }

   public void damageHelmetOrEntity(LivingEntity base) {
      ItemStack stack = base.getItemBySlot(EquipmentSlot.HEAD);
      int damage = (int)this.getAttackDamage();
      if (!stack.isEmpty() && stack.getItem().canBeDepleted() && stack.isDamageableItem()) {
         stack.hurtAndBreak(damage, base, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.HEAD);
         });
      } else if (base.hurt(this.damageSources().mobAttack(this), damage >= 6 ? (float)damage : 6.0F)) {
         this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         this.doEnchantDamageEffects(this, base);
      }

   }

   @Override
   protected boolean spawnCustomParticles() {
      if (this.level().isClientSide) {
         int i = this.getSize();

         for(int j = 0; j < i * 8; ++j) {
            float f = this.random.nextFloat() * ((float)Math.PI * 2F);
            float f1 = this.random.nextFloat() * 0.5F + 0.5F;
            float f2 = Mth.sin(f) * (float)i * 0.5F * f1;
            float f3 = Mth.cos(f) * (float)i * 0.5F * f1;
            double d0 = this.getX() + (double)f2;
            double d1 = this.getZ() + (double)f3;
            MMParticles.spawnParticle("slime", (ClientLevel)this.level(), d0, this.getBoundingBox().minY, d1, (double)0.0F, (double)0.0F, (double)0.0F, new float[]{209.0F, 165.0F, 189.0F});
         }
      }

      return true;
   }

   @Override
   protected float getAttackDamage() {
      return (float)((this.getSize() + 1) / 3);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (this.isPassenger()) {
         this.dismountSelf();
         this.setAttackDelay(10);
      }

      return super.hurt(source, amount);
   }

   @Override
   protected boolean isDealsDamage() {
      return false;
   }

   @Override
   public void setSize(int size, boolean resetHealth) {
      super.setSize(size, resetHealth);
   }

   @Override
   public MoveControl getMoveControl() {
      return this.moveControl;
   }

   @Override
   public void push(Entity entityIn) {
      super.push(entityIn);
   }

   @Override
   public void playerTouch(Player entityIn) {
   }

   @Override
   public double getMyRidingOffset() {
      return this.getVehicle() != null && this.getVehicle() instanceof Player ? (double)0.25F : (double)0.0F;
   }

   public void setAttackDelay(int delay) {
      this.entityData.set(ATTACK_DELAY, delay);
   }

   public int getAttackDelay() {
      return (Integer)this.entityData.get(ATTACK_DELAY);
   }

   public void setSaturation(int delay) {
      this.entityData.set(SATURATION, delay);
   }

   public int getSaturation() {
      return (Integer)this.entityData.get(SATURATION);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("CurrentsAttackDelay", this.getAttackDelay());
      compound.putInt("Saturation", this.getSaturation());
      if (this.getVictimId() == null) {
         compound.putString("VictimUUID", "");
      } else {
         compound.putString("VictimUUID", this.getVictimId().toString());
      }

   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setAttackDelay(compound.getInt("CurrentAttackDelay"));
      this.setSaturation(compound.getInt("Saturation"));
      String s;
      if (compound.contains("VictimUUID", 8)) {
         s = compound.getString("VictimUUID");
      } else {
         String s1 = compound.getString("Victim");
         s = s1;
      }

      if (!s.isEmpty()) {
         try {
            this.setVictimId(UUID.fromString(s));
         } catch (Throwable var4) {
         }
      }

   }

   @Nullable
   public UUID getVictimId() {
      return (UUID)((Optional)this.entityData.get(VICTIM_UNIQUE_ID)).orElse(null);
   }

   public void setVictimId(@Nullable UUID p_184754_1_) {
      this.entityData.set(VICTIM_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
   }

   @Nullable
   public LivingEntity getVictim() {
      try {
         UUID uuid = this.getVictimId();
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

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
      int i = Mth.floor(this.getX());
      int j = Mth.floor(this.getBoundingBox().minY);
      int k = Mth.floor(this.getZ());
      BlockPos blockpos = new BlockPos(i, j, k);
      return this.level().getBlockState(blockpos.below()).getBlock() == this.spawnableBlock;
   }

   static class AISlimeAttack extends Goal {
      private final EntityBrainSlime slime;

      public AISlimeAttack(EntityBrainSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         LivingEntity entitylivingbase = this.slime.getTarget();
         if (this.slime.getSaturation() >= 10) {
            return false;
         } else if (entitylivingbase != null && this.slime.isPassengerOfSameVehicle(entitylivingbase)) {
            return false;
         } else if (entitylivingbase != null && this.slime.distanceTo(entitylivingbase) > 12.0F) {
            return false;
         } else {
            return entitylivingbase == null ? false : (!entitylivingbase.isAlive() ? false : !(entitylivingbase instanceof Player) || !((Player)entitylivingbase).getAbilities().invulnerable);
         }
      }

      @Override
      public void start() {
         super.start();
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse();
      }

      @Override
      public void tick() {
         this.slime.lookAt(this.slime.getTarget(), 10.0F, 10.0F);
         LivingEntity entitylivingbase = this.slime.getTarget();
         ((SlimeMoveHelper)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
      }
   }

   static class AISlimeFaceRandom extends Goal {
      private final EntityBrainSlime slime;
      private float chosenDegrees;
      private int nextRandomizeTime;

      public AISlimeFaceRandom(EntityBrainSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         return this.slime.getTarget() == null && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION));
      }

      @Override
      public void tick() {
         if (--this.nextRandomizeTime <= 0) {
            this.nextRandomizeTime = 10 + this.slime.getRandom().nextInt(60);
            this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
         }

         ((SlimeMoveHelper)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
      }
   }

   static class AISlimeHop extends Goal {
      private final EntityBrainSlime slime;

      public AISlimeHop(EntityBrainSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         return true;
      }

      @Override
      public void tick() {
         ((SlimeMoveHelper)this.slime.getMoveControl()).setSpeed((double)1.0F);
      }
   }

   static class AISlimeFloat extends Goal {
      private final EntityBrainSlime slime;

      public AISlimeFloat(EntityBrainSlime slimeIn) {
         this.slime = slimeIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
         slimeIn.getNavigation().setCanFloat(true);
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

         ((SlimeMoveHelper)this.slime.getMoveControl()).setSpeed(1.2);
      }
   }

   static class SlimeMoveHelper extends MoveControl {
      private float yRot;
      private int jumpDelay;
      private final EntityBrainSlime slime;
      private boolean isAggressive;

      public SlimeMoveHelper(EntityBrainSlime slimeIn) {
         super(slimeIn);
         this.slime = slimeIn;
         this.yRot = 180.0F * slimeIn.getYRot() / (float)Math.PI;
      }

      public void setDirection(float p_179920_1_, boolean p_179920_2_) {
         this.yRot = p_179920_1_;
         this.isAggressive = p_179920_2_;
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
         if (this.operation != MoveControl.Operation.MOVE_TO) {
            this.mob.setZza(0.0F);
         } else {
            this.operation = MoveControl.Operation.WAIT;
            if (this.mob.onGround()) {
               this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
               if (this.jumpDelay-- <= 0) {
                  this.jumpDelay = this.slime.getJumpDelay();
                  if (this.isAggressive && this.slime.getSaturation() < 10) {
                     if (!this.slime.isTiny()) {
                        this.slime.playSound(PrimitiveMobsSoundEvents.ENTITY_BRAINSLIME_CHARGE.get(), this.slime.getSoundVolume(), ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                     }

                     this.performChargeAttack();
                  } else {
                     this.slime.getJumpControl().jump();
                     if (!this.slime.isTiny()) {
                        this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRandom().nextFloat() - this.slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                     }
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

      public void performChargeAttack() {
         LivingEntity entity = this.mob.getTarget();
         if (entity != null) {
            this.mob.setDeltaMovement(
               this.mob.getDeltaMovement().add(
                  (entity.getX() - this.mob.getX()) / (double)8.0F,
                  0,
                  (entity.getZ() - this.mob.getZ()) / (double)8.0F
               )
            );
            double newY = this.mob.getDeltaMovement().y + (entity.getY() - this.mob.getY()) / (double)8.0F;
            if (newY <= (double)0.0F) {
               newY = (double)0.0F;
            }
            newY += (double)0.5F;
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().x, newY, this.mob.getDeltaMovement().z);
         }

      }
   }
}
