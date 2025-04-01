package com.example;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.CommandEvent;

import java.util.regex.Pattern;

// CommandEventHandler.java
@EventBusSubscriber(modid = LightweightIDMapper.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommandEventHandler {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%[rl]");

    @SubscribeEvent
    public static void onCommandExecution(CommandEvent event) {
        // 获取命令原始内容
        String rawCommand = event.getParseResults().getReader().getString();

        // 检查是否需要处理占位符
        if (!PLACEHOLDER_PATTERN.matcher(rawCommand).find()) {
            return;
        }

        // 获取命令来源（必须是玩家）
        CommandSourceStack source = event.getParseResults().getContext().getSource();
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }

        // 执行占位符替换
        String modifiedCommand = replacePlaceholders(rawCommand, player);

        // 若命令被修改，重新解析并执行
        if (!modifiedCommand.equals(rawCommand)) {
            event.setParseResults(parseNewCommand(modifiedCommand, event.getParseResults()));
        }
    }

    // 替换占位符为物品ID
    private static String replacePlaceholders(String command, Player player) {
        // 获取主手和副手物品
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        // 构建替换逻辑
        return PLACEHOLDER_PATTERN.matcher(command).replaceAll(match -> {
            String matched = match.group();
            switch (matched) {
                case "%r":
                    return getItemId(mainHand);
                case "%l":
                    return getItemId(offHand);
                default:
                    return matched;
            }
        });
    }

    // 获取物品的注册ID
    private static String getItemId(ItemStack stack) {
        if (stack.isEmpty()) {
            return "air"; // 空手时的默认值
        }
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }

    // 解析修改后的命令
    private static ParseResults<CommandSourceStack> parseNewCommand(
            String newCommand,
            ParseResults<CommandSourceStack> original
    ) {
        CommandDispatcher<CommandSourceStack> dispatcher = original.getContext().getDispatcher();
        return dispatcher.parse(newCommand, original.getContext().getSource());
    }
}
