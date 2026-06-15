package net.daveyx0.primitivemobs.entity.item;

import javax.annotation.Nullable;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity.EventHandler;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.daveyx0.primitivemobs.entity.monster.EntityFestiveCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityRocketCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntitySupportCreeper;
import net.daveyx0.primitivemobs.entity.passive.EntityDodo;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
   protected void defineSynchedData() {
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

   @Nullable
   private LivingEntity getThrower() {
      if (this.throwerEntity != null) {
         return this.throwerEntity;
      }

      Entity owner = this.getOwner();
      return owner instanceof LivingEntity living ? living : null;
   }

   private void prepareTamedSpawn(Mob entity) {
      LivingEntity thrower = this.getThrower();
      if (thrower == null) {
         return;
      }

      if (entity instanceof EntityPrimitiveCreeper creeper) {
         creeper.setupEggTaming(thrower);
         return;
      }

      ITameableEntity tameable = EntityUtil.getCapability(entity, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, null);
      if (tameable != null && !tameable.isTamed()) {
         EventHandler.setUpTameable(tameable, entity, thrower);
      }
   }

   private void spawnMob(Mob entity) {
      entity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
      this.prepareTamedSpawn(entity);
      if (this.level().addFreshEntity(entity)) {
         this.prepareTamedSpawn(entity);
      }
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
                  EntityType<?> entityType = resolveSpawnType(this.spawnEntityClass, this.level());
                  if (entityType != null) {
                     entity = (Mob)entityType.create(this.level());
                  }
               } catch (Exception e) {
                  // Entity creation failed
               }
               if (entity != null) {
                  if (entity instanceof Animal animal) {
                     animal.setAge(-24000);
                     this.spawnMob(animal);
                  } else if (entity instanceof EntityPrimitiveCreeper creeper) {
                     creeper.setGrowingAge(-24000);
                     this.spawnMob(creeper);
                  } else {
                     this.spawnMob(entity);
                  }
               }
            }
         }

         this.level().broadcastEntityEvent(this, (byte)3);
         this.discard();
      }

   }

   private static final java.util.Map<Class<? extends Mob>, EntityType<?>> ENTITY_TYPE_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

   private static EntityType<?> resolveSpawnType(Class<? extends Mob> clazz, Level level) {
      if (EntityFestiveCreeper.class.equals(clazz)) {
         return PrimitiveMobsEntityRegistry.FESTIVE_CREEPER.get();
      }
      if (EntitySupportCreeper.class.equals(clazz)) {
         return PrimitiveMobsEntityRegistry.SUPPORT_CREEPER.get();
      }
      if (EntityRocketCreeper.class.equals(clazz)) {
         return PrimitiveMobsEntityRegistry.ROCKET_CREEPER.get();
      }
      if (EntityBabySpider.class.equals(clazz)) {
         return PrimitiveMobsEntityRegistry.BABY_SPIDER.get();
      }
      if (EntityDodo.class.equals(clazz)) {
         return PrimitiveMobsEntityRegistry.DODO.get();
      }

      return findEntityType(clazz, level);
   }

   @SuppressWarnings("unchecked")
   private static EntityType<?> findEntityType(Class<? extends Mob> clazz, Level level) {
      EntityType<?> cached = ENTITY_TYPE_CACHE.get(clazz);
      if (cached != null) {
         return cached;
      }
      for (EntityType<?> type : net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES) {
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
