package com.example;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// LightweightIDMapper.java
@Mod(LightweightIDMapper.MODID)
public class LightweightIDMapper {
    public static final String MODID = "lightweightidmapper";

    public LightweightIDMapper(IEventBus modBus) {
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }
}