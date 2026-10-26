package com.ramsaver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;

/**
 * Sets vanilla options by name via reflection, so a renamed option in a future
 * Minecraft version just gets skipped (and logged) instead of crashing the game.
 * Original values are remembered so "Off" / "Vanilla" restores them.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class OptionTweaker {
	private static final Map<String, Object> originals = new HashMap<>();

	private static OptionInstance find(Options o, String[] names) {
		for (String n : names) {
			try {
				Method m = Options.class.getMethod(n);
				Object r = m.invoke(o);
				if (r instanceof OptionInstance oi) return oi;
			} catch (Throwable ignored) {}
		}
		return null;
	}

	private static Object convert(Object cur, String v) {
		try {
			if (cur instanceof Integer) return Integer.valueOf(v);
			if (cur instanceof Boolean) return Boolean.valueOf(v);
			if (cur instanceof Double) return Double.valueOf(v);
			if (cur instanceof Enum<?> e)
				for (Object c : e.getDeclaringClass().getEnumConstants())
					if (((Enum<?>) c).name().equalsIgnoreCase(v)) return c;
		} catch (Exception ignored) {}
		return null;
	}

	public static void applyAll(Minecraft mc) {
		Options o = mc.options;
		if (o == null) return;
		for (Settings.Setting s : Settings.ALL) {
			if (s.optionNames().length == 0) continue;
			String v = Config.values.getOrDefault(s.key(), "-");
			if (Config.level == 0) v = "-";
			try {
				OptionInstance oi = find(o, s.optionNames());
				if (oi == null) { RamSaverClient.LOG.warn("Option not found for '{}', skipping", s.key()); continue; }
				if (v.equals("-")) {
					if (originals.containsKey(s.key())) oi.set(originals.remove(s.key()));
					continue;
				}
				Object target = convert(oi.get(), v);
				if (target == null) continue;
				originals.putIfAbsent(s.key(), oi.get());
				oi.set(target);
			} catch (Throwable t) {
				RamSaverClient.LOG.warn("Failed to apply '{}'", s.key(), t);
			}
		}
	}
	private OptionTweaker() {}
}
