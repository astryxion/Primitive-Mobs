package net.daveyx0.primitivemobs.entity.monster;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.client.particle.MMParticles;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.util.ColorUtil;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EntityTreasureSlime extends EntityTameableSlime implements IMultiMob {
   private float R = 0.0F;
   private float G = 0.0F;
   private float B = 0.0F;
   private float NewR;
   private float NewG;
   private float NewB;
   private int colorSpeed = 10;
   private ItemStack currentItem;
   private boolean wasOnGround;

   public EntityTreasureSlime(EntityType<? extends EntityTreasureSlime> type, Level worldIn) {
      super(type, worldIn);
      if (this.getSkinRGB()[0] == 0.0F && this.getSkinRGB()[1] == 0.0F && this.getSkinRGB()[2] == 0.0F) {
         this.setSkinRGB(new int[]{255, 255, 255});
      }

      this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
      this.setDropChance(EquipmentSlot.OFFHAND, 0.0F);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Slime.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 10.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.3D)
         .add(Attributes.ATTACK_DAMAGE, 2.0D);
   }

   @Override
   protected boolean spawnCustomParticles() {
      return true;
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(net.minecraft.world.level.ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      int chance = PrimitiveMobsConfigSpecial.getTameableSlimeChance();
      if (!this.isTamed() && chance < 100 && (chance <= 0 || this.random.nextInt(100 / chance) != 0)) {
         while(this.getMainHandItem().isEmpty() && !this.level().isClientSide) {
            this.setItemSlot(EquipmentSlot.MAINHAND, getSpawnLootItem(this, this.getSpawnLootTable(), new ItemStack(Items.SLIME_BALL)));
         }
      }

      return super.finalizeSpawn(worldIn, difficulty, reason, livingdata);
   }

   @Nullable
   private static ItemStack getSpawnLootItem(net.minecraft.world.entity.Entity entityIn, ResourceKey<LootTable> resourceLootTable, ItemStack defaultItem) {
      if (resourceLootTable != null && entityIn.level() instanceof net.minecraft.server.level.ServerLevel) {
         net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) entityIn.level();
         net.minecraft.world.level.storage.loot.LootTable loottable = serverLevel.getServer().reloadableRegistries().getLootTable(resourceLootTable);
         net.minecraft.world.level.storage.loot.LootParams lootparams = new net.minecraft.world.level.storage.loot.LootParams.Builder(serverLevel)
            .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY, entityIn)
            .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, entityIn.position())
            .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.GIFT);
         List<ItemStack> items = loottable.getRandomItems(lootparams);
         if (!items.isEmpty()) {
            return items.get(0);
         }
      }
      return defaultItem;
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return !this.isTamed();
   }

   @Override
   public void tick() {
      if (this.isInWater() && !this.horizontalCollision) {
         this.setDeltaMovement(this.getDeltaMovement().x, 0.02, this.getDeltaMovement().z);
      }

      if (this.level().isClientSide) {
         this.changeColor(this);
      }

      if (this.R != this.NewR || this.G != this.NewG || this.B != this.NewB) {
         for(int i = 0; i < this.colorSpeed; ++i) {
            if (this.R > this.NewR) {
               --this.R;
            } else if (this.R < this.NewR) {
               ++this.R;
            }

            if (this.G > this.NewG) {
               --this.G;
            } else if (this.G < this.NewG) {
               ++this.G;
            }

            if (this.B > this.NewB) {
               --this.B;
            } else if (this.B < this.NewB) {
               ++this.B;
            }
         }
      }

      if (this.isTamed()) {
         if (this.random.nextInt(200) == 0) {
            this.level().addParticle(ParticleTypes.HEART, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)1.0F, (double)1.0F, (double)1.0F);
         }

         if (this.isSitting()) {
            this.setTarget((LivingEntity)null);
         }
      }

      if (this.onGround() && !this.wasOnGround && this.level().isClientSide) {
         int i = this.getSize();

         for(int j = 0; j < i * 8; ++j) {
            float f = this.random.nextFloat() * ((float)Math.PI * 2F);
            float f1 = this.random.nextFloat() * 0.5F + 0.5F;
            float f2 = Mth.sin(f) * (float)i * 0.5F * f1;
            float f3 = Mth.cos(f) * (float)i * 0.5F * f1;
            double d0 = this.getX() + (double)f2;
            double d1 = this.getZ() + (double)f3;
            MMParticles.spawnParticle("slime", (ClientLevel)this.level(), d0, this.getBoundingBox().minY, d1, (double)0.0F, (double)0.0F, (double)0.0F, this.getSkinRGB());
         }

         this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         this.targetSquish = -0.5F;
      } else if (!this.onGround() && this.wasOnGround) {
         this.targetSquish = 1.0F;
      }

      this.wasOnGround = this.onGround();
      super.tick();
   }

   @SuppressWarnings("unchecked")
   protected EntityTreasureSlime createInstance() {
      if (this.isTamed()) {
         EntityTreasureSlime entityslime = new EntityTreasureSlime((EntityType<? extends EntityTreasureSlime>)(EntityType<?>)this.getType(), this.level());
         if (this.isTamed() && this.getOwner() != null) {
            entityslime.setTamed(true);
            entityslime.setOwnerId(this.getOwnerId());
         }

         return entityslime;
      } else {
         EntityTreasureSlime entityslime = new EntityTreasureSlime((EntityType<? extends EntityTreasureSlime>)(EntityType<?>)this.getType(), this.level());
         entityslime.setSkinRGB(this.getSkinRGB());
         entityslime.setItemSlot(EquipmentSlot.MAINHAND, this.getMainHandItem());
         return entityslime;
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("wasOnGround", this.wasOnGround);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.wasOnGround = compound.getBoolean("wasOnGround");
   }

   @Override
   public void remove(Entity.RemovalReason reason) {
      int i = this.getSize();
      if (!this.level().isClientSide && i > 1 && this.getHealth() <= 0.0F) {
         int j = 2 + this.random.nextInt(3);

         for(int k = 0; k < j; ++k) {
            float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
            float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
            EntityTreasureSlime entityslime = this.createInstance();
            if (this.hasCustomName()) {
               entityslime.setCustomName(this.getCustomName());
            }

            if (this.isNoAi()) {
               entityslime.setNoAi(true);
            }

            entityslime.setSize(i / 2, true);
            entityslime.moveTo(this.getX() + (double)f, this.getY() + (double)0.5F, this.getZ() + (double)f1, this.random.nextFloat() * 360.0F, 0.0F);
            this.level().addFreshEntity(entityslime);
         }

         if (this.isTamed()) {
            ItemStack stack = this.getMainHandItem();
            if (!stack.isEmpty() && !this.level().isClientSide) {
               ItemStack newStack = stack.copy();
               this.dropItemStack(newStack, 1.0F);
            }
         }

         this.setSize(1, false);
      }

      super.remove(reason);
   }

   @Nullable
   @Override
   protected ResourceKey<LootTable> getDefaultLootTable() {
      return PrimitiveMobsLootTables.EMPTY;
   }

   @Nullable
   protected ResourceKey<LootTable> getSpawnLootTable() {
      return PrimitiveMobsLootTables.TREASURESLIME_SPAWN;
   }

   @Override
   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
      ItemStack stack = this.getMainHandItem();
      if (!stack.isEmpty() && this.getSize() == 1 && !this.level().isClientSide) {
         ItemStack newStack = stack.copy();
         this.dropItemStack(newStack, 1.0F);
      }

   }

   @Override
   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float partialTick) {
      return new Vec3(0.0, (double)(dimensions.height() * 1.2F), 0.0);
   }

   public ItemEntity dropItemStack(ItemStack itemIn, float offsetY) {
      return this.spawnAtLocation(itemIn, offsetY);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (this.isTamed() && hand == InteractionHand.MAIN_HAND) {
         if (!stack.isEmpty()) {
            if (this.isHealingItem(stack)) {
               if (!player.getAbilities().instabuild) {
                  stack.shrink(1);
               }

               this.playHealEffect();
               this.heal(20.0F);
               return InteractionResult.SUCCESS;
            }

            if (this.isOwner(player)) {
               if (!this.getMainHandItem().isEmpty() && !this.level().isClientSide) {
                  this.dropItemStack(this.getMainHandItem(), 0.0F);
               }

               if (!stack.isEmpty()) {
                  ItemStack newStack = stack.copy();
                  newStack.setCount(1);
                  this.setItemSlot(EquipmentSlot.MAINHAND, newStack);
                  if (!player.getAbilities().instabuild) {
                     stack.shrink(1);
                  }
               }
            }
         } else if (stack.isEmpty() && this.isOwner(player)) {
            this.setSitting(!this.isSitting());
            this.playSitEffect();
         }
      } else if (!stack.isEmpty() && this.isTamingItem(stack) && hand == InteractionHand.MAIN_HAND && this.getMainHandItem().isEmpty()) {
         if (!player.getAbilities().instabuild) {
            stack.shrink(1);
         }

         if (!this.level().isClientSide) {
            this.setTamed(true);
            this.getNavigation().stop();
            this.setHealth(20.0F);
            this.setOwnerId(player.getUUID());
            this.level().broadcastEntityEvent(this, (byte)7);
         }

         this.playTameEffect(true);
         return InteractionResult.SUCCESS;
      }

      return super.mobInteract(player, hand);
   }

   @Override
   public void setTamed(boolean tamed) {
      super.setTamed(tamed);
      if (tamed) {
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)20.0F);
      } else {
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)10.0F);
      }

   }

   @Override
   public boolean hurt(DamageSource par1DamageSource, float par2) {
      if (par1DamageSource.is(DamageTypes.IN_WALL) && this.isTamed()) {
         return false;
      } else if (par1DamageSource.is(DamageTypes.FALL) && this.isTamed()) {
         return false;
      } else {
         return par1DamageSource.is(DamageTypes.DROWN) && this.isTamed() ? false : super.hurt(par1DamageSource, par2);
      }
   }

   public float[] getSkinRGB() {
      return new float[]{this.R, this.G, this.B};
   }

   public void setSkinRGB(int[] RGB) {
      this.R = (float)RGB[0];
      this.G = (float)RGB[1];
      this.B = (float)RGB[2];
   }

   public void setSkinRGB(float[] RGB) {
      this.R = RGB[0];
      this.G = RGB[1];
      this.B = RGB[2];
   }

   public float[] getNewSkinRGB() {
      return new float[]{this.NewR, this.NewG, this.NewB};
   }

   public void setNewSkinRGB(int[] RGB) {
      this.NewR = (float)RGB[0];
      this.NewG = (float)RGB[1];
      this.NewB = (float)RGB[2];
   }

   public void changeColor(Entity entity) {
      int[] newColor = new int[3];
      ItemStack heldItem = this.getMainHandItem();
      if (!heldItem.isEmpty() && heldItem != this.currentItem) {
         this.currentItem = heldItem;
         if (!(heldItem.getItem() instanceof BlockItem)) {
            newColor = ColorUtil.getItemStackColor(heldItem, this.level());
            if (newColor != null) {
               newColor = ColorUtil.setBrightness(newColor, 25.0F);
               this.setNewSkinRGB(newColor);
               return;
            }
         } else {
            BlockItem itemblock = (BlockItem)heldItem.getItem();
            Block block = itemblock.getBlock();
            BlockState blockstate = block.defaultBlockState();
            // Forge BlockModelShaper#getTexture requires a non-null BlockPos (ModelDataManager / ChunkPos); was null in production.
            newColor = ColorUtil.getBlockStateColor(blockstate, this.blockPosition(), this.level(), true);
            newColor = ColorUtil.setBrightness(newColor, 25.0F);
            if (newColor != null) {
               this.setNewSkinRGB(newColor);
               return;
            }
         }
      }

      if (heldItem.isEmpty()) {
         this.setNewSkinRGB(new int[]{255, 255, 255});
      }

   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
      return this.level().getDifficulty() != Difficulty.PEACEFUL && EntityUtil.isValidMobLightLevel(this);
   }
}
