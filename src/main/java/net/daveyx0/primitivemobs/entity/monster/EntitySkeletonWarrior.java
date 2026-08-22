package net.daveyx0.primitivemobs.entity.monster;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.primitivemobs.entity.ai.EntityAISwitchBetweenRangedAndMelee;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.nbt.CompoundTag;

public class EntitySkeletonWarrior extends Skeleton {
   @Nullable
   private ItemStack pendingMainHandWeapon;

   public EntitySkeletonWarrior(EntityType<? extends EntitySkeletonWarrior> type, Level worldIn) {
      super(type, worldIn);
   }

   /**
    * Queues a main-hand swap to run in {@link #customServerAiStep()} after {@link net.minecraft.world.entity.ai.goal.GoalSelector#tick()}
    * finishes, so {@link #setItemSlot} never runs from inside a goal's {@link net.minecraft.world.entity.ai.goal.Goal#tick()} (which would
    * call {@link net.minecraft.world.entity.monster.AbstractSkeleton#reassessWeaponGoal()} and corrupt the goal selector iterator).
    */
   void scheduleWarriorWeaponSwap(ItemStack stack) {
      this.pendingMainHandWeapon = stack.copy();
   }

   @Override
   protected void customServerAiStep() {
      super.customServerAiStep();
      if (this.pendingMainHandWeapon != null) {
         ItemStack stack = this.pendingMainHandWeapon;
         this.pendingMainHandWeapon = null;
         this.setItemSlot(EquipmentSlot.MAINHAND, stack);
      }
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(2, new RestrictSunGoal(this));
      this.goalSelector.addGoal(3, new FleeSunGoal(this, (double)1.0F));
      this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, (double)1.0F, 1.2));
      this.goalSelector.addGoal(4, new EntityAISwitchBetweenRangedAndMelee(this, 1.35, 20, 15.0F));
      this.goalSelector.addGoal(5, new EntityAISwitchWeapons(this, (double)5.0F, (double)6.0F, new ItemStack(Items.IRON_SWORD), new ItemStack(Items.BOW)));
      this.goalSelector.addGoal(6, new RandomStrollGoal(this, (double)1.0F));
      this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
      this.removeVanillaAbstractSkeletonWeaponGoals();
   }

   /**
    * {@link net.minecraft.world.entity.monster.AbstractSkeleton#reassessWeaponGoal()} registers vanilla
    * {@link RangedBowAttackGoal} or a vanilla {@link MeleeAttackGoal} whenever the held weapon changes. This mob uses
    * {@link EntityAISwitchBetweenRangedAndMelee} for that behaviour instead; if both run, movement and attack timing
    * conflict (for example melee goals while holding a bow), which can make combat feel broken.
    */
   private void removeVanillaAbstractSkeletonWeaponGoals() {
      List<Goal> toRemove = new ArrayList<>();
      this.goalSelector.getAvailableGoals().forEach(wrappedGoal -> {
         Goal goal = wrappedGoal.getGoal();
         if (goal instanceof RangedBowAttackGoal) {
            toRemove.add(goal);
         } else if (goal instanceof MeleeAttackGoal && !(goal instanceof EntityAISwitchBetweenRangedAndMelee)) {
            toRemove.add(goal);
         }
      });
      for (Goal goal : toRemove) {
         this.goalSelector.removeGoal(goal);
      }
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Skeleton.createAttributes()
         .add(Attributes.MAX_HEALTH, (double)25.0F);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
      this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05, AttributeModifier.Operation.ADDITION));
      if (this.random.nextFloat() < 0.05F) {
         this.setLeftHanded(true);
      } else {
         this.setLeftHanded(false);
      }

      this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
      if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
         this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
      }

      if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
         this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
      }

      return livingdata;
   }

   public ItemStack getBackItem() {
      return this.getMainHandItem().getItem() == Items.IRON_SWORD ? new ItemStack(Items.BOW) : new ItemStack(Items.IRON_SWORD);
   }

   @Override
   protected AbstractArrow getArrow(ItemStack arrowStack, float velocity) {
      AbstractArrow entityarrow = super.getArrow(arrowStack, velocity);
      if (entityarrow instanceof Arrow) {
         ((Arrow)entityarrow).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
      }

      return entityarrow;
   }

   @Override
   public void reassessWeaponGoal() {
   }

   public class EntityAISwitchWeapons extends Goal {
      Skeleton mob;
      LivingEntity target;
      double minDistance;
      double maxDistance;
      ItemStack weaponOne;
      ItemStack weaponTwo;

      public EntityAISwitchWeapons(Skeleton entitymob, double minDistance, double maxDistance, ItemStack weaponOne, ItemStack weaponTwo) {
         this.mob = entitymob;
         this.minDistance = minDistance;
         this.maxDistance = maxDistance;
         this.weaponOne = weaponOne;
         this.weaponTwo = weaponTwo;
      }

      @Override
      public boolean canUse() {
         this.target = this.mob.getTarget();
         if (this.target == null) {
            return false;
         } else if (!this.target.isAlive()) {
            return false;
         } else {
            return ((double)this.mob.distanceTo(this.target) < this.minDistance && this.mob.getMainHandItem().getItem() != this.weaponOne.getItem() || (double)this.mob.distanceTo(this.target) > this.maxDistance && this.mob.getMainHandItem().getItem() != this.weaponTwo.getItem()) && this.mob.hasLineOfSight(this.target);
         }
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse();
      }

      @Override
      public void stop() {
         this.target = null;
      }

      @Override
      public void tick() {
         if (!(this.mob instanceof EntitySkeletonWarrior warrior)) {
            return;
         }

         if ((double)this.mob.distanceTo(this.target) < this.minDistance) {
            if (this.mob.getMainHandItem().getItem() != this.weaponOne.getItem()) {
               warrior.scheduleWarriorWeaponSwap(this.weaponOne);
            }
         } else if ((double)this.mob.distanceTo(this.target) > this.maxDistance) {
            if (this.mob.getMainHandItem().getItem() != this.weaponTwo.getItem()) {
               warrior.scheduleWarriorWeaponSwap(this.weaponTwo);
            }
         }
      }
   }
}
