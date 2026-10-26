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
	public static final String[] LEVEL_NAMES = {"Off (vanilla)", "Light", "Medium", "Heavy", "Extreme (potato)", "INSANE (breaks stuff)", "Custom"};
	public static int level = 0; // 0-5 presets, 6 = custom
	public static final Map<String, String> values = new HashMap<>();

	private static Path file() { return FabricLoader.getInstance().getConfigDir().resolve("ramsaver.properties"); }

	public static void load() {
		for (Settings.Setting s : Settings.ALL) values.put(s.key(), "-");
		Path f = file();
		if (!Files.exists(f)) return;
		try (Reader r = Files.newBufferedReader(f)) {
			Properties p = new Properties();
			p.load(r);
			level = Math.max(0, Math.min(6, Integer.parseInt(p.getProperty("level", "0"))));
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
		if (l <= 5) for (Settings.Setting s : Settings.ALL) values.put(s.key(), s.preset(l));
	}

	public static void cycle(Settings.Setting s) {
		String[] c = s.choices();
		int i = 0;
		for (int j = 0; j < c.length; j++) if (c[j].equals(values.get(s.key()))) i = j;
		values.put(s.key(), c[(i + 1) % c.length]);
		level = 6;
	}

	/** True when the mod is active (level above Off) and the flag is set to "true". */
	public static boolean flag(String key) { return level > 0 && "true".equals(values.get(key)); }
	/** Integer value of a setting, or -1 when untouched / mod is Off. */
	public static int intVal(String key) {
		if (level == 0) return -1;
		try { String v = values.get(key); return v == null || v.equals("-") ? -1 : Integer.parseInt(v); }
		catch (Exception e) { return -1; }
	}
	private Config() {}
}
