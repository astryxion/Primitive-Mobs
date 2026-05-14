package net.daveyx0.primitivemobs.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collection;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import java.util.List;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EntityHauntedTool extends Monster implements IMultiMob {
   public float floatingb;
   public float floatingc;
   public float floatingd;
   public float floatinge;
   public float floatingh;

   public EntityHauntedTool(EntityType<? extends EntityHauntedTool> type, Level worldIn) {
      super(type, worldIn);
      this.floatingb = 0.0F;
      this.floatingc = 0.0F;
      this.floatingh = 1.0F;
      this.armorDropChances[0] = 0.0F;
      this.armorDropChances[1] = 0.0F;
   }

   @Override
   protected void registerGoals() {
      int prio = 0;
      ++prio;
      this.goalSelector.addGoal(prio, new FloatGoal(this));
      ++prio;
      this.goalSelector.addGoal(prio, new MeleeAttackGoal(this, 1.3, false));
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
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.FOLLOW_RANGE, (double)40.0F);
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData) {
      while(this.getMainHandItem().isEmpty() && !this.level().isClientSide) {
         ItemStack tool = getSpawnLootItem(this, this.getSpawnLootTable(), new ItemStack(Items.IRON_SWORD));
         this.setItemSlot(EquipmentSlot.MAINHAND, tool);
         if (tool.isDamageableItem()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)getHealthFromTool(tool));
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(getDamageFromHeldItem(this));
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(getSpeedFromHeldItem(this));
            this.setHealth((float)this.getAttribute(Attributes.MAX_HEALTH).getBaseValue());
         }
      }

      return super.finalizeSpawn(levelAccessor, difficulty, spawnType, spawnData);
   }

   @Nullable
   private static ItemStack getSpawnLootItem(net.minecraft.world.entity.Entity entityIn, ResourceKey<LootTable> resourceLootTable, ItemStack defaultItem) {
      if (resourceLootTable != null && entityIn.level() instanceof ServerLevel) {
         ServerLevel serverLevel = (ServerLevel) entityIn.level();
         LootTable loottable = serverLevel.getServer().reloadableRegistries().getLootTable(resourceLootTable);
         LootParams lootparams = new LootParams.Builder(serverLevel)
            .withParameter(LootContextParams.THIS_ENTITY, entityIn)
            .withParameter(LootContextParams.ORIGIN, entityIn.position())
            .create(LootContextParamSets.GIFT);
         List<ItemStack> items = loottable.getRandomItems(lootparams);
         if (!items.isEmpty()) {
            return items.get(0);
         }
      }
      return defaultItem;
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
         this.level().addParticle(ParticleTypes.SMOKE, this.getX() + (double)(this.getRandom().nextFloat() / 2.0F - this.getRandom().nextFloat() / 2.0F), this.getY() + (double)(this.getRandom().nextFloat() / 2.0F - this.getRandom().nextFloat() / 2.0F) + (double)1.0F, this.getZ() + (double)(this.getRandom().nextFloat() / 2.0F - this.getRandom().nextFloat() / 2.0F), (double)0.0F, (double)0.0F, (double)0.0F);
      }

      if (this.getMainHandItem().isEmpty()) {
         this.discard();
      }

      super.aiStep();
   }

   protected Item getDropItem() {
      return this.getMainHandItem().getItem();
   }

   @Nullable
   @Override
   protected ResourceKey<LootTable> getDefaultLootTable() {
      return PrimitiveMobsLootTables.EMPTY;
   }

   @Nullable
   protected ResourceKey<LootTable> getSpawnLootTable() {
      return PrimitiveMobsLootTables.HAUNTEDTOOL_SPAWN;
   }

   @Override
   public boolean shouldDropExperience() {
      return true;
   }

   public ItemEntity dropItemStack(ItemStack itemIn, float offsetY) {
      return this.spawnAtLocation(itemIn, offsetY);
   }

   @Override
   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
      ItemStack stack = this.getMainHandItem();
      if (!stack.isEmpty() && !this.level().isClientSide) {
         if (!PrimitiveMobsConfigSpecial.getHauntedToolDurability()) {
            int lootingModifier = source.getEntity() instanceof LivingEntity livingentity ? EnchantmentHelper.getEnchantmentLevel(livingentity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), livingentity) : 0;
            if (lootingModifier > 3) {
               lootingModifier = 3;
            }

            RandomSource rand = RandomSource.create();
            int itemDurability = stack.getMaxDamage();
            int minDurability = itemDurability / 10;
            int maxDurability = itemDurability / 2;
            minDurability *= lootingModifier + 1;
            maxDurability *= lootingModifier + 2;
            stack.setDamageValue(itemDurability - Mth.randomBetweenInclusive(rand, minDurability, maxDurability));
         }

         this.dropItemStack(stack, 1.0F);
      }

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
      return SoundEvents.GENERIC_EXTINGUISH_FIRE;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.GENERIC_EXTINGUISH_FIRE;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.GENERIC_EXTINGUISH_FIRE;
   }

   public static float getHealthFromTool(ItemStack tool) {
      if (!tool.isEmpty() && tool.isDamageableItem()) {
         float health = (float)tool.getMaxDamage() / 10.0F;
         if (health > 100.0F) {
            health = 100.0F;
         }

         return health;
      } else {
         return 10.0F;
      }
   }

   public static double getDamageFromHeldItem(Mob entity) {
      ItemStack stack = entity.getMainHandItem();
      if (!stack.isEmpty() && stack.isDamageableItem()) {
      }

      Collection<AttributeModifier> modifiers = stack.getAttributeModifiers().modifiers().stream().filter(entry -> entry.attribute().is(Attributes.ATTACK_DAMAGE)).map(entry -> entry.modifier()).toList();
      if (modifiers != null && !modifiers.isEmpty()) {
         Object[] mods = modifiers.toArray(new Object[modifiers.size()]);
         AttributeModifier attribute = (AttributeModifier)mods[0];
         double attackDamage = attribute.amount() / (double)2.0F;
         if (attackDamage <= (double)1.0F) {
            attackDamage = (double)1.0F;
         } else if (attackDamage > (double)8.0F) {
            attackDamage = (double)8.0F;
         }

         return attackDamage;
      } else {
         return (double)1.0F;
      }
   }

   public static double getSpeedFromHeldItem(Mob entity) {
      ItemStack stack = entity.getMainHandItem();
      if (!stack.isEmpty() && stack.isDamageableItem()) {
         Collection<AttributeModifier> modifiers = stack.getAttributeModifiers().modifiers().stream().filter(entry -> entry.attribute().is(Attributes.ATTACK_SPEED)).map(entry -> entry.modifier()).toList();
         if (modifiers != null && !modifiers.isEmpty()) {
            Object[] mods = modifiers.toArray(new Object[modifiers.size()]);
            AttributeModifier attribute = (AttributeModifier)mods[0];
            double attackSpeed = (double)0.5F - attribute.amount() * (double)-1.0F * 0.1;
            if (attackSpeed <= 0.1) {
               attackSpeed = 0.1;
            } else if (attackSpeed > 0.3) {
               attackSpeed = 0.3;
            }

            return attackSpeed;
         }
      }

      return 0.2;
   }
}
