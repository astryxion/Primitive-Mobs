package net.daveyx0.primitivemobs.core;

import net.daveyx0.multimob.core.MMEntityRegistry;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigMobs;
import net.daveyx0.primitivemobs.entity.item.EntityFlameSpit;
import net.daveyx0.primitivemobs.entity.item.EntityPrimitiveTNTPrimed;
import net.daveyx0.primitivemobs.entity.item.EntityPrimitiveThrowable;
import net.daveyx0.primitivemobs.entity.item.EntitySpiderEgg;
import net.daveyx0.primitivemobs.entity.item.EntityThrownBlock;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.daveyx0.primitivemobs.entity.monster.EntityBlazingJuggernaut;
import net.daveyx0.primitivemobs.entity.monster.EntityBrainSlime;
import net.daveyx0.primitivemobs.entity.monster.EntityEnchantedBook;
import net.daveyx0.primitivemobs.entity.monster.EntityFestiveCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityFlameSpewer;
import net.daveyx0.primitivemobs.entity.monster.EntityGoblin;
import net.daveyx0.primitivemobs.entity.monster.EntityHarpy;
import net.daveyx0.primitivemobs.entity.monster.EntityHauntedTool;
import net.daveyx0.primitivemobs.entity.monster.EntityLilyLurker;
import net.daveyx0.primitivemobs.entity.monster.EntityMimic;
import net.daveyx0.primitivemobs.entity.monster.EntityMotherSpider;
import net.daveyx0.primitivemobs.entity.monster.EntityRocketCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntitySkeletonWarrior;
import net.daveyx0.primitivemobs.entity.monster.EntitySupportCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityTreasureSlime;
import net.daveyx0.primitivemobs.entity.monster.EntityTrollager;
import net.daveyx0.primitivemobs.entity.monster.EntityVoidEye;
import net.daveyx0.primitivemobs.entity.passive.EntityChameleon;
import net.daveyx0.primitivemobs.entity.passive.EntityDodo;
import net.daveyx0.primitivemobs.entity.passive.EntityFilchLizard;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.daveyx0.primitivemobs.entity.passive.EntityLostMiner;
import net.daveyx0.primitivemobs.entity.passive.EntitySheepman;
import net.daveyx0.primitivemobs.entity.passive.EntityTravelingMerchant;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.daveyx0.primitivemobs.client.models.*;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.daveyx0.primitivemobs.client.renderer.entity.*;

public class PrimitiveMobsEntityRegistry extends MMEntityRegistry {
   public static int id;

