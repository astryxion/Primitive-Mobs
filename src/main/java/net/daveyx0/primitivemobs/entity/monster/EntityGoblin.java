package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry;
import net.daveyx0.primitivemobs.core.PrimitiveMobsItems;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.nbt.CompoundTag;

public class EntityGoblin extends Monster implements IMultiMob {
   public EntityGoblin(EntityType<? extends EntityGoblin> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4, false));
      this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
      this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
      this.applyEntityAI();
   }

   protected void applyEntityAI() {
      this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, (double)1.0F, false, 4, () -> false));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, ZombifiedPiglin.class));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, false));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.FOLLOW_RANGE, (double)35.0F)
         .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
         .add(Attributes.ATTACK_DAMAGE, (double)3.0F);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
      this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(PrimitiveMobsItems.GOBLIN_MACE.get()));
      return super.finalizeSpawn(level, difficulty, reason, livingdata, dataTag);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.VINDICATOR_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.VINDICATOR_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.VINDICATOR_DEATH;
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_GOBLIN;
   }

   @Override
   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F;
   }
}
