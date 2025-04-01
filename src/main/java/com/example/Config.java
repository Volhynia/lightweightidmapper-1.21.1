package com.example;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = LightweightIDMapper.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
    }
}
