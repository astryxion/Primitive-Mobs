package net.daveyx0.primitivemobs.core;

import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nullable;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMParticle;
import net.daveyx0.multimob.network.MMNetworkWrapper;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.message.PrimitiveMobsMessageRegistry;
import net.daveyx0.primitivemobs.entity.monster.EntityGoblin;
import net.daveyx0.primitivemobs.entity.monster.EntityHarpy;
import net.daveyx0.primitivemobs.entity.monster.EntityHauntedTool;
import net.daveyx0.primitivemobs.entity.monster.EntityMimic;
import net.daveyx0.primitivemobs.entity.monster.EntityRocketCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntitySkeletonWarrior;
import net.daveyx0.primitivemobs.entity.passive.EntityLostMiner;
import net.daveyx0.primitivemobs.entity.passive.EntitySheepman;
import net.daveyx0.primitivemobs.entity.passive.EntityTravelingMerchant;
import net.daveyx0.primitivemobs.item.ItemCamouflageArmor;
import net.daveyx0.primitivemobs.message.MessagePrimitiveJumping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

public class PrimitiveMobsEvents {
   @Mod.EventBusSubscriber(modid = "primitivemobs")
   public static class EntityEventHandler {
      @SubscribeEvent
      public static void spawnEvent(EntityJoinLevelEvent event) {
         EntityUtil.removeWhenDisabled(event.getEntity());
         if (!PrimitiveMobsConfigSpecial.getMinerInVillage() && event.getEntity() instanceof Villager && !(event.getEntity() instanceof EntityLostMiner)) {
            Villager villager = (Villager)event.getEntity();
            if (villager != null && villager.getVillagerData().getProfession() == PrimitiveMobsVillagerProfessions.MINER_PROFESSION.get()) {
               replaceVillager(villager);
            }
         }

         if (event.getEntity() instanceof Villager && !(event.getEntity() instanceof EntityTravelingMerchant)) {
            Villager villager = (Villager)event.getEntity();
            if (villager != null && (villager.getVillagerData().getProfession() == PrimitiveMobsVillagerProfessions.MERCHANT_PROFESSION.get() || villager.getVillagerData().getProfession() == PrimitiveMobsVillagerProfessions.FAKE_MERCHANT_PROFESSION.get())) {
               replaceVillager(villager);
            }
         }

         if (event.getEntity() instanceof Villager && !(event.getEntity() instanceof EntitySheepman)) {
            Villager villager = (Villager)event.getEntity();
            if (villager != null && (villager.getVillagerData().getProfession() == PrimitiveMobsVillagerProfessions.SHEEPMAN_PROFESSION_ALCHEMIST.get() || villager.getVillagerData().getProfession() == PrimitiveMobsVillagerProfessions.SHEEPMAN_PROFESSION_SCAVENGER.get() || villager.getVillagerData().getProfession() == PrimitiveMobsVillagerProfessions.SHEEPMAN_PROFESSION_THIEF.get())) {
               replaceVillager(villager);
            }
         }

         if (event.getEntity() instanceof Villager) {
            Villager villager = (Villager)event.getEntity();
            if (!(event.getEntity() instanceof EntitySheepman)) {
               villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, EntityGoblin.class, 8.0F, 0.6, 0.6));
            } else {
               villager.goalSelector.getAvailableGoals().removeIf(wrappedGoal -> wrappedGoal.getGoal() instanceof AvoidEntityGoal);
               villager.goalSelector.addGoal(1, new AvoidEntityGoal<>(villager, ZombifiedPiglin.class, 12.0F, 0.8, 0.8));
            }
         }

