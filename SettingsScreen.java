package com.ramsaver;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends Screen {
	private final Screen parent;

	public SettingsScreen(Screen parent) {
		super(Component.literal("RAM Saver Settings"));
		this.parent = parent;
	}

	private void changed() {
		Config.save();
		OptionTweaker.applyAll(this.minecraft);
		this.rebuildWidgets();
	}

	@Override
	protected void init() {
		int cx = this.width / 2;
		Button title = Button.builder(Component.literal("RAM Saver Settings"), b -> {}).bounds(cx - 150, 10, 300, 20).build();
		title.active = false;
		this.addRenderableWidget(title);

		this.addRenderableWidget(Button.builder(
				Component.literal("RAM saving level: " + Config.LEVEL_NAMES[Config.level]),
				b -> { Config.setLevel(Config.level >= 4 ? 0 : Config.level + 1); changed(); })
				.bounds(cx - 150, 36, 300, 20).build());

		int i = 0;
		for (Settings.Setting s : Settings.ALL) {
			int col = i % 2, row = i / 2;
			String shown = Settings.display(s, Config.values.getOrDefault(s.key(), "-"));
			this.addRenderableWidget(Button.builder(Component.literal(s.label() + ": " + shown),
					b -> { Config.cycle(s); changed(); })
					.bounds(cx - 152 + col * 154, 64 + row * 24, 150, 20).build());
			i++;
		}

		this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
				.bounds(cx - 100, this.height - 28, 200, 20).build());
	}

	@Override
	public void onClose() {
		this.minecraft.setScreenAndShow(parent);
	}
}
