package me.thecamzone.Listeners;

import java.util.ArrayList;
import org.bukkit.Chunk;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import me.thecamzone.Utils.ChunkUtil;
import me.thecamzone.Utils.SignUtils;

public class OnChunkGeneration implements Listener {
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		Chunk chunk = e.getChunk();
		
		if(!e.isNewChunk()) {
			return;
		}
		
		ArrayList<Sign> signs = ChunkUtil.getChunkSigns(chunk);
		
		for(Sign sign : signs) {
			SignUtils.replaceSign(sign);
		}
	}
	
	
	
}
