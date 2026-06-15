package net.daveyx0.primitivemobs.entity.ai;

import java.util.EnumSet;
import java.util.UUID;
import javax.annotation.Nullable;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveCreeper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class EntityAIEggOwnerFollow extends Goal {
   private final EntityPrimitiveCreeper creeper;
   private LivingEntity owner;
   private final double followSpeed;
   private final PathNavigation navigation;
   private final float minDist;
   private final float maxDist;
   private int timeToRecalcPath;
   private float oldWaterCost;

   public EntityAIEggOwnerFollow(EntityPrimitiveCreeper creeper, double followSpeed, float minDist, float maxDist) {
      this.creeper = creeper;
      this.followSpeed = followSpeed;
      this.navigation = creeper.getNavigation();
      this.minDist = minDist;
      this.maxDist = maxDist;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
   }

   @Nullable
   private LivingEntity resolveOwner() {
      ITameableEntity tameable = EntityUtil.getCapability(this.creeper, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, null);
      if (tameable != null && tameable.isTamed()) {
         if (tameable.getFollowState() == 0) {
            return null;
         }

         LivingEntity capabilityOwner = tameable.getOwner(this.creeper);
         if (capabilityOwner != null) {
            return capabilityOwner;
         }
      }

      UUID eggOwnerId = this.creeper.getEggOwnerId();
      if (eggOwnerId != null) {
         return EntityUtil.getLoadedEntityByUUID(eggOwnerId, this.creeper.level());
      }

      return null;
   }

   @Override
   public boolean canUse() {
      if (!this.creeper.isEggTamed()) {
         return false;
      }

      LivingEntity followTarget = this.resolveOwner();
      if (followTarget == null || !followTarget.isAlive()) {
         return false;
      }

      if (followTarget instanceof Player player && player.isSpectator()) {
         return false;
      }

      if (this.creeper.isPassenger()) {
         return false;
      }

      if (this.creeper.distanceToSqr(followTarget) < (double)(this.minDist * this.minDist)) {
         return false;
      }

      this.owner = followTarget;
      return true;
   }

   @Override
   public boolean canContinueToUse() {
      return this.owner != null && this.owner.isAlive() && !this.navigation.isDone() && this.creeper.distanceToSqr(this.owner) > (double)(this.maxDist * this.maxDist);
   }

   @Override
   public void start() {
      this.timeToRecalcPath = 0;
      this.oldWaterCost = this.creeper.getPathfindingMalus(BlockPathTypes.WATER);
      this.creeper.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
   }

   @Override
   public void stop() {
      this.owner = null;
      this.navigation.stop();
      this.creeper.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
   }

   @Override
   public void tick() {
      if (this.owner == null) {
         return;
      }

      this.creeper.getLookControl().setLookAt(this.owner, 10.0F, (float)this.creeper.getMaxHeadYRot());
      if (--this.timeToRecalcPath <= 0) {
         this.timeToRecalcPath = 10;
         if (!this.navigation.moveTo(this.owner, this.followSpeed) && !this.creeper.isLeashed() && !this.creeper.isPassenger() && this.creeper.getTarget() == null && this.creeper.distanceToSqr(this.owner) >= 144.0D) {
            int i = Mth.floor(this.owner.getX()) - 2;
            int j = Mth.floor(this.owner.getZ()) - 2;
            int k = Mth.floor(this.owner.getBoundingBox().minY);

            for (int l = 0; l <= 4; ++l) {
               for (int i1 = 0; i1 <= 4; ++i1) {
                  if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
                     this.creeper.moveTo((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.creeper.getYRot(), this.creeper.getXRot());
                     this.navigation.stop();
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean isTeleportFriendlyBlock(int x, int z, int y, int offsetX, int offsetZ) {
      BlockPos blockpos = new BlockPos(x + offsetX, y - 1, z + offsetZ);
      BlockState state = this.creeper.level().getBlockState(blockpos);
      return state.entityCanStandOnFace(this.creeper.level(), blockpos, this.creeper, Direction.UP) && this.creeper.level().isEmptyBlock(blockpos.above()) && this.creeper.level().isEmptyBlock(blockpos.above(2));
   }
}
