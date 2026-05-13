package net.daveyx0.primitivemobs.world.gen;

import com.mojang.serialization.Codec;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigMobs;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WorldGenMimic extends Feature<NoneFeatureConfiguration> {

   public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, "primitivemobs");

   public static final RegistryObject<Feature<NoneFeatureConfiguration>> MIMIC_FEATURE = FEATURES.register("mimic_chest",
      () -> new WorldGenMimic(NoneFeatureConfiguration.CODEC));

   public WorldGenMimic(Codec<NoneFeatureConfiguration> codec) {
      super(codec);
   }

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      WorldGenLevel world = context.level();
      BlockPos origin = context.origin();

      int chance = PrimitiveMobsConfigSpecial.getMimicRarity();
      if (!PrimitiveMobsConfigMobs.enableMimic || !PrimitiveMobsConfigSpecial.getMimicGeneratesInCaves() || chance <= 0) {
         return false;
      }

      if (context.random().nextInt(chance) != 0) {
         return false;
      }

      BlockPos tempPos = new BlockPos(origin.getX() + context.random().nextInt(16) + 8, 0, origin.getZ() + context.random().nextInt(16) + 8);
      BlockPos newPos = this.getAboveSolidOrLiquidBlock(world, tempPos);
      if (newPos != null) {
         world.setBlock(newPos, Blocks.CHEST.defaultBlockState(), 3);
         BlockEntity tileEntity = world.getBlockEntity(newPos);
         if (tileEntity instanceof ChestBlockEntity) {
            ((ChestBlockEntity)tileEntity).getPersistentData().putInt("Mimic", 1);
         }
         return true;
      }

      return false;
   }

   public BlockPos getAboveSolidOrLiquidBlock(WorldGenLevel world, BlockPos pos) {
      BlockPos blockpos = null;

      for(int i = 5; i < 40; ++i) {
         blockpos = new BlockPos(pos.getX(), i, pos.getZ());
         BlockPos blockpos1 = blockpos.below();
         BlockState state1 = world.getBlockState(blockpos1);
         if (world.isEmptyBlock(blockpos) && state1.isSolid() && !state1.getBlock().defaultBlockState().is(net.minecraft.tags.BlockTags.LEAVES)) {
            break;
         }

         blockpos = null;
      }

      return blockpos;
   }

   public static void register() {
   }

   public static void init(IEventBus modEventBus) {
      FEATURES.register(modEventBus);
   }
}
