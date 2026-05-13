package net.daveyx0.primitivemobs.entity;

import net.minecraft.world.entity.LivingEntity;

public interface IAnimatedMob {
   void performAction(LivingEntity var1, int var2);

   void setAnimationState(int var1);

   int getAnimationState();

   void setPreviousAnimationState(int var1);

   int getPreviousAnimationState();

   void setAnimVar(float var1);

   float getAnimVar();
}
