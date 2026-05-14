package net.daveyx0.primitivemobs.entity.monster;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EntityEnchantedBook extends Monster implements IMultiMob {
   public float floatingb;
   public float floatingc;
   public float floatingd;
   public float floatinge;
   public float floatingh;

   public EntityEnchantedBook(EntityType<? extends EntityEnchantedBook> type, Level worldIn) {
      super(type, worldIn);
      this.floatingb = 0.0F;
      this.floatingc = 0.0F;
      this.floatingh = 1.0F;
   }

   @Override
   protected void registerGoals() {
      int prio = 0;
      ++prio;
      this.goalSelector.addGoal(prio, new FloatGoal(this));
      ++prio;
      this.goalSelector.addGoal(prio, new MeleeAttackGoal(this, 1.4, false));
      ++prio;
      this.goalSelector.addGoal(prio, new MoveTowardsRestrictionGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomStrollGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new LookAtPlayerGoal(this, Player.class, 8.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomLookAroundGoal(this));
      int attackPrio = 1;
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new HurtByTargetGoal(this));
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.FOLLOW_RANGE, (double)40.0F)
         .add(Attributes.MOVEMENT_SPEED, 0.23000000298023224)
         .add(Attributes.MAX_HEALTH, (double)20.0F)
         .add(Attributes.ATTACK_DAMAGE, (double)2.0F);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
      return super.finalizeSpawn(levelAccessor, difficulty, spawnType, spawnData);
   }

   @Override
   public void aiStep() {
      this.floatinge = this.floatingb;
      this.floatingd = this.floatingc;
      this.floatingc = (float)((double)this.floatingc + 1.6);
      if (this.floatingc < 0.0F) {
         this.floatingc = 0.0F;
      }

      if (this.floatingc > 0.2F) {
         this.floatingc = 0.2F;
      }

      if (this.floatingh < 0.2F) {
         this.floatingh = 0.2F;
      }

      this.floatingh = (float)((double)this.floatingh * 0.9);
      this.floatingb += this.floatingh * 1.0F;
      if (this.level().isClientSide) {
         this.level().addParticle(ParticleTypes.ENCHANT, this.getX() + (double)(this.getRandom().nextFloat() - this.getRandom().nextFloat()), this.getY() + (double)(this.getRandom().nextFloat() - this.getRandom().nextFloat()) + (double)1.0F, this.getZ() + (double)(this.getRandom().nextFloat() - this.getRandom().nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
      }

      super.aiStep();
   }

   @Nullable
   @Override
   protected ResourceKey<LootTable> getDefaultLootTable() {
      return PrimitiveMobsLootTables.EMPTY;
   }

   @Override
   public boolean shouldDropExperience() {
      return true;
   }

   @Override
   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
      if (!this.level().isClientSide) {
         int chance = this.getRandom().nextInt(2);
         if (chance == 0) {
            Holder<Enchantment> enchantment = getRandomEnchantment();
            int maxPower = enchantment.value().getMaxLevel();
            int randomPower = 1 + this.getRandom().nextInt(maxPower);
            if (randomPower > 0 && enchantment != null) {
               this.spawnAtLocation(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, randomPower)), 1.0F);
            }
         } else {
            this.spawnAtLocation(new ItemStack(Items.BOOK), 1.0F);
         }
      }

   }

   private Holder<Enchantment> getRandomEnchantment() {
      var enchantments = this.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements().toList();
      return enchantments.get(this.getRandom().nextInt(enchantments.size()));
   }

   @Override
   public boolean onClimbable() {
      return this.horizontalCollision;
   }

   @Override
   public void jumpFromGround() {
   }

   protected SoundEvent getFallDamageSound(int heightIn) {
      return null;
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   @Override
   protected void playStepSound(BlockPos pos, BlockState state) {
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return null;
   }

   protected SoundEvent getHurtSound() {
      return SoundEvents.BOOK_PUT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.BOOK_PUT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.BOOK_PAGE_TURN;
   }

   public boolean checkSpawnRules(Level level, MobSpawnType spawnType) {
      return super.checkSpawnRules(level, spawnType) && this.getY() < (double)40.0F;
   }
}
