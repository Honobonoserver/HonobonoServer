package co.honobono.honobonoserver.listener;

import co.honobono.honobonoserver.constructor.RegistManager.AddListener;
import co.honobono.honobonoserver.util.Other;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syuchan on 2016/05/07.
 */

@AddListener
public class GetListener implements Listener {
	private Pattern pattern = Pattern.compile("(.*):(.*)");

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(event.getItemInHand().getType() != Material.BEDROCK) return;
		if(event.getItemInHand().getItemMeta().getDisplayName() == null) return;
		Matcher matcher = pattern.matcher(event.getItemInHand().getItemMeta().getDisplayName());
		if(!matcher.find()) return;
		if(Other.isInt(matcher.group(1)) && Other.isInt(matcher.group(2))) {
			Block place = event.getBlockPlaced();
			place.setType(Material.getMaterial(Integer.parseInt(matcher.group(1))));
			place.setData(Byte.parseByte(matcher.group(2)));
		}
	}
}
