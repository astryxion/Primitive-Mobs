package net.daveyx0.primitivemobs.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PrimitiveMobsVillagerProfessions {
   public static final DeferredRegister<VillagerProfession> PROFESSION_REGISTRY =
      DeferredRegister.create(Registries.VILLAGER_PROFESSION, PrimitiveMobsReference.MODID);

   public static final DeferredHolder<VillagerProfession, VillagerProfession> MINER_PROFESSION = PROFESSION_REGISTRY.register("miner",
      () -> new VillagerProfession("primitivemobs:miner", holder -> false, holder -> false,
         ImmutableSet.of(), ImmutableSet.of(), null));

   public static final DeferredHolder<VillagerProfession, VillagerProfession> MERCHANT_PROFESSION = PROFESSION_REGISTRY.register("merchant",
      () -> new VillagerProfession("primitivemobs:merchant", holder -> false, holder -> false,
         ImmutableSet.of(), ImmutableSet.of(), null));

   public static final DeferredHolder<VillagerProfession, VillagerProfession> FAKE_MERCHANT_PROFESSION = PROFESSION_REGISTRY.register("fakemerchant",
      () -> new VillagerProfession("primitivemobs:fakemerchant", holder -> false, holder -> false,
         ImmutableSet.of(), ImmutableSet.of(), null));

   public static final DeferredHolder<VillagerProfession, VillagerProfession> SHEEPMAN_PROFESSION_SCAVENGER = PROFESSION_REGISTRY.register("sheepman_scavenger",
      () -> new VillagerProfession("primitivemobs:sheepman_scavenger", holder -> false, holder -> false,
         ImmutableSet.of(), ImmutableSet.of(), null));

   public static final DeferredHolder<VillagerProfession, VillagerProfession> SHEEPMAN_PROFESSION_ALCHEMIST = PROFESSION_REGISTRY.register("sheepman_alchemist",
      () -> new VillagerProfession("primitivemobs:sheepman_alchemist", holder -> false, holder -> false,
         ImmutableSet.of(), ImmutableSet.of(), null));

   public static final DeferredHolder<VillagerProfession, VillagerProfession> SHEEPMAN_PROFESSION_THIEF = PROFESSION_REGISTRY.register("sheepman_thief",
      () -> new VillagerProfession("primitivemobs:sheepman_thief", holder -> false, holder -> false,
         ImmutableSet.of(), ImmutableSet.of(), null));

   public static final Set<VillagerProfession> PROFESSIONS = new HashSet<>();
   public static final VillagerTrades.ItemListing[][] primitive_trades = new VillagerTrades.ItemListing[0][];

   public static List<List<VillagerTrades.ItemListing>> MERCHANT_TRADE_POOLS = null;

   public static void init(IEventBus modEventBus) {
      PROFESSION_REGISTRY.register(modEventBus);
   }

   public static void registerProfessions() {
      PROFESSIONS.add(MINER_PROFESSION.get());
      PROFESSIONS.add(MERCHANT_PROFESSION.get());
      PROFESSIONS.add(FAKE_MERCHANT_PROFESSION.get());
      PROFESSIONS.add(SHEEPMAN_PROFESSION_SCAVENGER.get());
      PROFESSIONS.add(SHEEPMAN_PROFESSION_ALCHEMIST.get());
      PROFESSIONS.add(SHEEPMAN_PROFESSION_THIEF.get());
   }

   public static void injectTradesIntoMap() {
      injectProfessionTrades(MINER_PROFESSION.get(), trades -> TradeHandler.registerMinerTrades(trades));
      injectProfessionTrades(MERCHANT_PROFESSION.get(), trades -> TradeHandler.registerMerchantTrades(trades));
      injectProfessionTrades(FAKE_MERCHANT_PROFESSION.get(), trades -> TradeHandler.registerFakeMerchantTrades(trades));
      injectProfessionTrades(SHEEPMAN_PROFESSION_SCAVENGER.get(), trades -> TradeHandler.registerSheepmanScavengerTrades(trades));
      injectProfessionTrades(SHEEPMAN_PROFESSION_ALCHEMIST.get(), trades -> TradeHandler.registerSheepmanAlchemistTrades(trades));
      injectProfessionTrades(SHEEPMAN_PROFESSION_THIEF.get(), trades -> TradeHandler.registerSheepmanThiefTrades(trades));
   }

   private static void injectProfessionTrades(VillagerProfession profession,
         Consumer<Int2ObjectMap<List<VillagerTrades.ItemListing>>> registrar) {
      Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>> listTrades = new Int2ObjectOpenHashMap<>();
      registrar.accept(listTrades);
      Int2ObjectOpenHashMap<VillagerTrades.ItemListing[]> arrayTrades = new Int2ObjectOpenHashMap<>();
      for (Int2ObjectMap.Entry<List<VillagerTrades.ItemListing>> entry : listTrades.int2ObjectEntrySet()) {
         arrayTrades.put(entry.getIntKey(), entry.getValue().toArray(new VillagerTrades.ItemListing[0]));
      }
      VillagerTrades.TRADES.put(profession, arrayTrades);
   }

   public static void buildMerchantTradePools() {
      if (MERCHANT_TRADE_POOLS != null) return;
      MERCHANT_TRADE_POOLS = Lists.newArrayList();

      List<VillagerTrades.ItemListing> buyPool = Lists.newArrayList();
      buyPool.add(new PrimitiveEmeraldForItems(Items.LEATHER, 10, 14));
      buyPool.add(new PrimitiveEmeraldForItems(Items.BEEF, 4, 6));
      buyPool.add(new PrimitiveEmeraldForItems(Items.PORKCHOP, 4, 6));
      buyPool.add(new PrimitiveEmeraldForItems(Items.CHICKEN, 4, 6));
      buyPool.add(new PrimitiveEmeraldForItems(Items.RABBIT, 5, 7));
      buyPool.add(new PrimitiveEmeraldForItems(Items.MUTTON, 3, 5));
      buyPool.add(new PrimitiveEmeraldForItems(Items.COD, 4, 6));
      buyPool.add(new PrimitiveEmeraldForItems(Items.CHORUS_FRUIT, 3, 5));
      MERCHANT_TRADE_POOLS.add(buyPool);

      List<VillagerTrades.ItemListing> sellPool1 = Lists.newArrayList();
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.IRON_SWORD, -10, -8));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.IRON_AXE, -10, -8));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.GOLD_INGOT, -10, -8));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.ARROW, -15, -10));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.ENDER_PEARL, -7, -4));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.COCOA_BEANS, -8, -6));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.SPECTRAL_ARROW, -16, -10));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.REDSTONE, -38, -26));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.BONE, -10, -8));
      sellPool1.add(new PrimitiveListItemForEmeralds(Items.GUNPOWDER, -16, -14));
      MERCHANT_TRADE_POOLS.add(sellPool1);

      List<VillagerTrades.ItemListing> sellPool2 = Lists.newArrayList();
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.IRON_PICKAXE, -10, -8));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.IRON_SHOVEL, -10, -8));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.IRON_HELMET, -10, -7));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.ARROW, -15, -10));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.IRON_CHESTPLATE, -20, -15));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.IRON_LEGGINGS, -10, -6));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.PRISMARINE_SHARD, -3, -2));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.QUARTZ, -6, -3));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.GOLDEN_SWORD, -6, -4));
      sellPool2.add(new PrimitiveListItemForEmeralds(Items.GOLDEN_APPLE, -6, -4));
      MERCHANT_TRADE_POOLS.add(sellPool2);

      List<VillagerTrades.ItemListing> sellPool3 = Lists.newArrayList();
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.NETHER_STAR, -1, -1));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.ENCHANTED_BOOK, 1, 4));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.DIAMOND, 2, 3));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.SADDLE, 2, 4));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.COOKED_RABBIT, -7, -4));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.EXPERIENCE_BOTTLE, -10, -5));
      sellPool3.add(new PrimitiveListItemForEmeralds(
         PotionContents.createItemStack(Items.SPLASH_POTION, Potions.POISON), -15, -10));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.SLIME_BALL, 4, 6));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.ELYTRA, -3, -2));
      sellPool3.add(new PrimitiveListItemForEmeralds(Items.NAME_TAG, 1, 1));
      MERCHANT_TRADE_POOLS.add(sellPool3);
   }

   public static VillagerTrades.ItemListing getRandomMerchantTrade(RandomSource rand, int level,
         List<VillagerTrades.ItemListing> currentTrades, List<VillagerTrades.ItemListing> tradesForLevel) {
      PrimitiveListItemForEmeralds trade = (PrimitiveListItemForEmeralds)
         tradesForLevel.get(rand.nextInt(tradesForLevel.size() - 1));
      int index = 0;
      if (level == 0) {
         index = 1;
      }

      if (!currentTrades.isEmpty() && !(currentTrades.get(0) instanceof PrimitiveEmeraldForItems)) {
         for (int i = 0; i < 100; ++i) {
            if (!trade.itemToBuy.getItem().equals(
                  ((PrimitiveListItemForEmeralds) currentTrades.get(index)).itemToBuy.getItem())) {
               return trade;
            }
            trade = (PrimitiveListItemForEmeralds) tradesForLevel.get(rand.nextInt(tradesForLevel.size() - 1));
         }
      }

      return trade;
   }

   private static void addTrade(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades,
         int level, VillagerTrades.ItemListing listing) {
      if (!trades.containsKey(level)) {
         trades.put(level, Lists.newArrayList());
      }
      trades.get(level).add(listing);
   }

   @EventBusSubscriber(modid = PrimitiveMobsReference.MODID)
   public static class TradeHandler {
      @SubscribeEvent
      public static void registerTrades(VillagerTradesEvent event) {
         Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

         if (event.getType() == MINER_PROFESSION.get()) {
            registerMinerTrades(trades);
         } else if (event.getType() == MERCHANT_PROFESSION.get()) {
            registerMerchantTrades(trades);
         } else if (event.getType() == FAKE_MERCHANT_PROFESSION.get()) {
            registerFakeMerchantTrades(trades);
         } else if (event.getType() == SHEEPMAN_PROFESSION_SCAVENGER.get()) {
            registerSheepmanScavengerTrades(trades);
         } else if (event.getType() == SHEEPMAN_PROFESSION_ALCHEMIST.get()) {
            registerSheepmanAlchemistTrades(trades);
         } else if (event.getType() == SHEEPMAN_PROFESSION_THIEF.get()) {
            registerSheepmanThiefTrades(trades);
         }
      }

      private static void registerMinerTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
         // ore_miner career
         addTrade(trades, 1, new PrimitiveListItemForEmeralds(Items.IRON_INGOT, -22, -14));
         addTrade(trades, 2, new PrimitiveListItemForEmeralds(Items.COAL_ORE, -8, -6));
         addTrade(trades, 2, new PrimitiveListItemForEmeralds(Items.IRON_ORE, -9, -7));
         addTrade(trades, 3, new PrimitiveListItemForEmeralds(Items.DIAMOND_ORE, -3, -1));
         // stone_miner career
         addTrade(trades, 1, new PrimitiveListItemForEmeralds(Items.COBBLESTONE, -60, -45));
         addTrade(trades, 2, new PrimitiveListItemForEmeralds(Items.GRANITE, -15, -10));
         addTrade(trades, 2, new PrimitiveListItemForEmeralds(Items.DIORITE, -15, -10));
         addTrade(trades, 2, new PrimitiveListItemForEmeralds(Items.ANDESITE, -15, -10));
         addTrade(trades, 3, new PrimitiveListItemForEmeralds(Items.STONE, -20, -15));
         addTrade(trades, 4, new PrimitiveListItemForEmeralds(Items.DIAMOND_ORE, -3, -1));
         // gem_miner career
         addTrade(trades, 1, new PrimitiveListItemForEmeralds(Items.LAPIS_LAZULI, -4, -3));
         addTrade(trades, 2, new PrimitiveListItemForEmeralds(Items.QUARTZ, -6, -3));
         addTrade(trades, 2, new PrimitiveListItemForEmeralds(Items.EMERALD, -6, -3));
         addTrade(trades, 3, new PrimitiveListItemForEmeralds(Items.DIAMOND, 2, 3));
      }

      private static void registerMerchantTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
         buildMerchantTradePools();
         // Level 1 (original level 0): 1 buy + 2 sell from tier 1
         addTrade(trades, 1, new RandomizedMerchantTrade(MERCHANT_TRADE_POOLS.get(0)));
         addTrade(trades, 1, new RandomizedMerchantTrade(MERCHANT_TRADE_POOLS.get(1)));
         addTrade(trades, 1, new RandomizedMerchantTrade(MERCHANT_TRADE_POOLS.get(1)));
         // Level 2 (original level 1): 2 sell from tier 2
         addTrade(trades, 2, new RandomizedMerchantTrade(MERCHANT_TRADE_POOLS.get(2)));
         addTrade(trades, 2, new RandomizedMerchantTrade(MERCHANT_TRADE_POOLS.get(2)));
         // Level 3 (original level 2): 2 sell from tier 3
         addTrade(trades, 3, new RandomizedMerchantTrade(MERCHANT_TRADE_POOLS.get(3)));
         addTrade(trades, 3, new RandomizedMerchantTrade(MERCHANT_TRADE_POOLS.get(3)));
      }

      private static void registerFakeMerchantTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
         buildMerchantTradePools();
         for (VillagerTrades.ItemListing listing : MERCHANT_TRADE_POOLS.get(0)) {
            addTrade(trades, 1, listing);
         }
         for (VillagerTrades.ItemListing listing : MERCHANT_TRADE_POOLS.get(1)) {
            addTrade(trades, 2, listing);
         }
         for (VillagerTrades.ItemListing listing : MERCHANT_TRADE_POOLS.get(2)) {
            addTrade(trades, 3, listing);
         }
         for (VillagerTrades.ItemListing listing : MERCHANT_TRADE_POOLS.get(3)) {
            addTrade(trades, 4, listing);
         }
      }

      private static void registerSheepmanScavengerTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
         addTrade(trades, 1, new ListItemForGoldIngots(Items.NETHER_WART, -6, -4));
         addTrade(trades, 2, new ListItemForGoldIngots(Items.BONE, -4, -2));
         addTrade(trades, 2, new ListItemForGoldIngots(Items.GHAST_TEAR, -10, -8));
         addTrade(trades, 3, new ListItemForGoldIngots(Items.BLAZE_ROD, 1, 2));
         addTrade(trades, 3, new ListItemForGoldIngots(Items.EMERALD, -5, -3));
         addTrade(trades, 4, new ListItemForGoldIngots(Items.DIAMOND_ORE, 12, 15));
      }

      private static void registerSheepmanAlchemistTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
         addTrade(trades, 1, new ListItemForGoldIngots(Items.MAGMA_CREAM, 2, 4));
         addTrade(trades, 1, new ListItemForGoldIngots(Items.FERMENTED_SPIDER_EYE, 1, 2));
         addTrade(trades, 2, new ListItemForGoldIngots(
            PotionContents.createItemStack(Items.POTION, Potions.FIRE_RESISTANCE), 4, 6));
         addTrade(trades, 3, new ListItemForGoldIngots(
            PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING), 12, 15));
         addTrade(trades, 3, new ListItemForGoldIngots(
            PotionContents.createItemStack(Items.POTION, Potions.STRONG_STRENGTH), 10, 13));
         addTrade(trades, 3, new ListItemForGoldIngots(
            PotionContents.createItemStack(Items.POTION, Potions.LONG_REGENERATION), 15, 18));
         addTrade(trades, 4, new ListItemForGoldIngots(
            PotionContents.createItemStack(Items.POTION, Potions.STRONG_SWIFTNESS), 32, 40));
      }

      private static void registerSheepmanThiefTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
         addTrade(trades, 1, new ListItemForGoldIngots(Items.ROTTEN_FLESH, -10, -8));
         addTrade(trades, 2, new ListItemForGoldIngots(Items.GOLDEN_SWORD, 2, 3));
         addTrade(trades, 3, new ListItemForGoldIngots(Items.GOLDEN_BOOTS, 5, 8));
         addTrade(trades, 3, new ListItemForGoldIngots(Items.GOLDEN_HELMET, 5, 8));
         addTrade(trades, 4, new ListItemForGoldIngots(Items.SADDLE, 35, 40));
      }
   }

   public static class PrimitiveListItemForEmeralds implements VillagerTrades.ItemListing {
      public ItemStack itemToBuy;
      public int priceMin;
      public int priceMax;

      public PrimitiveListItemForEmeralds(Item item, int priceMin, int priceMax) {
         this(new ItemStack(item), priceMin, priceMax);
      }

      public PrimitiveListItemForEmeralds(ItemStack stack, int priceMin, int priceMax) {
         this.itemToBuy = stack;
         this.priceMin = priceMin;
         this.priceMax = priceMax;
      }

      @Override
      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource random) {
         int i;
         if (this.priceMin >= this.priceMax) {
            i = this.priceMin;
         } else {
            i = this.priceMin + random.nextInt(this.priceMax - this.priceMin + 1);
         }

         ItemStack cost;
         ItemStack result;
         if (i < 0) {
            cost = new ItemStack(Items.EMERALD);
            result = this.itemToBuy.copyWithCount(-i);
         } else {
            cost = new ItemStack(Items.EMERALD, i);
            result = this.itemToBuy.copyWithCount(1);
         }

         return new MerchantOffer(new ItemCost(cost.getItem(), cost.getCount()), result, 12, 2, 0.05F);
      }
   }

   public static class PrimitiveEmeraldForItems implements VillagerTrades.ItemListing {
      public Item buyingItem;
      public int priceMin;
      public int priceMax;

      public PrimitiveEmeraldForItems(Item item, int priceMin, int priceMax) {
         this.buyingItem = item;
         this.priceMin = priceMin;
         this.priceMax = priceMax;
      }

      @Override
      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource random) {
         int i;
         if (this.priceMin >= this.priceMax) {
            i = this.priceMin;
         } else {
            i = this.priceMin + random.nextInt(this.priceMax - this.priceMin + 1);
         }

         return new MerchantOffer(new ItemCost(this.buyingItem, i), new ItemStack(Items.EMERALD), 12, 2, 0.05F);
      }
   }

   public static class RandomizedMerchantTrade implements VillagerTrades.ItemListing {
      private final List<VillagerTrades.ItemListing> pool;

      public RandomizedMerchantTrade(List<VillagerTrades.ItemListing> pool) {
         this.pool = pool;
      }

      @Override
      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource random) {
         if (this.pool.isEmpty()) return null;
         VillagerTrades.ItemListing selected = this.pool.get(random.nextInt(this.pool.size()));
         return selected.getOffer(trader, random);
      }
   }

   public static class ItemAndItemToEmerald implements VillagerTrades.ItemListing {
      public ItemStack buyingItemStack;
      public int buyingPriceMin;
      public int buyingPriceMax;
      public ItemStack buyingItemStack2;
      public int buyingPriceMin2;
      public int buyingPriceMax2;

      public ItemAndItemToEmerald(Item item1, int priceMin, int priceMax,
            Item item2, int priceMin2, int priceMax2) {
         this.buyingItemStack = new ItemStack(item1);
         this.buyingPriceMin = priceMin;
         this.buyingPriceMax = priceMax;
         this.buyingItemStack2 = new ItemStack(item2);
         this.buyingPriceMin2 = priceMin2;
         this.buyingPriceMax2 = priceMax2;
      }

      @Override
      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource random) {
         int i;
         if (this.buyingPriceMin >= this.buyingPriceMax) {
            i = this.buyingPriceMin;
         } else {
            i = this.buyingPriceMin + random.nextInt(this.buyingPriceMax - this.buyingPriceMin + 1);
         }

         if (this.buyingPriceMin2 < this.buyingPriceMax2) {
            random.nextInt(this.buyingPriceMax2 - this.buyingPriceMin2 + 1);
         }

         return new MerchantOffer(
            new ItemCost(this.buyingItemStack.getItem(), i),
            Optional.of(new ItemCost(this.buyingItemStack2.getItem(), i)),
            new ItemStack(Items.EMERALD),
            12, 2, 0.05F
         );
      }
   }

   public static class GoldIngotsForItems implements VillagerTrades.ItemListing {
      public Item buyingItem;
      public int priceMin;
      public int priceMax;

      public GoldIngotsForItems(Item item, int priceMin, int priceMax) {
         this.buyingItem = item;
         this.priceMin = priceMin;
         this.priceMax = priceMax;
      }

      @Override
      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource random) {
         int i = 1;
         if (this.priceMin >= this.priceMax) {
            i = this.priceMin;
         } else {
            i = this.priceMin + random.nextInt(this.priceMax - this.priceMin + 1);
         }

         return new MerchantOffer(new ItemCost(this.buyingItem, i), new ItemStack(Items.GOLD_INGOT), 12, 2, 0.05F);
      }
   }

   public static class ListItemForGoldIngots implements VillagerTrades.ItemListing {
      public ItemStack itemToBuy;
      public int priceMin;
      public int priceMax;

      public ListItemForGoldIngots(Item item, int priceMin, int priceMax) {
         this.itemToBuy = new ItemStack(item);
         this.priceMin = priceMin;
         this.priceMax = priceMax;
      }

      public ListItemForGoldIngots(ItemStack stack, int priceMin, int priceMax) {
         this.itemToBuy = stack;
         this.priceMin = priceMin;
         this.priceMax = priceMax;
      }

      @Override
      @Nullable
      public MerchantOffer getOffer(Entity trader, RandomSource random) {
         int i = 1;
         if (this.priceMin >= this.priceMax) {
            i = this.priceMin;
         } else {
            i = this.priceMin + random.nextInt(this.priceMax - this.priceMin + 1);
         }

         ItemStack cost;
         ItemStack result;
         if (i < 0) {
            cost = new ItemStack(Items.GOLD_INGOT);
            if (this.itemToBuy.getItem() instanceof PotionItem) {
               result = this.itemToBuy.copy();
            } else {
               result = new ItemStack(this.itemToBuy.getItem(), -i);
            }
         } else {
            cost = new ItemStack(Items.GOLD_INGOT, i);
            if (this.itemToBuy.getItem() instanceof PotionItem) {
               result = this.itemToBuy.copy();
            } else {
               result = new ItemStack(this.itemToBuy.getItem(), 1);
            }
         }

         return new MerchantOffer(new ItemCost(cost.getItem(), cost.getCount()), result, 12, 2, 0.05F);
      }
   }
}