   public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "primitivemobs");

   public static final RegistryObject<EntityType<EntityChameleon>> CHAMELEON = ENTITY_TYPES.register("chameleon",
      () -> EntityType.Builder.of(EntityChameleon::new, MobCategory.CREATURE).sized(0.6F, 0.5F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:chameleon"));

   public static final RegistryObject<EntityType<EntityTreasureSlime>> TREASURE_SLIME = ENTITY_TYPES.register("treasure_slime",
      () -> EntityType.Builder.of(EntityTreasureSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:treasure_slime"));

   public static final RegistryObject<EntityType<EntityHauntedTool>> HAUNTED_TOOL = ENTITY_TYPES.register("haunted_tool",
      () -> EntityType.Builder.of(EntityHauntedTool::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:haunted_tool"));

   public static final RegistryObject<EntityType<EntityGroveSprite>> GROVESPRITE = ENTITY_TYPES.register("grovesprite",
      () -> EntityType.Builder.of(EntityGroveSprite::new, MobCategory.CREATURE).sized(0.6F, 1.2F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:grovesprite"));

   public static final RegistryObject<EntityType<EntityEnchantedBook>> BEWITCHED_TOME = ENTITY_TYPES.register("bewitched_tome",
      () -> EntityType.Builder.of(EntityEnchantedBook::new, MobCategory.MONSTER).sized(0.6F, 0.8F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:bewitched_tome"));

   public static final RegistryObject<EntityType<EntityFilchLizard>> FILCH_LIZARD = ENTITY_TYPES.register("filch_lizard",
      () -> EntityType.Builder.of(EntityFilchLizard::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:filch_lizard"));

   public static final RegistryObject<EntityType<EntityBrainSlime>> BRAIN_SLIME = ENTITY_TYPES.register("brain_slime",
      () -> EntityType.Builder.of(EntityBrainSlime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:brain_slime"));

   public static final RegistryObject<EntityType<EntityRocketCreeper>> ROCKET_CREEPER = ENTITY_TYPES.register("rocket_creeper",
      () -> EntityType.Builder.of(EntityRocketCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:rocket_creeper"));

   public static final RegistryObject<EntityType<EntityFestiveCreeper>> FESTIVE_CREEPER = ENTITY_TYPES.register("festive_creeper",
      () -> EntityType.Builder.of(EntityFestiveCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:festive_creeper"));

   public static final RegistryObject<EntityType<EntitySupportCreeper>> SUPPORT_CREEPER = ENTITY_TYPES.register("support_creeper",
      () -> EntityType.Builder.of(EntitySupportCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:support_creeper"));

   public static final RegistryObject<EntityType<EntitySkeletonWarrior>> SKELETON_WARRIOR = ENTITY_TYPES.register("skeleton_warrior",
      () -> EntityType.Builder.of(EntitySkeletonWarrior::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:skeleton_warrior"));

   public static final RegistryObject<EntityType<EntityBlazingJuggernaut>> BLAZING_JUGGERNAUT = ENTITY_TYPES.register("blazing_juggernaut",
      () -> EntityType.Builder.of(EntityBlazingJuggernaut::new, MobCategory.MONSTER).sized(0.9F, 2.4F).fireImmune().clientTrackingRange(80).updateInterval(3).build("primitivemobs:blazing_juggernaut"));

   public static final RegistryObject<EntityType<EntityLilyLurker>> LILY_LURKER = ENTITY_TYPES.register("lily_lurker",
      () -> EntityType.Builder.of(EntityLilyLurker::new, MobCategory.MONSTER).sized(1.4F, 1.0F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:lily_lurker"));

   public static final RegistryObject<EntityType<EntityMotherSpider>> MOTHER_SPIDER = ENTITY_TYPES.register("mother_spider",
      () -> EntityType.Builder.of(EntityMotherSpider::new, MobCategory.MONSTER).sized(1.4F, 0.9F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:mother_spider"));

   public static final RegistryObject<EntityType<EntityBabySpider>> BABY_SPIDER = ENTITY_TYPES.register("baby_spider",
      () -> EntityType.Builder.of(EntityBabySpider::new, MobCategory.MONSTER).sized(0.7F, 0.5F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:baby_spider"));

   public static final RegistryObject<EntityType<EntityTrollager>> TROLLAGER = ENTITY_TYPES.register("trollager",
      () -> EntityType.Builder.of(EntityTrollager::new, MobCategory.MONSTER).sized(1.2F, 2.7F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:trollager"));

   public static final RegistryObject<EntityType<EntityLostMiner>> LOST_MINER = ENTITY_TYPES.register("lost_miner",
      () -> EntityType.Builder.of(EntityLostMiner::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:lost_miner"));

   public static final RegistryObject<EntityType<EntityTravelingMerchant>> TRAVELING_MERCHANT = ENTITY_TYPES.register("traveling_merchant",
      () -> EntityType.Builder.of(EntityTravelingMerchant::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:traveling_merchant"));

   public static final RegistryObject<EntityType<EntityDodo>> DODO = ENTITY_TYPES.register("dodo",
      () -> EntityType.Builder.of(EntityDodo::new, MobCategory.CREATURE).sized(0.7F, 0.9F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:dodo"));

   public static final RegistryObject<EntityType<EntityMimic>> MIMIC = ENTITY_TYPES.register("mimic",
      () -> EntityType.Builder.of(EntityMimic::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:mimic"));

   public static final RegistryObject<EntityType<EntitySheepman>> SHEEPMAN = ENTITY_TYPES.register("sheepman",
      () -> EntityType.Builder.of(EntitySheepman::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:sheepman"));

   public static final RegistryObject<EntityType<EntityGoblin>> GOBLIN = ENTITY_TYPES.register("goblin",
      () -> EntityType.Builder.of(EntityGoblin::new, MobCategory.MONSTER).sized(0.6F, 1.4F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:goblin"));

   public static final RegistryObject<EntityType<EntityHarpy>> HARPY = ENTITY_TYPES.register("harpy",
      () -> EntityType.Builder.of(EntityHarpy::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:harpy"));

   public static final RegistryObject<EntityType<EntityFlameSpewer>> FLAME_SPEWER = ENTITY_TYPES.register("flame_spewer",
      () -> EntityType.Builder.of(EntityFlameSpewer::new, MobCategory.MONSTER).sized(1.0F, 1.8F).fireImmune().clientTrackingRange(80).updateInterval(3).build("primitivemobs:flame_spewer"));

   public static final RegistryObject<EntityType<EntityVoidEye>> VOID_EYE = ENTITY_TYPES.register("void_eye",
      () -> EntityType.Builder.of(EntityVoidEye::new, MobCategory.MONSTER).sized(0.8F, 0.8F).clientTrackingRange(80).updateInterval(3).build("primitivemobs:void_eye"));

   public static final RegistryObject<EntityType<EntityPrimitiveTNTPrimed>> PRIMITIVE_TNT_PRIMED = ENTITY_TYPES.register("primitive_tnt_primed",
      () -> EntityType.Builder.<EntityPrimitiveTNTPrimed>of(EntityPrimitiveTNTPrimed::new, MobCategory.MISC).sized(0.98F, 0.98F).fireImmune().clientTrackingRange(64).updateInterval(20).build("primitivemobs:primitive_tnt_primed"));

   public static final RegistryObject<EntityType<EntityFlameSpit>> FLAME_SPIT = ENTITY_TYPES.register("flame_spit",
      () -> EntityType.Builder.<EntityFlameSpit>of(EntityFlameSpit::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(64).updateInterval(1).build("primitivemobs:flame_spit"));

   public static final RegistryObject<EntityType<EntityThrownBlock>> THROWN_BLOCK = ENTITY_TYPES.register("thrown_block",
      () -> EntityType.Builder.<EntityThrownBlock>of(EntityThrownBlock::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(64).updateInterval(1).build("primitivemobs:thrown_block"));

   public static final RegistryObject<EntityType<EntityPrimitiveThrowable>> PRIMITIVE_EGG = ENTITY_TYPES.register("primitive_egg",
      () -> EntityType.Builder.<EntityPrimitiveThrowable>of(EntityPrimitiveThrowable::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(64).updateInterval(20).build("primitivemobs:primitive_egg"));

   public static final RegistryObject<EntityType<EntitySpiderEgg>> SPIDER_EGG = ENTITY_TYPES.register("spider_egg",
      () -> EntityType.Builder.<EntitySpiderEgg>of(EntitySpiderEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(64).updateInterval(20).build("primitivemobs:spider_egg"));

   public static void registerEntities() {
      id = 0;
      addEntities("primitivemobs", EntityChameleon.class, "chameleon", ++id, 297984, 153088, PrimitiveMobsConfigMobs.enableChameleon);
      addEntities("primitivemobs", EntityTreasureSlime.class, "treasure_slime", ++id, 16774041, 16769842, PrimitiveMobsConfigMobs.enableTreasureSlime);
      addEntities("primitivemobs", EntityHauntedTool.class, "haunted_tool", ++id, 4797973, 6835742, PrimitiveMobsConfigMobs.enableHauntedTool);
      addEntities("primitivemobs", EntityGroveSprite.class, "grovesprite", ++id, 5983805, 6465327, PrimitiveMobsConfigMobs.enableGroveSprite);
      addEntities("primitivemobs", EntityEnchantedBook.class, "bewitched_tome", ++id, 12024373, 13684944, PrimitiveMobsConfigMobs.enableEnchantedBook);
      addEntities("primitivemobs", EntityFilchLizard.class, "filch_lizard", ++id, 12760724, 13749696, PrimitiveMobsConfigMobs.enableFilchLizard);
      addEntities("primitivemobs", EntityBrainSlime.class, "brain_slime", ++id, 13014704, 13739453, PrimitiveMobsConfigMobs.enableBrainSlime);
      addEntities("primitivemobs", EntityRocketCreeper.class, "rocket_creeper", ++id, 5024208, 0, PrimitiveMobsConfigMobs.enableRocketCreeper);
      addEntities("primitivemobs", EntityFestiveCreeper.class, "festive_creeper", ++id, 12334600, 0, PrimitiveMobsConfigMobs.enableFestiveCreeper);
      addEntities("primitivemobs", EntitySupportCreeper.class, "support_creeper", ++id, 14400815, 0, PrimitiveMobsConfigMobs.enableSupportCreeper);
      addEntities("primitivemobs", EntitySkeletonWarrior.class, "skeleton_warrior", ++id, 11248008, 7098937, PrimitiveMobsConfigMobs.enableSkeletonWarrior);
      addEntities("primitivemobs", EntityBlazingJuggernaut.class, "blazing_juggernaut", ++id, 3151900, 11577656, PrimitiveMobsConfigMobs.enableBlazingJuggernaut);
      addEntities("primitivemobs", EntityLilyLurker.class, "lily_lurker", ++id, 5848361, 4013084, PrimitiveMobsConfigMobs.enableLilyLurker);
      addEntities("primitivemobs", EntityMotherSpider.class, "mother_spider", ++id, 2426146, 11013646, PrimitiveMobsConfigMobs.enableSpiderFamily);
      addEntities("primitivemobs", EntityBabySpider.class, "baby_spider", ++id, 11900008, 11013646, PrimitiveMobsConfigMobs.enableSpiderFamily);
      addEntities("primitivemobs", EntityTrollager.class, "trollager", ++id, 5669981, 3482911, PrimitiveMobsConfigMobs.enableTrollager);
      addEntities("primitivemobs", EntityLostMiner.class, "lost_miner", ++id, 7091750, 12422002, PrimitiveMobsConfigMobs.enableLostMiner);
      addEntities("primitivemobs", EntityTravelingMerchant.class, "traveling_merchant", ++id, 6316113, 12422002, PrimitiveMobsConfigMobs.enableMerchant);
      addEntities("primitivemobs", EntityDodo.class, "dodo", ++id, 7493187, 12362124, PrimitiveMobsConfigMobs.enableDodo);
      addEntities("primitivemobs", EntityMimic.class, "mimic", ++id, 11237677, 2762013, PrimitiveMobsConfigMobs.enableMimic);
      addEntities("primitivemobs", EntitySheepman.class, "sheepman", ++id, 2500134, 11769472, PrimitiveMobsConfigMobs.enableSheepman);
      addEntities("primitivemobs", EntityGoblin.class, "goblin", ++id, 9148269, 5390647, PrimitiveMobsConfigMobs.enableGoblin);
      addEntities("primitivemobs", EntityHarpy.class, "harpy", ++id, 4011092, 10785643, PrimitiveMobsConfigMobs.enableHarpy);
      addEntities("primitivemobs", EntityFlameSpewer.class, "flame_spewer", ++id, 8799790, 9268838, PrimitiveMobsConfigMobs.enableFlameSpewer);
      addEntities("primitivemobs", EntityVoidEye.class, "void_eye", ++id, 789778, 9358713, PrimitiveMobsConfigMobs.enableVoidWatcher);
      addCustomEntities("primitivemobs", EntityPrimitiveTNTPrimed.class, "primitive_tnt_primed", ++id, 64, 20, true);
      addCustomEntities("primitivemobs", EntityFlameSpit.class, "flame_spit", ++id, 64, 1, false);
      addCustomEntities("primitivemobs", EntityThrownBlock.class, "thrown_block", ++id, 64, 1, true);
      addCustomEntities("primitivemobs", EntityPrimitiveThrowable.class, "primitive_egg", ++id, 64, 20, true);
      addCustomEntities("primitivemobs", EntitySpiderEgg.class, "spider_egg", ++id, 64, 20, true);
   }

   @OnlyIn(Dist.CLIENT)
   public static void registerRenderers() {
      net.minecraft.client.renderer.entity.EntityRenderers.register(CHAMELEON.get(), RenderChameleon::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(TREASURE_SLIME.get(), RenderTreasureSlime::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(HAUNTED_TOOL.get(), RenderHauntedTool::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(GROVESPRITE.get(), RenderGroveSprite::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(BEWITCHED_TOME.get(), RenderEchantedBook::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(FILCH_LIZARD.get(), RenderFilchLizard::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(BRAIN_SLIME.get(), RenderBrainSlime::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(ROCKET_CREEPER.get(), RenderPrimitiveCreeper::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(FESTIVE_CREEPER.get(), RenderPrimitiveCreeper::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(SUPPORT_CREEPER.get(), RenderPrimitiveCreeper::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(SKELETON_WARRIOR.get(), RenderSkeletonWarrior::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(BLAZING_JUGGERNAUT.get(), RenderBlazingJuggernaut::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(LILY_LURKER.get(), RenderLilyLurker::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(MOTHER_SPIDER.get(), RenderMotherSpider::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(BABY_SPIDER.get(), RenderBabySpider::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(TROLLAGER.get(), RenderTrollager::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(LOST_MINER.get(), RenderLostMiner::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(TRAVELING_MERCHANT.get(), RenderTravelingMerchant::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(DODO.get(), RenderDodo::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(MIMIC.get(), RenderMimic::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(SHEEPMAN.get(), RenderSheepman::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(GOBLIN.get(), RenderGoblin::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(HARPY.get(), RenderHarpy::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(FLAME_SPEWER.get(), RenderFlameSpewer::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(VOID_EYE.get(), RenderVoidEye::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(PRIMITIVE_TNT_PRIMED.get(), RenderPrimitiveTNTPrimed::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(THROWN_BLOCK.get(), RenderThownBlock::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(PRIMITIVE_EGG.get(), RenderFlyingItem::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(SPIDER_EGG.get(), RenderFlyingItem::new);
      net.minecraft.client.renderer.entity.EntityRenderers.register(FLAME_SPIT.get(), RenderFlyingItem::new);
   }

   @OnlyIn(Dist.CLIENT)
   public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
      event.registerLayerDefinition(RenderChameleon.MODEL_LAYER, ModelChameleon::createBodyLayer);
      event.registerLayerDefinition(RenderDodo.MODEL_LAYER, ModelDodo::createBodyLayer);
      event.registerLayerDefinition(RenderFilchLizard.MODEL_LAYER, ModelFilchLizard::createBodyLayer);
      event.registerLayerDefinition(RenderGroveSprite.MODEL_LAYER, ModelGroveSprite::createBodyLayer);
      event.registerLayerDefinition(RenderGroveSprite.STUMP_LAYER, ModelGroveSprite::createBodyLayer);
      event.registerLayerDefinition(RenderGroveSprite.LEAVES_LAYER, ModelGroveSprite::createLeafLayer);
      event.registerLayerDefinition(RenderLostMiner.MODEL_LAYER, ModelLostMiner::createBodyLayer);
      event.registerLayerDefinition(RenderSheepman.MODEL_LAYER, ModelSheepman::createBodyLayer);
      event.registerLayerDefinition(RenderSheepman.WOOL_LAYER, ModelSheepman::createBodyLayer);
      event.registerLayerDefinition(RenderTravelingMerchant.MODEL_LAYER, ModelTravelingMerchant::createBodyLayer);
      event.registerLayerDefinition(RenderGoblin.MODEL_LAYER, ModelGoblin::createBodyLayer);
      event.registerLayerDefinition(RenderHarpy.MODEL_LAYER, ModelHarpy::createBodyLayer);
      event.registerLayerDefinition(RenderTrollager.MODEL_LAYER, ModelTrollager::createBodyLayer);
      event.registerLayerDefinition(RenderYeti.MODEL_LAYER, ModelYeti::createBodyLayer);
      event.registerLayerDefinition(RenderBlazingJuggernaut.MODEL_LAYER, ModelBlazingJuggernaut::createBodyLayer);
      event.registerLayerDefinition(RenderLilyLurker.MODEL_LAYER, ModelLilyLurker::createBodyLayer);
      event.registerLayerDefinition(RenderEchantedBook.MODEL_LAYER, ModelEnchantedBook::createBodyLayer);
      event.registerLayerDefinition(RenderHauntedTool.MODEL_LAYER, ModelEmpty::createBodyLayer);
      event.registerLayerDefinition(RenderMimic.MODEL_LAYER, ModelMimic::createBodyLayer);
      event.registerLayerDefinition(RenderMimic.MOUTH_LAYER, ModelMimic::createBodyLayer);
      event.registerLayerDefinition(RenderPrimitiveCreeper.MODEL_LAYER, ModelPrimitiveCreeper::createBodyLayer);
      event.registerLayerDefinition(RenderPrimitiveCreeper.CHARGE_LAYER, () -> ModelPrimitiveCreeper.createBodyLayer(new CubeDeformation(2.0F)));
      event.registerLayerDefinition(RenderFlameSpewer.MODEL_LAYER, () -> ModelFlameSpewer.createBodyLayer(false));
      event.registerLayerDefinition(RenderFlameSpewer.EYES_LAYER, () -> ModelFlameSpewer.createBodyLayer(false));
      event.registerLayerDefinition(RenderFlameSpewer.LAVA_LAYER, () -> ModelFlameSpewer.createBodyLayer(true));
      event.registerLayerDefinition(RenderBrainSlime.MODEL_LAYER, ModelBrainSlime::createInnerBodyLayer);
      event.registerLayerDefinition(RenderBrainSlime.OUTER_LAYER, ModelBrainSlime::createOuterBodyLayer);
      event.registerLayerDefinition(RenderTreasureSlime.MODEL_LAYER, ModelTreasureSlime::createInnerBodyLayer);
      event.registerLayerDefinition(RenderTreasureSlime.OUTER_LAYER, ModelTreasureSlime::createOuterBodyLayer);
      event.registerLayerDefinition(RenderVoidEye.MODEL_LAYER, ModelVoidEye::createBodyLayer);
      event.registerLayerDefinition(RenderVoidEye.SEEN_LAYER, ModelVoidEye::createBodyLayer);
   }

   public static void registerAttributes(net.minecraftforge.event.entity.EntityAttributeCreationEvent event) {
      event.put(CHAMELEON.get(), net.daveyx0.primitivemobs.entity.passive.EntityChameleon.createAttributes().build());
      event.put(TREASURE_SLIME.get(), net.daveyx0.primitivemobs.entity.monster.EntityTreasureSlime.createAttributes().build());
      event.put(HAUNTED_TOOL.get(), net.daveyx0.primitivemobs.entity.monster.EntityHauntedTool.createAttributes().build());
      event.put(GROVESPRITE.get(), net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite.createAttributes().build());
      event.put(BEWITCHED_TOME.get(), net.daveyx0.primitivemobs.entity.monster.EntityEnchantedBook.createAttributes().build());
      event.put(FILCH_LIZARD.get(), net.daveyx0.primitivemobs.entity.passive.EntityFilchLizard.createAttributes().build());
      event.put(BRAIN_SLIME.get(), net.daveyx0.primitivemobs.entity.monster.EntityBrainSlime.createAttributes().build());
      event.put(ROCKET_CREEPER.get(), net.daveyx0.primitivemobs.entity.monster.EntityRocketCreeper.createAttributes().build());
      event.put(FESTIVE_CREEPER.get(), net.daveyx0.primitivemobs.entity.monster.EntityFestiveCreeper.createAttributes().build());
      event.put(SUPPORT_CREEPER.get(), net.daveyx0.primitivemobs.entity.monster.EntitySupportCreeper.createAttributes().build());
      event.put(SKELETON_WARRIOR.get(), net.daveyx0.primitivemobs.entity.monster.EntitySkeletonWarrior.createAttributes().build());
      event.put(BLAZING_JUGGERNAUT.get(), net.daveyx0.primitivemobs.entity.monster.EntityBlazingJuggernaut.createAttributes().build());
      event.put(LILY_LURKER.get(), net.daveyx0.primitivemobs.entity.monster.EntityLilyLurker.createAttributes().build());
      event.put(MOTHER_SPIDER.get(), net.daveyx0.primitivemobs.entity.monster.EntityMotherSpider.createAttributes().build());
      event.put(BABY_SPIDER.get(), net.daveyx0.primitivemobs.entity.monster.EntityBabySpider.createAttributes().build());
      event.put(TROLLAGER.get(), net.daveyx0.primitivemobs.entity.monster.EntityTrollager.createAttributes().build());
      event.put(LOST_MINER.get(), net.minecraft.world.entity.npc.Villager.createAttributes().build());
      event.put(TRAVELING_MERCHANT.get(), net.minecraft.world.entity.npc.Villager.createAttributes().build());
      event.put(DODO.get(), net.minecraft.world.entity.animal.Chicken.createAttributes().build());
      event.put(MIMIC.get(), net.daveyx0.primitivemobs.entity.monster.EntityMimic.createAttributes().build());
      event.put(SHEEPMAN.get(), net.minecraft.world.entity.npc.Villager.createAttributes().build());
      event.put(GOBLIN.get(), net.daveyx0.primitivemobs.entity.monster.EntityGoblin.createAttributes().build());
      event.put(HARPY.get(), net.daveyx0.primitivemobs.entity.monster.EntityHarpy.createAttributes().build());
      event.put(FLAME_SPEWER.get(), net.daveyx0.primitivemobs.entity.monster.EntityFlameSpewer.createAttributes().build());
      event.put(VOID_EYE.get(), net.daveyx0.primitivemobs.entity.monster.EntityVoidEye.createAttributes().build());
   }

   public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
      SpawnPlacementRegisterEvent.Operation replace = SpawnPlacementRegisterEvent.Operation.REPLACE;
      event.register(CHAMELEON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, replace);
      event.register(TREASURE_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, replace);
      event.register(HAUNTED_TOOL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(GROVESPRITE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, replace);
      event.register(BEWITCHED_TOME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(FILCH_LIZARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, replace);
      event.register(BRAIN_SLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, replace);
      event.register(ROCKET_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(FESTIVE_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(SUPPORT_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(SKELETON_WARRIOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(BLAZING_JUGGERNAUT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(
         LILY_LURKER.get(),
         SpawnPlacements.Type.IN_WATER,
         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         (type, world, spawnReason, pos, random) -> world.getFluidState(pos).is(FluidTags.WATER) && Mob.checkMobSpawnRules(type, world, spawnReason, pos, random),
         replace
      );
      event.register(MOTHER_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(BABY_SPIDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(TROLLAGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(LOST_MINER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, replace);
      event.register(TRAVELING_MERCHANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, replace);
      event.register(DODO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, replace);
      event.register(MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(SHEEPMAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, replace);
      event.register(GOBLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(HARPY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, replace);
      event.register(
         FLAME_SPEWER.get(),
         SpawnPlacements.Type.IN_LAVA,
         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         (type, world, spawnReason, pos, random) -> world.getFluidState(pos).is(FluidTags.LAVA) && world.getFluidState(pos.below()).is(FluidTags.LAVA),
         replace
      );
      event.register(
         VOID_EYE.get(),
         SpawnPlacements.Type.ON_GROUND,
         Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         (type, world, spawnReason, pos, random) -> pos.getY() <= 20 && Mob.checkMobSpawnRules(type, world, spawnReason, pos, random),
         replace
      );
   }

   public static void init(IEventBus modEventBus) {
      ENTITY_TYPES.register(modEventBus);
   }

   public static void addEntities(Class var1, String name1, int entityid, int bkEggColor, int fgEggColor, boolean flag) {
      addEntities("primitivemobs", var1, name1, entityid, bkEggColor, fgEggColor, flag);
   }

   public static void addEntitiesWithoutEgg(Class var1, String name1, int entityid, boolean flag) {
      addEntitiesWithoutEgg("primitivemobs", var1, name1, entityid, flag);
   }

   public static void addCustomEntities(Class var1, String name1, int entityid, int track, int freq, boolean vel) {
      addCustomEntities("primitivemobs", var1, name1, entityid, track, freq, vel);
   }
}
