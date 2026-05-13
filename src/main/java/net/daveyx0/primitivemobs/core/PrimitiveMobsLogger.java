package net.daveyx0.primitivemobs.core;

import java.util.logging.Logger;
import net.minecraft.world.level.Level;

public class PrimitiveMobsLogger {
   public static Logger PMlogger;

   public static void preInit() {
      PMlogger = Logger.getLogger("primitivemobs");
   }

   public static void info(Level world, String message) {
      if (world.isClientSide) {
         PMlogger.info("Client log: " + message);
      } else {
         PMlogger.info("Server log: " + message);
      }

   }

   public static void info(String message) {
      PMlogger.info(message);
   }

   public static void gotHere() {
      PMlogger.info("got here");
   }
}
