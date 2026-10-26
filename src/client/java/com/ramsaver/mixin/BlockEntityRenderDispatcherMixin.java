package com.ramsaver.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ramsaver.Config;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
	@Inject(method = "submit", at = @At("HEAD"), cancellable = true, require = 0)
	private void ramsaver$submit(BlockEntityRenderState s, PoseStack p, SubmitNodeCollector c, CameraRenderState cam, CallbackInfo ci) {
		if (Config.flag("noBlockEntities")) ci.cancel();
	}
}
