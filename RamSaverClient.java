package com.ramsaver;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RamSaverClient implements ClientModInitializer {
	public static final Logger LOG = LoggerFactory.getLogger("ramsaver");
	private static boolean started = false;
	private static int ticks = 0;

	@Override
	public void onInitializeClient() {
		Config.load();

		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			if (!started && mc.options != null) {
				started = true;
				OptionTweaker.applyAll(mc);
			}
			int secs = parseGc();
			if (secs > 0 && ++ticks >= secs * 20) {
				ticks = 0;
				Thread t = new Thread(System::gc, "ramsaver-gc");
				t.setDaemon(true);
				t.start();
			}
		});

		// Adds a "RAM Saver" button (top-left) to the title, pause and options screens.
		ScreenEvents.AFTER_INIT.register((mc, screen, w, h) -> {
			String n = screen.getClass().getSimpleName();
			if (n.equals("TitleScreen") || n.equals("PauseScreen") || n.equals("OptionsScreen")) {
				Screens.getWidgets(screen).add(Button.builder(Component.literal("RAM Saver"),
						b -> mc.setScreenAndShow(new SettingsScreen(screen))).bounds(4, 4, 80, 20).build());
			}
		});
	}

	static int parseGc() {
		try { String v = Config.values.getOrDefault(Settings.GC, "-"); return Config.level == 0 || v.equals("-") ? 0 : Integer.parseInt(v); }
		catch (Exception e) { return 0; }
	}
}
