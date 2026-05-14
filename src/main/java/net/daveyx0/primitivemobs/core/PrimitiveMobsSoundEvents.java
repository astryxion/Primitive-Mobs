package net.daveyx0.primitivemobs.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PrimitiveMobsSoundEvents {

   public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, "primitivemobs");

   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BRAINSLIME_CHARGE = createSoundEvent("entity.brainslime.slimecharge");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MOTHERSPIDER_SCREECH = createSoundEvent("entity.motherspider.spiderscreech");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_ANGRY = createSoundEvent("entity.grovesprite.angry");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_DEATH = createSoundEvent("entity.grovesprite.death");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_HURT = createSoundEvent("entity.grovesprite.hurt");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_IDLE = createSoundEvent("entity.grovesprite.idle");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GROVESPRITE_THANKS = createSoundEvent("entity.grovesprite.thanks");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_IDLE = createSoundEvent("entity.trollager.idle");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_HIT = createSoundEvent("entity.trollager.hit");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_DEATH = createSoundEvent("entity.trollager.death");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TROLLAGER_ATTACK = createSoundEvent("entity.trollager.attack");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HARPY_IDLE = createSoundEvent("entity.harpy.idle");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HARPY_HURT = createSoundEvent("entity.harpy.hurt");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_FLAMESPEWER_IDLE = createSoundEvent("entity.flamespewer.idle");
   public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_VOIDEYE_IDLE = createSoundEvent("entity.voideye.idle");

   private static DeferredHolder<SoundEvent, SoundEvent> createSoundEvent(String soundName) {
      ResourceLocation soundID = ResourceLocation.fromNamespaceAndPath("primitivemobs", soundName);
      return SOUND_EVENTS.register(soundName.replace('.', '_'), () -> SoundEvent.createVariableRangeEvent(soundID));
   }

   public static void init(IEventBus modEventBus) {
      SOUND_EVENTS.register(modEventBus);
   }
}
