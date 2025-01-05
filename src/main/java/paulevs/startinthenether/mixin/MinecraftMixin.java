package paulevs.startinthenether.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MovementManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.DeathScreen;
import net.minecraft.client.options.GameOptions;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.AbstractClientPlayer;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.ChunkCache;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.dimension.NetherPortalManager;
import net.minecraft.level.source.LevelSource;
import net.minecraft.util.maths.Vec3IMutable;
import net.modificationstation.stationapi.api.world.dimension.DimensionHelper;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow public ClientInteractionManager interactionManager;
	@Shadow public AbstractClientPlayer player;
	@Shadow public LivingEntity viewEntity;
	@Shadow public Screen currentScreen;
	@Shadow public GameOptions options;
	@Shadow public Level level;
	
	@Shadow protected abstract void loadLevelWithNotify(String message);
	@Shadow public abstract void openScreen(Screen screen);
	
	@Inject(method = "method_2122", at = @At("HEAD"), cancellable = true)
	private void startinthenether_switchDimension(boolean flag, int i, CallbackInfo info) {
		info.cancel();
		
		if (player.dimensionId != -1) {
			DimensionHelper.switchDimension(player, VanillaDimensions.THE_NETHER, 1.0, new NetherPortalManager());
		}
		
		Vec3IMutable spawnPos = null;
		Vec3IMutable storedSpawnPos = null;
		boolean var5 = true;
		if (this.player != null && !flag) {
			spawnPos = this.player.getSpawnPos();
			if (spawnPos != null) {
				storedSpawnPos = PlayerEntity.getRespawnPos(level, spawnPos);
				if (storedSpawnPos == null) {
					this.player.sendMessage("tile.bed.notValid");
				}
			}
		}
		
		if (storedSpawnPos == null) {
			storedSpawnPos = level.getSpawnPosition();
			var5 = false;
		}
		
		LevelSource source = level.getCache();
		if (source instanceof ChunkCache cache) {
			cache.setCenter(storedSpawnPos.x >> 4, storedSpawnPos.z >> 4);
		}
		
		level.shuffleSpawnPoint();
		level.removeClientEntities();
		int newID = 0;
		
		if (this.player != null) {
			newID = this.player.entityId;
			level.removeEntity(this.player);
		}
		
		viewEntity = null;
		player = (AbstractClientPlayer) this.interactionManager.makePlayer(level);
		player.dimensionId = i;
		viewEntity = player;
		player.afterSpawn();
		
		if (var5) {
			player.setPlayerSpawn(spawnPos);
			player.setPosAndRot(
				storedSpawnPos.x + 0.5,
				storedSpawnPos.y + 0.1,
				storedSpawnPos.z + 0.5,
				0.0F, 0.0F
			);
		}
		
		interactionManager.rotatePlayer(this.player);
		level.addPlayer(player);
		player.playerKeypressManager = new MovementManager(options);
		player.entityId = newID;
		player.method_494();
		interactionManager.method_1718(this.player);
		loadLevelWithNotify("Respawning");
		
		if (currentScreen instanceof DeathScreen) {
			openScreen(null);
		}
	}
	
	@WrapOperation(method = "createOrLoadLevel", at = @At(
		value = "NEW",
		target = "(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;J)Lnet/minecraft/level/Level;"
	))
	private Level startinthenether_createLevel(DimensionData data, String name, long seed, Operation<Level> original) {
		return data.getLevelProperties() == null ? new Level(data, name, seed, Dimension.getByID(-1)) : original.call(data, name, seed);
	}
}