         if (event.getEntity() instanceof ZombieVillager zombie) {
            if (PrimitiveMobsVillagerProfessions.PROFESSIONS.contains(zombie.getVillagerData().getProfession())) {
               zombie.setVillagerData(zombie.getVillagerData().setProfession(VillagerProfession.NONE));
            }
         }

      }

      /** Deny all {@code primitivemobs} mobs for {@link MobSpawnType#CHUNK_GENERATION} only; natural spawns unchanged. */
      @SubscribeEvent(priority = EventPriority.HIGH)
      public static void onMobSpawnPositionCheck(MobSpawnEvent.PositionCheck event) {
         if (event.getSpawnType() != MobSpawnType.CHUNK_GENERATION) {
            return;
         }
         ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
         if (id != null && PrimitiveMobsReference.MODID.equals(id.getNamespace())) {
            event.setResult(Event.Result.DENY);
         }
      }

      public static void replaceVillager(Villager villager) {
         int age = villager.getAge();
         Villager replacer = new Villager(EntityType.VILLAGER, villager.level());
         replacer.moveTo(villager.getX(), villager.getY(), villager.getZ(), villager.getYRot(), villager.getXRot());
         replacer.setAge(age);
         villager.level().addFreshEntity(replacer);
         villager.discard();
      }

      @SubscribeEvent
      public static void onPlayerLogOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
         if (event.getEntity().isPassenger()) {
            event.getEntity().stopRiding();
            if (event.getEntity() instanceof ServerPlayer) {
               MMNetworkWrapper.sendPacket(event.getEntity(), new ClientboundSetPassengersPacket(event.getEntity()));
            }
         }

      }

      @SubscribeEvent
      public static void onBlockLeftClickEvent(PlayerInteractEvent.LeftClickBlock event) {
         BlockEntity tileEntity = event.getLevel().getBlockEntity(event.getPos());
         if (tileEntity != null && tileEntity instanceof ChestBlockEntity && !event.getLevel().isClientSide) {
            ChestBlockEntity chest = (ChestBlockEntity)tileEntity;
            CompoundTag compound = chest.getPersistentData();
            boolean isEmpty = true;

            for(int i = 0; i < chest.getContainerSize(); ++i) {
               if (!chest.getItem(i).isEmpty()) {
                  isEmpty = false;
               }
            }

            boolean isSingleChest = chest.getBlockState().getValue(ChestBlock.TYPE) == ChestType.SINGLE;
            if (event.getEntity().isCrouching() && isEmpty && event.getEntity().getMainHandItem() != null && event.getEntity().getMainHandItem().getItem() == PrimitiveMobsItems.MIMIC_ORB.get() && isSingleChest) {
               consumeItemFromStack(event.getEntity(), event.getEntity().getMainHandItem());
               compound.putInt("Mimic", 1);
               MMMessageRegistry.getNetwork().send(PacketDistributor.ALL.noArg(), new MessageMMParticle(9, 10, (float)event.getPos().getX() + 0.05F, (float)event.getPos().getY() + 0.05F, (float)event.getPos().getZ() + 0.05F, (double)0.0F, 0.01, (double)0.0F, 0));
               event.setCanceled(true);
            }
         }

      }

      @SubscribeEvent
      public static void onBlockRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
         BlockEntity tileEntity = event.getLevel().getBlockEntity(event.getPos());
         if (tileEntity != null && tileEntity instanceof ChestBlockEntity && !event.getLevel().isClientSide) {
            ChestBlockEntity chest = (ChestBlockEntity)tileEntity;
            CompoundTag compound = chest.getPersistentData();
            boolean isSingleChest = chest.getBlockState().getValue(ChestBlock.TYPE) == ChestType.SINGLE;
            if (compound.contains("Mimic")) {
               if (compound.getInt("Mimic") != 0) {
                  int chance = event.getLevel().getRandom().nextInt(3);
                  boolean flag = chance == 0 || compound.getInt("Mimic") == 2;
                  boolean flag1 = chance == 1;
                  if (flag && isSingleChest) {
                     EntityMimic mimic = new EntityMimic(PrimitiveMobsEntityRegistry.MIMIC.get(), event.getLevel());
                     mimic.moveTo((double)event.getPos().getX() + (double)0.5F, (double)event.getPos().getY(), (double)event.getPos().getZ() + (double)0.5F, 180.0F, 0.0F);
                     mimic.setChest(event.getLevel().getBlockState(event.getPos()));
                     event.getLevel().addFreshEntity(mimic);
                     if (compound.getInt("Mimic") != 2 && event.getLevel().getRandom().nextInt(10) == 0) {
                        mimic.setToExplode();
                     }

                     event.getLevel().removeBlock(event.getPos(), false);
                     event.setCanceled(true);
                     MMMessageRegistry.getNetwork().send(PacketDistributor.ALL.noArg(), new MessageMMParticle(26, 10, (float)event.getPos().getX() + 0.05F, (float)event.getPos().getY() + 0.05F, (float)event.getPos().getZ() + 0.05F, (double)0.0F, (double)0.0F, (double)0.0F, 0));
                  } else if (flag1 && isSingleChest) {
                     int option = event.getLevel().getRandom().nextInt(4);
                     if (option == 0) {
                        EntitySkeletonWarrior skeleton = new EntitySkeletonWarrior(PrimitiveMobsEntityRegistry.SKELETON_WARRIOR.get(), event.getLevel());
                        skeleton.finalizeSpawn((ServerLevelAccessor)event.getLevel(), event.getLevel().getCurrentDifficultyAt(skeleton.blockPosition()), MobSpawnType.EVENT, (SpawnGroupData)null, (CompoundTag)null);
                        skeleton.moveTo((double)event.getPos().getX() + (double)0.5F, (double)event.getPos().getY() + (double)1.0F, (double)event.getPos().getZ() + (double)0.5F, 180.0F, 0.0F);
                        event.getLevel().addFreshEntity(skeleton);
                     } else if (option == 1) {
                        for(int i = 0; i < 3; ++i) {
                           Bat bat = new Bat(EntityType.BAT, event.getLevel());
                           bat.moveTo((double)event.getPos().getX() + (double)0.5F, (double)event.getPos().getY() + (double)1.0F, (double)event.getPos().getZ() + (double)0.5F, 180.0F, 0.0F);
                           event.getLevel().addFreshEntity(bat);
                        }
                     } else {
                        EntityHauntedTool tool = new EntityHauntedTool(PrimitiveMobsEntityRegistry.HAUNTED_TOOL.get(), event.getLevel());
                        tool.finalizeSpawn((ServerLevelAccessor)event.getLevel(), event.getLevel().getCurrentDifficultyAt(tool.blockPosition()), MobSpawnType.EVENT, (SpawnGroupData)null, (CompoundTag)null);
                        tool.moveTo((double)event.getPos().getX() + (double)0.5F, (double)event.getPos().getY() + (double)1.0F, (double)event.getPos().getZ() + (double)0.5F, 180.0F, 0.0F);
                        event.getLevel().addFreshEntity(tool);
                     }

                     chest.setLootTable(PrimitiveMobsLootTables.MIMIC_TRAP, event.getLevel().getRandom().nextLong());
                     compound.putInt("Mimic", 0);
                     event.setCanceled(true);
                     MMMessageRegistry.getNetwork().send(PacketDistributor.ALL.noArg(), new MessageMMParticle(26, 10, (float)event.getPos().getX() + 0.5F, (float)event.getPos().getY() + 0.5F, (float)event.getPos().getZ() + 0.5F, (double)0.0F, (double)0.0F, (double)0.0F, 0));
                  } else if (isSingleChest) {
                     chest.setLootTable(PrimitiveMobsLootTables.MIMIC_TREASURE, event.getLevel().getRandom().nextLong());
                     compound.putInt("Mimic", 0);
                  }
               }
            } else {
               boolean isEmpty = true;

               for(int i = 0; i < chest.getContainerSize(); ++i) {
                  if (!chest.getItem(i).isEmpty()) {
                     isEmpty = false;
                  }
               }

               if (isEmpty && event.getEntity().getMainHandItem() != null && event.getEntity().getMainHandItem().getItem() == PrimitiveMobsItems.MIMIC_ORB.get() && isSingleChest) {
                  consumeItemFromStack(event.getEntity(), event.getEntity().getMainHandItem());
                  compound.putInt("Mimic", 2);
                  MMMessageRegistry.getNetwork().send(PacketDistributor.ALL.noArg(), new MessageMMParticle(26, 10, (float)event.getPos().getX() + 0.5F, (float)event.getPos().getY() + 0.5F, (float)event.getPos().getZ() + 0.5F, (double)0.0F, (double)0.0F, (double)0.0F, 0));
                  event.setCanceled(true);
               }
            }
         }

      }

      @SubscribeEvent
      public void onLivingUpdate(LivingEvent.LivingTickEvent event) {
         LivingEntity entityLiving = event.getEntity();
         if (entityLiving != null && entityLiving.tickCount % 5 == 0) {
            ItemCamouflageArmor.setCamouflageArmorNBT(entityLiving, EquipmentSlot.CHEST);
            ItemCamouflageArmor.setCamouflageArmorNBT(entityLiving, EquipmentSlot.FEET);
            ItemCamouflageArmor.setCamouflageArmorNBT(entityLiving, EquipmentSlot.HEAD);
            ItemCamouflageArmor.setCamouflageArmorNBT(entityLiving, EquipmentSlot.LEGS);
            if (entityLiving.level().getRandom().nextInt(50) == 0 && entityLiving instanceof Player) {
               Player player = (Player)entityLiving;
               if (hasFullCamouflageArmor(player)) {
                  entityLiving.level().addParticle(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX() + (double)(event.getEntity().level().getRandom().nextFloat() - entityLiving.level().getRandom().nextFloat()), entityLiving.getY() + (double)entityLiving.level().getRandom().nextFloat() + (double)1.0F, entityLiving.getZ() + (double)(entityLiving.level().getRandom().nextFloat() - entityLiving.level().getRandom().nextFloat()), (double)1.0F, (double)1.0F, (double)1.0F);
               }
            }
         }

      }

      @SubscribeEvent
      public void onSetAttackTarget(LivingChangeTargetEvent event) {
         LivingEntity entityLiving = event.getEntity();
         if (entityLiving != null && (entityLiving instanceof Zombie || entityLiving instanceof AbstractIllager || entityLiving instanceof EntityGoblin)) {
            if (event.getNewTarget() instanceof EntitySheepman) {
               event.setNewTarget(null);
            }
         }

         if (event.getNewTarget() != null && event.getNewTarget() instanceof Player && event.getEntity() instanceof Mob) {
            Player player = (Player)event.getNewTarget();
            Mob living = (Mob)event.getEntity();
            if (living.getLastHurtByMob() != player && living.getLastHurtMob() != player && hasFullCamouflageArmor(player) && living.distanceToSqr(player) > (double)36.0F) {
               event.setNewTarget(null);
            } else {
               living.setLastHurtByMob(player);
            }
         }

      }

      public static boolean hasFullCamouflageArmor(Player player) {
         int amountOfPieces = 0;

         for(ItemStack stack : player.getArmorSlots()) {
            if (stack.getItem() == PrimitiveMobsItems.CAMOUFLAGE_BOOTS.get() || stack.getItem() == PrimitiveMobsItems.CAMOUFLAGE_CHEST.get() || stack.getItem() == PrimitiveMobsItems.CAMOUFLAGE_HELMET.get() || stack.getItem() == PrimitiveMobsItems.CAMOUFLAGE_LEGS.get()) {
               ++amountOfPieces;
            }
         }

         if (amountOfPieces == 4) {
            return true;
         } else {
            return false;
         }
      }

      protected static void consumeItemFromStack(Player player, ItemStack stack) {
         if (!player.getAbilities().instabuild) {
            stack.shrink(1);
         }

      }

      @SubscribeEvent
      public void onLivingFall(LivingFallEvent event) {
         if (event.getEntity() != null && event.getEntity() instanceof EntityRocketCreeper) {
            event.setCanceled(true);
         }

      }

      @SubscribeEvent
      public void onEntityAttacked(LivingAttackEvent event) {
         if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity sourceEntity = (LivingEntity)event.getSource().getEntity();
            if (sourceEntity.getMainHandItem().getItem() == PrimitiveMobsItems.GOBLIN_MACE.get()) {
               ArrayList<ItemStack> damageableArmorPieces = new ArrayList<>();
               Iterable<ItemStack> armorPieces = event.getEntity().getArmorSlots();
               if (armorPieces != null && !armorPieces.equals(Collections.emptyList())) {
                  for(ItemStack piece : event.getEntity().getArmorSlots()) {
                     if (!piece.isEmpty() && piece.isDamageableItem()) {
                        damageableArmorPieces.add(piece);
                     }
                  }

                  if (!damageableArmorPieces.isEmpty()) {
                     ItemStack targetPiece = (ItemStack)damageableArmorPieces.get(event.getEntity().getRandom().nextInt(damageableArmorPieces.size()));
                     targetPiece.hurtAndBreak((int)((float)targetPiece.getMaxDamage() * 0.1F), event.getEntity(), (entity) -> {});
                  }
               }
            }
         }

      }

      @Nullable
      public EquipmentSlot getSlotFromItemStack(LivingEntity entityIn, ItemStack stack) {
         if (entityIn != null && !stack.isEmpty()) {
            if (stack.getItem() == entityIn.getItemBySlot(EquipmentSlot.HEAD).getItem()) {
               return EquipmentSlot.HEAD;
            }

            if (stack.getItem() == entityIn.getItemBySlot(EquipmentSlot.CHEST).getItem()) {
               return EquipmentSlot.CHEST;
            }

            if (stack.getItem() == entityIn.getItemBySlot(EquipmentSlot.LEGS).getItem()) {
               return EquipmentSlot.LEGS;
            }

            if (stack.getItem() == entityIn.getItemBySlot(EquipmentSlot.FEET).getItem()) {
               return EquipmentSlot.FEET;
            }
         }

         return null;
      }

      @SubscribeEvent
      public static void DismountPlayerEvent(EntityMountEvent event) {
         if (event.isDismounting() && event.getEntityBeingMounted() != null && event.getEntityBeingMounted() instanceof EntityHarpy && event.getEntityMounting() != null && event.getEntityMounting().isShiftKeyDown() && event.getEntityMounting() instanceof Player) {
            event.setCanceled(true);
         }

      }

      @SubscribeEvent
      public static void PlayEntitySound(PlayLevelSoundEvent.AtEntity event) {
         if (event.getEntity() instanceof EntitySheepman) {
            if (event.getSound() != null && event.getSound().value() == SoundEvents.VILLAGER_AMBIENT) {
               event.setSound(Holder.direct(SoundEvents.SHEEP_AMBIENT));
            } else if (event.getSound() != null && event.getSound().value() == SoundEvents.VILLAGER_HURT) {
               event.setSound(Holder.direct(SoundEvents.SHEEP_HURT));
            }
         }

      }

      @SubscribeEvent
      @OnlyIn(Dist.CLIENT)
      public void onKeyInput(InputEvent.Key event) {
         if (Minecraft.getInstance().options.keyJump.isDown()) {
            String UUID = Minecraft.getInstance().player.getUUID().toString();
            PrimitiveMobsMessageRegistry.getPrimitiveNetwork().sendToServer(new MessagePrimitiveJumping(UUID));
         }

      }
   }
}
