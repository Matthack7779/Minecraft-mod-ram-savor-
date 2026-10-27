package com.ramsaver;

import java.util.List;

/** Every individual setting. "-" = leave vanilla value alone. Presets cover levels 1..5. */
public final class Settings {
	public record Setting(String key, String label, String[] optionNames, String[] choices, String[] presets) {
		public String preset(int level) { return level >= 1 && level <= 5 ? presets[level - 1] : "-"; }
	}

	private static Setting s(String k, String l, String[] names, String[] c, String... p) { return new Setting(k, l, names, c, p); }
	private static String[] a(String... v) { return v; }
	private static final String[] NONE = new String[0];

	public static final String GC = "gcSeconds";

	// 35 settings. Preset order: Light, Medium, Heavy, Extreme, Insane
	public static final List<Setting> ALL = List.of(
		// ---- page 1: world / entities / memory
		s("renderDistance", "Render distance", a("renderDistance"), a("-","2","3","4","5","6","8","10","12"), "8","6","4","2","2"),
		s("simDistance", "Simulation distance", a("simulationDistance"), a("-","5","6","8","10","12"), "8","6","5","5","5"),
		s("entRender", "Draw entities within", NONE, a("-","64","32","16","8","0"), "-","-","32","16","0"),
		s("entTick", "Tick entities within", NONE, a("-","64","32","16"), "-","-","-","32","16"),
		s("entityDist", "Entity distance scale", a("entityDistanceScaling"), a("-","0.5","0.75","1.0"), "-","-","0.5","0.5","0.5"),
		s("maxFps", "Max FPS", a("framerateLimit"), a("-","30","60","120"), "-","60","60","30","30"),
		s("afkFps", "FPS limit when idle", a("inactivityFpsLimit"), a("-","MINIMIZED","AFK"), "-","AFK","AFK","AFK","AFK"),
		s(GC, "Memory cleanup (GC) every", NONE, a("-","15","30","60","120","300"), "300","120","60","30","15"),
		s("particles", "Particles", a("particles"), a("-","DECREASED","MINIMAL"), "-","DECREASED","MINIMAL","MINIMAL","MINIMAL"),
		s("noParticleSpawn", "Block ALL particles", NONE, a("-","true"), "-","-","-","true","true"),
		s("noAnimTick", "Ambient block effects", NONE, a("-","true"), "-","-","true","true","true"),
		s("weather", "Weather radius", a("weatherRadius"), a("-","3","5","10"), "-","-","3","3","3"),
		// ---- page 2: visuals / textures
		s("graphics", "Graphics preset", a("graphicsPreset"), a("-","FAST"), "-","FAST","FAST","FAST","FAST"),
		s("clouds", "Clouds", a("cloudStatus"), a("-","FAST","OFF"), "-","FAST","OFF","OFF","OFF"),
		s("ao", "Smooth lighting", a("ambientOcclusion"), a("-","false"), "-","-","false","false","false"),
		s("shadows", "Entity shadows", a("entityShadows"), a("-","false"), "-","false","false","false","false"),
		s("leaves", "Cutout leaves", a("cutoutLeaves"), a("-","false"), "-","-","false","false","false"),
		s("vignette", "Vignette", a("vignette"), a("-","false"), "-","false","false","false","false"),
		s("transp", "Improved transparency", a("improvedTransparency"), a("-","false"), "-","-","false","false","false"),
		s("chunkFade", "Chunk fade-in time", a("chunkSectionFadeInTime"), a("-","0.0"), "-","-","0.0","0.0","0.0"),
		s("mipmaps", "Mipmap levels", a("mipmapLevels"), a("-","0","1","2","4"), "-","2","0","0","0"),
		s("texFilter", "Texture filtering", a("textureFiltering"), a("-","NONE"), "-","-","NONE","NONE","NONE"),
		s("biomeBlend", "Biome blend", a("biomeBlendRadius"), a("-","0","1","3","5"), "-","1","0","0","0"),
		// ---- page 3: hardcore removal / effects
		s("noBlockEntities", "Hide chests/signs/etc", NONE, a("-","true"), "-","-","-","true","true"),
		s("noSound", "Disable all sound", NONE, a("-","true"), "-","-","-","-","true"),
		s("subtitles", "Subtitles", a("showSubtitles"), a("-","false"), "-","false","false","false","false"),
		s("dirAudio", "Directional audio", a("directionalAudio"), a("-","false"), "-","-","false","false","false"),
		s("bobbing", "View bobbing", a("bobView"), a("-","false"), "-","-","-","false","false"),
		s("glint", "Enchant glint strength", a("glintStrength"), a("-","0.0"), "-","-","-","0.0","0.0"),
		s("dmgTilt", "Damage tilt", a("damageTiltStrength"), a("-","0.0"), "-","-","-","0.0","0.0"),
		s("screenFx", "Screen effects", a("screenEffectScale"), a("-","0.0"), "-","-","-","0.0","0.0"),
		s("fovFx", "FOV effects", a("fovEffectScale"), a("-","0.0"), "-","-","-","0.0","0.0"),
		s("menuBlur", "Menu background blur", a("menuBackgroundBlurriness"), a("-","0"), "-","-","0","0","0"),
		s("panorama", "Title panorama speed", a("panoramaSpeed"), a("-","0.0"), "-","-","0.0","0.0","0.0"),
		s("realms", "Realms notifications", a("realmsNotifications"), a("-","false"), "-","false","false","false","false")
	);

	public static String display(Setting s, String v) {
		if (v.equals("-")) return s.key().equals(GC) ? "Off" : "Vanilla";
		switch (s.key()) {
			case GC: return v + "s";
			case "entRender": return v.equals("0") ? "Players only" : v + " blocks";
			case "entTick": return v + " blocks";
			default:
		}
		return switch (v) { case "false" -> "Off"; case "true" -> "On"; default -> v; };
	}
	private Settings() {}
}
