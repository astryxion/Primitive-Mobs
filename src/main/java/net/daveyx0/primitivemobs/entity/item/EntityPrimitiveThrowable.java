package net.daveyx0.primitivemobs.entity.item;

import net.minecraft.network.syncher.SynchedEntityData;

import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity.EventHandler;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveCreeper;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EntityPrimitiveThrowable extends ThrowableProjectile {
   public Class<? extends Mob> spawnEntityClass;
   int spawnChance;
   LivingEntity throwerEntity = null;

   public EntityPrimitiveThrowable(EntityType<? extends EntityPrimitiveThrowable> type, Level worldIn) {
      super(type, worldIn);
      this.spawnEntityClass = Chicken.class;
      this.spawnChance = 0;
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
   }

   public EntityPrimitiveThrowable(Level worldIn, int chance) {
      super(EntityType.EGG, worldIn);
      this.spawnEntityClass = Chicken.class;
      this.spawnChance = chance;
   }

   public EntityPrimitiveThrowable(Level worldIn, Class<? extends Mob> entity, int chance) {
      super(EntityType.EGG, worldIn);
      this.spawnEntityClass = entity;
      this.spawnChance = chance;
   }

   public EntityPrimitiveThrowable(Level worldIn, LivingEntity throwerIn, Class<? extends Mob> entity, int chance) {
      super(EntityType.EGG, throwerIn, worldIn);
      this.spawnEntityClass = entity;
      this.spawnChance = chance;
      this.throwerEntity = throwerIn;
   }

   public EntityPrimitiveThrowable(Level worldIn, double x, double y, double z, Class<? extends Mob> entity, int chance) {
      super(EntityType.EGG, x, y, z, worldIn);
      this.spawnEntityClass = entity;
      this.spawnChance = chance;
   }

   @Override
   protected void onHit(HitResult result) {
      if (result instanceof EntityHitResult entityHitResult) {
         if (entityHitResult.getEntity() != null) {
            entityHitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
         }
      }

      if (!this.level().isClientSide) {
         if (this.random.nextInt(this.spawnChance) == 0) {
            int i = 1;

            for(int j = 0; j < i; ++j) {
               Mob entity = null;
               try {
                  EntityType<?> entityType = findEntityType(this.spawnEntityClass, this.level());
                  if (entityType != null) {
                     entity = (Mob)entityType.create(this.level());
                  }
               } catch (Exception e) {
                  // Entity creation failed
               }
               if (entity != null) {
                  if (entity instanceof Animal) {
                     Animal animal = (Animal)entity;
                     animal.setAge(-24000);
                     animal.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                     this.level().addFreshEntity(animal);
                  } else if (entity instanceof EntityPrimitiveCreeper) {
                     EntityPrimitiveCreeper creeper = (EntityPrimitiveCreeper)entity;
                     creeper.setGrowingAge(-24000);
                     creeper.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                     ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(entity, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
                     if (tameable != null && !tameable.isTamed() && this.throwerEntity != null) {
                        EventHandler.setUpTameable(tameable, entity, this.throwerEntity);
                     }

                     this.level().addFreshEntity(creeper);
                  } else {
                     entity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                     ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(entity, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
                     if (tameable != null && !tameable.isTamed() && this.throwerEntity != null) {
                        EventHandler.setUpTameable(tameable, entity, this.throwerEntity);
                     }

                     this.level().addFreshEntity(entity);
                  }
               }
            }
         }

         this.level().broadcastEntityEvent(this, (byte)3);
         this.discard();
      }

   }

   private static final java.util.Map<Class<? extends Mob>, EntityType<?>> ENTITY_TYPE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

   @SuppressWarnings("unchecked")
   private static EntityType<?> findEntityType(Class<? extends Mob> clazz, Level level) {
      EntityType<?> cached = ENTITY_TYPE_CACHE.get(clazz);
      if (cached != null) {
         return cached;
      }
      for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
         if (type != null) {
            try {
               Entity testEntity = type.create(level);
               if (testEntity != null && clazz.isInstance(testEntity)) {
                  testEntity.discard();
                  ENTITY_TYPE_CACHE.put(clazz, type);
                  return type;
               }
               if (testEntity != null) {
                  testEntity.discard();
               }
            } catch (Exception e) {
               // skip
            }
         }
      }
      return null;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void handleEntityEvent(byte id) {
      if (id == 3) {
         double d0 = 0.08;

         for(int i = 0; i < 8; ++i) {
            this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - (double)0.5F) * 0.08, ((double)this.random.nextFloat() - (double)0.5F) * 0.08, ((double)this.random.nextFloat() - (double)0.5F) * 0.08);
         }
      }

   }

   public ItemStack getItemFromEntity() {
      return new ItemStack(Items.EGG);
   }
}
