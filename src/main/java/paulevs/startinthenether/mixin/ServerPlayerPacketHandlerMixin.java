package paulevs.startinthenether.mixin;

import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.packet.play.RespawnPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerPacketHandler.class)
public class ServerPlayerPacketHandlerMixin {
	@Shadow private ServerPlayer serverPlayer;
	@Shadow private MinecraftServer server;
	
	@Inject(method = "onRespawn", at = @At("HEAD"), cancellable = true)
	private void startinthenether_modifyDimension(RespawnPacket packet, CallbackInfo info) {
		if (serverPlayer.health > 0) return;
		serverPlayer = server.serverPlayerConnectionManager.teleportToDimension(serverPlayer, -1);
		serverPlayer.dimensionId = -1;
		info.cancel();
	}
}
