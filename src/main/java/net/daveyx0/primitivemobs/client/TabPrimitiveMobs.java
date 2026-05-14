package net.daveyx0.primitivemobs.client;

import net.daveyx0.primitivemobs.core.PrimitiveMobsItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TabPrimitiveMobs {

   public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "primitivemobs");

   public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PRIMITIVE_MOBS_TAB = CREATIVE_MODE_TABS.register("tabprimitivemobs",
      () -> CreativeModeTab.builder()
         .title(Component.literal("PrimitiveMobs"))
         .icon(() -> new ItemStack(PrimitiveMobsItems.CAMOUFLAGE_DYE.get()))
         .displayItems((parameters, output) -> {
            output.accept(PrimitiveMobsItems.CAMOUFLAGE_DYE.get());
            output.accept(PrimitiveMobsItems.CAMOUFLAGE_HELMET.get());
            output.accept(PrimitiveMobsItems.CAMOUFLAGE_CHEST.get());
            output.accept(PrimitiveMobsItems.CAMOUFLAGE_LEGS.get());
            output.accept(PrimitiveMobsItems.CAMOUFLAGE_BOOTS.get());
            output.accept(PrimitiveMobsItems.RAW_DODO.get());
            output.accept(PrimitiveMobsItems.COOKED_DODO.get());
            output.accept(PrimitiveMobsItems.DODO_EGG.get());
            output.accept(PrimitiveMobsItems.MIMIC_ORB.get());
            output.accept(PrimitiveMobsItems.GOBLIN_MACE.get());
            output.accept(PrimitiveMobsItems.WONDER_SAP.get());
            output.accept(PrimitiveMobsItems.SPIDER_EGG_ITEM.get());
            output.accept(PrimitiveMobsItems.MYSTERYEGG1.get());
            output.accept(PrimitiveMobsItems.MYSTERYEGG2.get());
            output.accept(PrimitiveMobsItems.MYSTERYEGG3.get());
            output.accept(PrimitiveMobsItems.SPIDER_EGGSHELL.get());
            output.accept(PrimitiveMobsItems.CHAMELEON_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.TREASURE_SLIME_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.HAUNTED_TOOL_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.GROVE_SPRITE_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.BEWITCHED_TOME_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.FILCH_LIZARD_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.BRAIN_SLIME_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.ROCKET_CREEPER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.FESTIVE_CREEPER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.SUPPORT_CREEPER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.SKELETON_WARRIOR_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.BLAZING_JUGGERNAUT_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.LILY_LURKER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.MOTHER_SPIDER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.BABY_SPIDER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.TROLLAGER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.LOST_MINER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.TRAVELING_MERCHANT_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.DODO_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.MIMIC_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.SHEEPMAN_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.GOBLIN_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.HARPY_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.FLAME_SPEWER_SPAWN_EGG.get());
            output.accept(PrimitiveMobsItems.VOID_EYE_SPAWN_EGG.get());
         })
         .build());

   public static void init(IEventBus modEventBus) {
      CREATIVE_MODE_TABS.register(modEventBus);
   }
}
