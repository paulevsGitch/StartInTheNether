package paulevs.startinthenether.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.server.ServerPlayerConnectionManager;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.startinthenether.NetherSpawn;

@Mixin(ServerPlayerConnectionManager.class)
public class ServerPlayerConnectionManagerMixin {
	@Inject(method = "teleportToDimension", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/server/network/ServerPlayerPacketHandler;send(Lnet/minecraft/packet/AbstractPacket;)V",
		ordinal = 1,
		shift = Shift.BEFORE
	))
	private void startinthenether_changePlayerPos(
		ServerPlayer player, int dimensionID, CallbackInfoReturnable<ServerPlayer> info,
		@Local ServerLevel serverLevel
	) {
		if (player.y < player.level.getTopY()) return;
		NetherSpawn.spawnPlayer(player);
	}
}
