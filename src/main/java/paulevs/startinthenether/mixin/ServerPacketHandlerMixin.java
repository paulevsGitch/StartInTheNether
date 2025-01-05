package paulevs.startinthenether.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.packet.login.LoginRequestPacket;
import net.minecraft.server.network.ServerPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPacketHandler.class)
public class ServerPacketHandlerMixin {
	@Inject(method = "complete", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/server/ServerPlayerConnectionManager;method_566(Lnet/minecraft/entity/living/player/ServerPlayer;)V",
		shift = Shift.BEFORE
	))
	private void startinthenether_modifyDimension(LoginRequestPacket packet, CallbackInfo info, @Local ServerPlayer player) {
		player.dimensionId = -1;
	}
}
