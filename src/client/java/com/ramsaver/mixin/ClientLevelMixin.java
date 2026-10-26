package com.ramsaver.mixin;

import com.ramsaver.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	/** Skip client-side ticking of far-away entities (less CPU/allocation). */
	@Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$tick(Entity e, CallbackInfo ci) {
		int cap = Config.intVal("entTick");
		if (cap < 0 || e instanceof Player) return;
		Player me = Minecraft.getInstance().player;
		if (me != null && e.distanceToSqr(me) > (double) cap * cap) ci.cancel();
	}

	@Inject(method = "animateTick", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$animate(int x, int y, int z, CallbackInfo ci) {
		if (Config.flag("noAnimTick")) ci.cancel();
	}
}
