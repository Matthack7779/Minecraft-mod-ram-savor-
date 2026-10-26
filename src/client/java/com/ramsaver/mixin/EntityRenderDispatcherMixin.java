package com.ramsaver.mixin;

import com.ramsaver.Config;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$shouldRender(Entity e, Frustum f, double x, double y, double z, float pt, CallbackInfoReturnable<Boolean> cir) {
		int cap = Config.intVal("entRender");
		if (cap < 0 || e instanceof Player) return;
		if (cap == 0 || e.distanceToSqr(x, y, z) > (double) cap * cap) cir.setReturnValue(false);
	}
}
