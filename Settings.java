package com.ramsaver;

import java.util.List;

/** Every individual setting the mod can control. "-" means "leave vanilla value alone". */
public final class Settings {
	public record Setting(String key, String label, String[] optionNames, String[] choices,
			String light, String medium, String heavy, String extreme) {
		public String preset(int level) {
			return switch (level) { case 1 -> light; case 2 -> medium; case 3 -> heavy; case 4 -> extreme; default -> "-"; };
		}
	}

	private static Setting s(String k, String l, String[] names, String[] c, String a, String b, String d, String e) {
		return new Setting(k, l, names, c, a, b, d, e);
	}
	private static String[] a(String... v) { return v; }

	public static final String GC = "gcSeconds";

	public static final List<Setting> ALL = List.of(
		s("renderDistance", "Render distance", a("renderDistance"), a("-","2","3","4","5","6","8","10","12"), "8","6","4","2"),
		s("simDistance", "Simulation distance", a("simulationDistance"), a("-","5","6","8","10","12"), "8","6","5","5"),
		s("maxFps", "Max FPS", a("framerateLimit","maxFps"), a("-","30","60","120"), "-","60","60","30"),
		s("particles", "Particles", a("particles"), a("-","DECREASED","MINIMAL"), "-","DECREASED","MINIMAL","MINIMAL"),
		s("clouds", "Clouds", a("cloudStatus","clouds"), a("-","FAST","OFF"), "-","FAST","OFF","OFF"),
		s("graphics", "Graphics", a("graphicsMode","graphicsStatus"), a("-","FAST"), "-","FAST","FAST","FAST"),
		s("ao", "Smooth lighting", a("ambientOcclusion"), a("-","false"), "-","-","false","false"),
		s("shadows", "Entity shadows", a("entityShadows"), a("-","false"), "-","false","false","false"),
		s("mipmaps", "Mipmap levels", a("mipmapLevels"), a("-","0","1","2","4"), "-","2","0","0"),
		s("biomeBlend", "Biome blend", a("biomeBlendRadius"), a("-","0","1","3","5"), "-","1","0","0"),
		s("entityDist", "Entity distance", a("entityDistanceScaling"), a("-","0.5","0.75","1.0"), "-","-","0.5","0.5"),
		s("bobbing", "View bobbing", a("bobView"), a("-","false"), "-","-","-","false"),
		s(GC, "Memory cleanup (GC) every", a(), a("-","30","60","120","300"), "300","120","60","30")
	);

	public static String display(Setting s, String v) {
		if (v.equals("-")) return s.key().equals(GC) ? "Off" : "Vanilla";
		if (s.key().equals(GC)) return v + "s";
		return switch (v) { case "false" -> "Off"; default -> v; };
	}
	private Settings() {}
}
