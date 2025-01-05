package paulevs.startinthenether.mixin;

import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
		if (y >= level.getTopY()) {
			int cx = (int) x;
			int cz = (int) z;
			PlayerEntity player = PlayerEntity.class.cast(this);
			for (int i = 0; i < 256; i++) {
				x = cx + level.random.nextInt(128) - 63.5;
				z = cz + level.random.nextInt(128) - 63.5;
				for (y = level.getBottomY(); y < level.getTopY(); y += 1) {
					if (level.getCollidingEntities(player, player.boundingBox).isEmpty()) return;
				}
			}
		}
	}
}
