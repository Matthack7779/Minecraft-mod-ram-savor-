package com.ramsaver;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SettingsScreen extends Screen {
	private final Screen parent;
	private static int page = 0;
	private static final int PER_PAGE = 12;

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

		int pages = (Settings.ALL.size() + PER_PAGE - 1) / PER_PAGE;
		this.addRenderableWidget(Button.builder(
				Component.literal("Level: " + Config.LEVEL_NAMES[Config.level]),
				b -> { Config.setLevel(Config.level >= 5 ? 0 : Config.level + 1); changed(); })
				.bounds(cx - 152, 34, 196, 20).build());
		this.addRenderableWidget(Button.builder(Component.literal("<"), b -> { page = (page + pages - 1) % pages; this.rebuildWidgets(); })
				.bounds(cx + 48, 34, 24, 20).build());
		Button pg = Button.builder(Component.literal((page + 1) + "/" + pages), b -> {}).bounds(cx + 74, 34, 52, 20).build();
		pg.active = false;
		this.addRenderableWidget(pg);
		this.addRenderableWidget(Button.builder(Component.literal(">"), b -> { page = (page + 1) % pages; this.rebuildWidgets(); })
				.bounds(cx + 128, 34, 24, 20).build());

		int start = page * PER_PAGE;
		for (int i = 0; i < PER_PAGE && start + i < Settings.ALL.size(); i++) {
			Settings.Setting s = Settings.ALL.get(start + i);
			int col = i % 2, row = i / 2;
			String shown = Settings.display(s, Config.values.getOrDefault(s.key(), "-"));
			this.addRenderableWidget(Button.builder(Component.literal(s.label() + ": " + shown),
					b -> { Config.cycle(s); changed(); })
					.bounds(cx - 152 + col * 154, 60 + row * 22, 150, 20).build());
		}

		this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
				.bounds(cx - 100, this.height - 26, 200, 20).build());
	}

	@Override
	public void onClose() {
		this.minecraft.setScreenAndShow(parent);
	}
}
