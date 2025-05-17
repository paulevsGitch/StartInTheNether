package paulevs.startinthenether.mixin;

import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.startinthenether.NetherSpawn;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	public PlayerEntityMixin(Level level) {
		super(level);
	}
	
	@Inject(method = "afterSpawn", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/living/LivingEntity;afterSpawn()V",
		shift = Shift.AFTER
	))
	private void startinthenether_changePlayerPos(CallbackInfo info) {
		if (y < level.getTopY()) return;
		NetherSpawn.spawnPlayer(PlayerEntity.class.cast(this));
	}
}
