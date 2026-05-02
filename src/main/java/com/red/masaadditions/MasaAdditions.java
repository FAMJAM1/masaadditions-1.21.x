package com.red.masaadditions;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MasaAdditions.MOD_ID)
public class MasaAdditions {
    public static final String MOD_ID = "masaadditions";
    public static final String MOD_NAME = "MasaAdditions";
    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public MasaAdditions(IEventBus modEventBus) {
        logger.info("MasaAdditions Loaded.");
    }
}
