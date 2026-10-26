package com.ramsaver;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;

public final class Config {
	public static final String[] LEVEL_NAMES = {"Off (vanilla)", "Light", "Medium", "Heavy", "Extreme (potato)", "Custom"};
	public static int level = 0; // 0-4 presets, 5 = custom
	public static final Map<String, String> values = new HashMap<>();

	private static Path file() { return FabricLoader.getInstance().getConfigDir().resolve("ramsaver.properties"); }

	public static void load() {
		for (Settings.Setting s : Settings.ALL) values.put(s.key(), "-");
		Path f = file();
		if (!Files.exists(f)) return;
		try (Reader r = Files.newBufferedReader(f)) {
			Properties p = new Properties();
			p.load(r);
			level = Math.max(0, Math.min(5, Integer.parseInt(p.getProperty("level", "0"))));
			for (Settings.Setting s : Settings.ALL) values.put(s.key(), p.getProperty(s.key(), "-"));
		} catch (Exception e) { RamSaverClient.LOG.warn("Could not read config", e); }
	}

	public static void save() {
		Properties p = new Properties();
		p.setProperty("level", Integer.toString(level));
		values.forEach(p::setProperty);
		try (Writer w = Files.newBufferedWriter(file())) { p.store(w, "RAM Saver settings"); }
		catch (Exception e) { RamSaverClient.LOG.warn("Could not save config", e); }
	}

	public static void setLevel(int l) {
		level = l;
		if (l <= 4) for (Settings.Setting s : Settings.ALL) values.put(s.key(), s.preset(l));
	}

	public static void cycle(Settings.Setting s) {
		String[] c = s.choices();
		int i = 0;
		for (int j = 0; j < c.length; j++) if (c[j].equals(values.get(s.key()))) i = j;
		values.put(s.key(), c[(i + 1) % c.length]);
		level = 5;
	}
	private Config() {}
}
