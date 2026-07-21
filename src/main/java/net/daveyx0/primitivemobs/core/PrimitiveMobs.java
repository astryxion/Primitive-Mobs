package net.daveyx0.primitivemobs.core;

import net.daveyx0.primitivemobs.client.TabPrimitiveMobs;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfig;
import net.daveyx0.primitivemobs.config.PrimitiveMobsFactoryGui;
import net.daveyx0.primitivemobs.crafting.PrimitiveMobsRecipeSerializers;
import net.daveyx0.primitivemobs.message.PrimitiveMobsMessageRegistry;
import net.daveyx0.multimob.spawn.MMSpawnRegistry;
import net.daveyx0.primitivemobs.world.gen.WorldGenMimic;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("primitivemobs")
public class PrimitiveMobs {
   public static final Logger LOGGER = LogManager.getLogger("primitivemobs");
   public static PrimitiveMobs instance;

   public PrimitiveMobs() {
      instance = this;
      IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

      PrimitiveMobsEntityRegistry.init(modEventBus);
      PrimitiveMobsSoundEvents.init(modEventBus);
      PrimitiveMobsItems.init(modEventBus);
      TabPrimitiveMobs.init(modEventBus);
      PrimitiveMobsRecipeSerializers.init(modEventBus);
      PrimitiveMobsVillagerProfessions.init(modEventBus);
      WorldGenMimic.init(modEventBus);

      modEventBus.addListener(this::commonSetup);
      modEventBus.addListener(this::clientSetup);
      modEventBus.addListener(PrimitiveMobsConfig::onConfigChanged);
      modEventBus.addListener(PrimitiveMobsEntityRegistry::registerLayerDefinitions);
      modEventBus.addListener(PrimitiveMobsEntityRegistry::registerAttributes);
      modEventBus.addListener(PrimitiveMobsEntityRegistry::registerSpawnPlacements);

      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PrimitiveMobsConfig.CONFIG_SPEC, "primitivemobs/primitivemobs.toml");
      ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class, PrimitiveMobsFactoryGui::getFactory);

      MinecraftForge.EVENT_BUS.register(this);
      MinecraftForge.EVENT_BUS.register(new PrimitiveMobsEvents.EntityEventHandler());

      // Register before any entity AttachCapabilitiesEvent can fire.
      PrimitiveTameableEntries.registerTameables();
   }

   private void commonSetup(final FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
         PrimitiveMobsLogger.preInit();
         PrimitiveMobsMessageRegistry.registerMessages();
         PrimitiveMobsLootTables.registerLootTables();
         PrimitiveMobsMapGen.registerWorldGenerators();
         PrimitiveMobsSpawnRegistry.registerSpawns();
         MMSpawnRegistry.loadSpawns();
         PrimitiveMobsRecipes.registerRecipes();
         PrimitiveMobsVillagerProfessions.registerProfessions();
         PrimitiveMobsVillagerProfessions.injectTradesIntoMap();
         PrimitiveMobsConfig.load();
      });
   }

   private void clientSetup(final FMLClientSetupEvent event) {
      event.enqueueWork(() -> {
         PrimitiveMobsEntityRegistry.registerRenderers();
         PrimitiveMobsItems.registerItemColors();
      });
   }
}
