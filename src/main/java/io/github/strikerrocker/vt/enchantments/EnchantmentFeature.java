package io.github.strikerrocker.vt.enchantments;

import com.google.common.collect.Maps;
import io.github.strikerrocker.vt.base.Feature;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentFeature extends Feature {
    static ForgeConfigSpec.BooleanValue enableBlazing;
    static ForgeConfigSpec.BooleanValue enableHops;
    static ForgeConfigSpec.BooleanValue enableNimble;
    static ForgeConfigSpec.BooleanValue enableSiphon;
    static ForgeConfigSpec.BooleanValue enableVeteran;
    static ForgeConfigSpec.BooleanValue enableVigor;
    static ForgeConfigSpec.BooleanValue enableHoming;
    private static Map<String, Tuple<Enchantment, String>> enchantments = Maps.newHashMap();

    static {
        enchantments.put("blazing", new Tuple<>(new BlazingEnchantment("blazing"), "Want to smelt things when you mine them?"));
        enchantments.put("hops", new Tuple<>(new HopsEnchantment("hops"), "Want to jump more than a block high with an enchantment?"));
        enchantments.put("nimble", new Tuple<>(new NimbleEnchantment("nimble"), "Want more speed with an enchantment?"));
        enchantments.put("siphon", new Tuple<>(new SiphonEnchantment("siphon"), "Don't want the zombies stealing your items when you are mining?"));
        enchantments.put("veteran", new Tuple<>(new VeteranEnchantment("veteran"), "Want all the experience in the nearby area?"));
        enchantments.put("vigor", new Tuple<>(new VigorEnchantment("vigor"), "Want more health with an enchant?"));
        enchantments.put("homing", new Tuple<>(new HomingEnchantment("homing"), "Don't want to aim but love shooting arrows?"));
        enchantments.forEach((name, tuple) -> MinecraftForge.EVENT_BUS.register(tuple.getA()));
    }

    @Override
    public void setupConfig(ForgeConfigSpec.Builder builder) {
        enableBlazing = builder
                .translation("config.vanillatweaks:enableBlazing")
                .comment(getComment("blazing"))
                .define("enableBlazing", true);
        enableHops = builder
                .translation("config.vanillatweaks:enableHops")
                .comment(getComment("hops"))
                .define("enableHops", true);
        enableNimble = builder
                .translation("config.vanillatweaks:enableNimble")
                .comment(getComment("nimble"))
                .define("enableNimble", true);
        enableSiphon = builder
                .translation("config.vanillatweaks:enableSiphon")
                .comment(getComment("siphon"))
                .define("enableSiphon", true);
        enableVeteran = builder
                .translation("config.vanillatweaks:enableVeteran")
                .comment(getComment("veteran"))
                .define("enableVeteran", true);
        enableVigor = builder
                .translation("config.vanillatweaks:enableVigor")
                .comment(getComment("vigor"))
                .define("enableVigor", true);
        enableHoming = builder
                .translation("config.vanillatweaks:enableHoming")
                .comment(getComment("homing"))
                .define("enableHoming", true);
    }

    private String getComment(String name) {
        return enchantments.get(name).getB();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public void registerEnchantments(RegistryEvent.Register<Enchantment> registryEvent) {
            enchantments.values().forEach(triple -> registryEvent.getRegistry().register(triple.getA()));
        }
    }
}
