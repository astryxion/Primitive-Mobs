package net.daveyx0.primitivemobs.core;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.client.MMItemModelManager;
import net.daveyx0.multimob.core.MMItemRegistry;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.daveyx0.primitivemobs.entity.monster.EntityFestiveCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityRocketCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntitySupportCreeper;
import net.daveyx0.primitivemobs.entity.passive.EntityDodo;
import net.daveyx0.primitivemobs.item.ItemCamouflageArmor;
import net.daveyx0.primitivemobs.item.ItemCamouflageDye;
import net.daveyx0.primitivemobs.item.ItemGoblinMace;
import net.daveyx0.primitivemobs.item.ItemGroveSpriteSap;
import net.daveyx0.primitivemobs.item.ItemPrimitive;
import net.daveyx0.primitivemobs.item.ItemPrimitiveEgg;
import net.daveyx0.primitivemobs.item.ItemPrimitiveFood;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PrimitiveMobsItems extends MMItemRegistry {

   public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "primitivemobs");

   public static final RegistryObject<Item> CAMOUFLAGE_DYE = ITEMS_REGISTRY.register("camouflage_dye",
      () -> new ItemCamouflageDye(new Item.Properties()));

   public static final RegistryObject<Item> CAMOUFLAGE_HELMET = ITEMS_REGISTRY.register("camouflage_helmet",
      () -> new ItemCamouflageArmor(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));

   public static final RegistryObject<Item> CAMOUFLAGE_CHEST = ITEMS_REGISTRY.register("camouflage_chestplate",
      () -> new ItemCamouflageArmor(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

   public static final RegistryObject<Item> CAMOUFLAGE_BOOTS = ITEMS_REGISTRY.register("camouflage_boots",
      () -> new ItemCamouflageArmor(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new Item.Properties()));

   public static final RegistryObject<Item> CAMOUFLAGE_LEGS = ITEMS_REGISTRY.register("camouflage_leggings",
      () -> new ItemCamouflageArmor(ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, new Item.Properties()));

   public static final RegistryObject<Item> RAW_DODO = ITEMS_REGISTRY.register("dodo",
      () -> new ItemPrimitiveFood(4, 1.2F, true, new Item.Properties().food(
         new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).meat().build()
      )));

   public static final RegistryObject<Item> COOKED_DODO = ITEMS_REGISTRY.register("cooked_dodo",
      () -> new ItemPrimitiveFood(8, 2.4F, true, new Item.Properties().food(
         new FoodProperties.Builder().nutrition(8).saturationMod(2.4F).meat().build()
      )));

   public static final RegistryObject<Item> DODO_EGG = ITEMS_REGISTRY.register("dodo_egg",
      () -> new ItemPrimitiveEgg(EntityDodo.class, 8, new Item.Properties()));

   public static final RegistryObject<Item> SPIDER_EGG_ITEM = ITEMS_REGISTRY.register("spider_egg",
      () -> new ItemPrimitiveEgg(EntityBabySpider.class, 1, new Item.Properties()));

   public static final RegistryObject<Item> MYSTERYEGG1 = ITEMS_REGISTRY.register("mysteryegg1",
      () -> new ItemPrimitiveEgg(EntityFestiveCreeper.class, 1, new Item.Properties()) {
         @OnlyIn(Dist.CLIENT)
         @Override
         public boolean isFoil(ItemStack stack) {
            return true;
         }
      });

   public static final RegistryObject<Item> MYSTERYEGG2 = ITEMS_REGISTRY.register("mysteryegg2",
      () -> new ItemPrimitiveEgg(EntitySupportCreeper.class, 1, new Item.Properties()) {
         @OnlyIn(Dist.CLIENT)
         @Override
         public boolean isFoil(ItemStack stack) {
            return true;
         }
      });

   public static final RegistryObject<Item> MYSTERYEGG3 = ITEMS_REGISTRY.register("mysteryegg3",
      () -> new ItemPrimitiveEgg(EntityRocketCreeper.class, 1, new Item.Properties()) {
         @OnlyIn(Dist.CLIENT)
         @Override
         public boolean isFoil(ItemStack stack) {
            return true;
         }
      });

   public static final RegistryObject<Item> MIMIC_ORB = ITEMS_REGISTRY.register("mimic_orb",
      () -> new ItemPrimitive(new Item.Properties()) {
         @OnlyIn(Dist.CLIENT)
         @Override
         public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            tooltip.add(Component.literal("Joke Item"));
            tooltip.add(Component.literal("R-Click empty Chest to change it into a Mimic"));
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
         }
      });

   public static final RegistryObject<Item> GOBLIN_MACE = ITEMS_REGISTRY.register("goblin_mace",
      () -> new ItemGoblinMace(new Item.Properties()));

   public static final RegistryObject<Item> WONDER_SAP = ITEMS_REGISTRY.register("wonder_sap",
      () -> new ItemGroveSpriteSap(new Item.Properties()));

   public static final RegistryObject<Item> SPIDER_EGGSHELL = ITEMS_REGISTRY.register("spider_eggshell",
      () -> new ItemPrimitive(new Item.Properties()) {
         @Override
         public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            if (target instanceof EntityBabySpider && attacker instanceof Player) {
               Player playerIn = (Player) attacker;
               target.hurt(target.damageSources().playerAttack(playerIn), 0.1F);
               EntityBabySpider spider = (EntityBabySpider)target;
               if (spider.isAlive() && spider.getGrowthLevel() == 0) {
                  if (!playerIn.level().isClientSide) {
                     ItemEntity entityitem = new ItemEntity(spider.level(), spider.getX(), spider.getY() + (double)0.5F, spider.getZ(), new ItemStack(PrimitiveMobsItems.SPIDER_EGG_ITEM.get()));
                     entityitem.setDefaultPickUpDelay();
                     spider.level().addFreshEntity(entityitem);
                  }

                  if (!playerIn.getAbilities().instabuild) {
                     stack.shrink(1);
                  }

                  target.discard();
               }

               return true;
            } else {
               return false;
            }
         }
      });

   public static final RegistryObject<Item> CHAMELEON_SPAWN_EGG = ITEMS_REGISTRY.register("chameleon_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.CHAMELEON, 297984, 153088, new Item.Properties()));

   public static final RegistryObject<Item> TREASURE_SLIME_SPAWN_EGG = ITEMS_REGISTRY.register("treasure_slime_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.TREASURE_SLIME, 16774041, 16769842, new Item.Properties()));

   public static final RegistryObject<Item> HAUNTED_TOOL_SPAWN_EGG = ITEMS_REGISTRY.register("haunted_tool_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.HAUNTED_TOOL, 4797973, 6835742, new Item.Properties()));

   public static final RegistryObject<Item> GROVE_SPRITE_SPAWN_EGG = ITEMS_REGISTRY.register("grovesprite_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.GROVESPRITE, 5983805, 6465327, new Item.Properties()));

   public static final RegistryObject<Item> BEWITCHED_TOME_SPAWN_EGG = ITEMS_REGISTRY.register("bewitched_tome_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.BEWITCHED_TOME, 12024373, 13684944, new Item.Properties()));

   public static final RegistryObject<Item> FILCH_LIZARD_SPAWN_EGG = ITEMS_REGISTRY.register("filch_lizard_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.FILCH_LIZARD, 12760724, 13749696, new Item.Properties()));

   public static final RegistryObject<Item> BRAIN_SLIME_SPAWN_EGG = ITEMS_REGISTRY.register("brain_slime_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.BRAIN_SLIME, 13014704, 13739453, new Item.Properties()));

   public static final RegistryObject<Item> ROCKET_CREEPER_SPAWN_EGG = ITEMS_REGISTRY.register("rocket_creeper_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.ROCKET_CREEPER, 5024208, 0, new Item.Properties()));

   public static final RegistryObject<Item> FESTIVE_CREEPER_SPAWN_EGG = ITEMS_REGISTRY.register("festive_creeper_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.FESTIVE_CREEPER, 12334600, 0, new Item.Properties()));

   public static final RegistryObject<Item> SUPPORT_CREEPER_SPAWN_EGG = ITEMS_REGISTRY.register("support_creeper_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.SUPPORT_CREEPER, 14400815, 0, new Item.Properties()));

   public static final RegistryObject<Item> SKELETON_WARRIOR_SPAWN_EGG = ITEMS_REGISTRY.register("skeleton_warrior_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.SKELETON_WARRIOR, 11248008, 7098937, new Item.Properties()));

   public static final RegistryObject<Item> BLAZING_JUGGERNAUT_SPAWN_EGG = ITEMS_REGISTRY.register("blazing_juggernaut_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.BLAZING_JUGGERNAUT, 3151900, 11577656, new Item.Properties()));

   public static final RegistryObject<Item> LILY_LURKER_SPAWN_EGG = ITEMS_REGISTRY.register("lily_lurker_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.LILY_LURKER, 5848361, 4013084, new Item.Properties()));

   public static final RegistryObject<Item> MOTHER_SPIDER_SPAWN_EGG = ITEMS_REGISTRY.register("mother_spider_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.MOTHER_SPIDER, 2426146, 11013646, new Item.Properties()));

   public static final RegistryObject<Item> BABY_SPIDER_SPAWN_EGG = ITEMS_REGISTRY.register("baby_spider_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.BABY_SPIDER, 11900008, 11013646, new Item.Properties()));

   public static final RegistryObject<Item> TROLLAGER_SPAWN_EGG = ITEMS_REGISTRY.register("trollager_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.TROLLAGER, 5669981, 3482911, new Item.Properties()));

   public static final RegistryObject<Item> YETI_SPAWN_EGG = ITEMS_REGISTRY.register("yeti_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.YETI, 15331573, 10138820, new Item.Properties()));

   public static final RegistryObject<Item> LOST_MINER_SPAWN_EGG = ITEMS_REGISTRY.register("lost_miner_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.LOST_MINER, 7091750, 12422002, new Item.Properties()));

   public static final RegistryObject<Item> TRAVELING_MERCHANT_SPAWN_EGG = ITEMS_REGISTRY.register("traveling_merchant_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.TRAVELING_MERCHANT, 6316113, 12422002, new Item.Properties()));

   public static final RegistryObject<Item> DODO_SPAWN_EGG = ITEMS_REGISTRY.register("dodo_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.DODO, 7493187, 12362124, new Item.Properties()));

   public static final RegistryObject<Item> MIMIC_SPAWN_EGG = ITEMS_REGISTRY.register("mimic_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.MIMIC, 11237677, 2762013, new Item.Properties()));

   public static final RegistryObject<Item> SHEEPMAN_SPAWN_EGG = ITEMS_REGISTRY.register("sheepman_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.SHEEPMAN, 2500134, 11769472, new Item.Properties()));

   public static final RegistryObject<Item> GOBLIN_SPAWN_EGG = ITEMS_REGISTRY.register("goblin_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.GOBLIN, 9148269, 5390647, new Item.Properties()));

   public static final RegistryObject<Item> HARPY_SPAWN_EGG = ITEMS_REGISTRY.register("harpy_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.HARPY, 4011092, 10785643, new Item.Properties()));

   public static final RegistryObject<Item> FLAME_SPEWER_SPAWN_EGG = ITEMS_REGISTRY.register("flame_spewer_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.FLAME_SPEWER, 8799790, 9268838, new Item.Properties()));

   public static final RegistryObject<Item> VOID_EYE_SPAWN_EGG = ITEMS_REGISTRY.register("void_eye_spawn_egg",
      () -> new ForgeSpawnEggItem(PrimitiveMobsEntityRegistry.VOID_EYE, 789778, 9358713, new Item.Properties()));

   public static void registerItemColors() {
      Item[] coloredItems = new Item[]{CAMOUFLAGE_HELMET.get(), CAMOUFLAGE_CHEST.get(), CAMOUFLAGE_LEGS.get(), CAMOUFLAGE_BOOTS.get(), WONDER_SAP.get()};
      MMItemModelManager.INSTANCE.registerItemColors(coloredItems);
   }

   public static void init(IEventBus modEventBus) {
      ITEMS_REGISTRY.register(modEventBus);
   }
}
