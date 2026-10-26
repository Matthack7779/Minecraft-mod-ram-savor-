package com.ramsaver.mixin;

import com.ramsaver.Config;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {
	@Inject(method = "add", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$add(Particle p, CallbackInfo ci) {
		if (Config.flag("noParticleSpawn")) ci.cancel();
	}

	@Inject(method = "createParticle", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$create(ParticleOptions o, double x, double y, double z, double a, double b, double c,
			CallbackInfoReturnable<Particle> cir) {
		if (Config.flag("noParticleSpawn")) cir.setReturnValue(null);
	}
}
