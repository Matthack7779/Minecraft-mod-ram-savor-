# RAM Saver (Fabric, Minecraft Java 26.3)

Client-side mod. Open **RAM Saver** (button top-left on Title / Pause / Options screens).

* **Level**: Off (vanilla, mod does nothing) -> Light -> Medium -> Heavy -> Extreme (potato).
* Every setting can also be set individually (click to cycle); "Vanilla" = untouched. Editing one switches to *Custom*.
* Off restores your original values. Your options.txt is not modified.
* Settings: render/simulation distance, max FPS, particles, clouds, graphics, smooth lighting, entity shadows,
  mipmaps, biome blend, entity distance, view bobbing, periodic garbage collection.
* Config file: `config/ramsaver.properties`

## Build
Needs JDK 25. `gradle build` (Gradle 9.6) -> `build/libs/ramsaver-1.0.0.jar`
Or push to GitHub: the included Actions workflow builds the jar for you (Actions -> artifact).

## Honest limits
A mod cannot change the JVM heap size. The biggest RAM saving is in your launcher's JVM arguments, e.g.
`-Xms256M -Xmx1G -XX:+UseSerialGC -XX:MaxMetaspaceSize=256M` (raise -Xmx if you crash).
Mipmap changes need F3+T to fully apply.
