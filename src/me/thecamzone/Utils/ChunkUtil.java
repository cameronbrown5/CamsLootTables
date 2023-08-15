package me.thecamzone.Utils;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class ChunkUtil {

	public static ArrayList<Sign> getChunkSigns(Chunk chunk) {
		ArrayList<Sign> signs = new ArrayList<>();
		
		for(int y = 0; y < chunk.getWorld().getMaxHeight(); y++) {
			for(int x = 0; x < 15; x++) {
				for(int z = 0; z < 15; z++) {
					Block block = chunk.getBlock(x, y, z);
					
					BlockState state = block.getState();
					if(state instanceof Sign) {
						signs.add((Sign) state);
					}
				}
			}
		}
		
		return signs;
	}
	
}
