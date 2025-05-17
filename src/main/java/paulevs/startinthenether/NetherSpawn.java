package paulevs.startinthenether;

import net.minecraft.block.material.Material;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;

import java.util.Random;

public class NetherSpawn {
	public static void spawnPlayer(PlayerEntity player) {
		Level level = player.level;
		Random random = level.random;
		int startX = (int) player.x;
		int startZ = (int) player.z;
		int count = level.getTopY() - level.getBottomY();
		int middleY = (level.getTopY() + level.getBottomY()) >> 1;
		
		for (short i = 0; i < 256; i++) {
			player.x = startX + random.nextInt(128) - 63.5;
			player.z = startZ + random.nextInt(128) - 63.5;
			for (int dy = 2; dy < count; dy++) {
				player.y = middleY + (dy >> 1) * ((dy & 1) == 0 ? -1 : 1);
				if (player.isInFluid(Material.LAVA)) continue;
				if (level.getCollidingEntities(player, player.boundingBox).isEmpty()) return;
			}
		}
	}
}
