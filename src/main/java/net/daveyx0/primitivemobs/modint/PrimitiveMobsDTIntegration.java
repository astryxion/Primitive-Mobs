package net.daveyx0.primitivemobs.modint;

import net.daveyx0.multimob.modint.DynamicTreesIntegration;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;

public class PrimitiveMobsDTIntegration extends DynamicTreesIntegration {
   @Override
   public void init() {
      super.init();
   }

   public Object[] searchDynamicTree(Mob entity, double d) {
      Object[] states = new Object[3];
      AABB axisalignedbb = new AABB(entity.getX() - d, entity.getY() - d, entity.getZ() - d, entity.getX() + d, entity.getY() + d, entity.getZ() + d);
      int n = Mth.floor(axisalignedbb.minX);
      int o = Mth.floor(axisalignedbb.maxX);
      int p = Mth.floor(axisalignedbb.minY);
      int q = Mth.floor(axisalignedbb.maxY);
      int r = Mth.floor(axisalignedbb.minZ);
      int s = Mth.floor(axisalignedbb.maxZ);

      for(int p1 = n; p1 < o; ++p1) {
         for(int q1 = p; q1 < q; ++q1) {
            for(int n2 = r; n2 < s; ++n2) {
               BlockPos pos = new BlockPos(p1, q1, n2);
               BlockState state = entity.level().getBlockState(pos);
               if (state != null && !state.isAir() && state.is(net.minecraft.tags.BlockTags.LOGS)) {
                  states[0] = state;

                  for(int l = 0; l < 64; ++l) {
                     BlockPos pos2 = new BlockPos(pos.getX(), pos.getY() + l, pos.getZ());
                     BlockState state2 = entity.level().getBlockState(pos2);
                     if (state2 != null && !state2.isAir() && state2.is(net.minecraft.tags.BlockTags.LEAVES)) {
                        states[1] = state2;
                        states[2] = pos2;
                        return states;
                     }
                  }
               }
            }
         }
      }

      return null;
   }
}
