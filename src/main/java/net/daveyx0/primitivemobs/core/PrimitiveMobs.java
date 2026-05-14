package net.daveyx0.primitivemobs.core;

import net.daveyx0.primitivemobs.client.TabPrimitiveMobs;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfig;
import net.daveyx0.primitivemobs.config.PrimitiveMobsFactoryGui;
import net.daveyx0.primitivemobs.crafting.PrimitiveMobsRecipeSerializers;
import net.daveyx0.primitivemobs.message.PrimitiveMobsMessageRegistry;
import net.daveyx0.multimob.spawn.MMSpawnRegistry;
import net.daveyx0.primitivemobs.world.gen.WorldGenMimic;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("primitivemobs")
public class PrimitiveMobs {
   public static final Logger LOGGER = LogManager.getLogger("primitivemobs");
   public static PrimitiveMobs instance;

   public PrimitiveMobs() {
      instance = this;
      ModLoadingContext context = ModLoadingContext.get();
      IEventBus modEventBus = context.getActiveContainer().getEventBus();

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
      modEventBus.addListener(PrimitiveMobsMessageRegistry::registerPayloads);

      context.getActiveContainer().registerConfig(ModConfig.Type.COMMON, PrimitiveMobsConfig.CONFIG_SPEC, "primitivemobs/primitivemobs.toml");
      if (FMLEnvironment.dist == Dist.CLIENT) {
         context.getActiveContainer().registerExtensionPoint(net.neoforged.neoforge.client.gui.IConfigScreenFactory.class, PrimitiveMobsFactoryGui.getFactory());
      }
   }

   private void commonSetup(final FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
         PrimitiveMobsLogger.preInit();
         PrimitiveMobsMessageRegistry.registerMessages();
         PrimitiveMobsLootTables.registerLootTables();
         PrimitiveMobsMapGen.registerWorldGenerators();
         PrimitiveMobsSpawnRegistry.registerSpawns();
         MMSpawnRegistry.loadSpawns();
         PrimitiveTameableEntries.registerTameables();
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
