package com.ramsaver.mixin;

import com.ramsaver.Config;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** With sound disabled no sound file is ever decoded into memory. */
@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
	@Inject(method = "play", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$play(SoundInstance s, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
		if (Config.flag("noSound")) cir.setReturnValue(SoundEngine.PlayResult.NOT_STARTED);
	}

	@Inject(method = "playDelayed", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$delayed(SoundInstance s, int d, CallbackInfo ci) {
		if (Config.flag("noSound")) ci.cancel();
	}
}
