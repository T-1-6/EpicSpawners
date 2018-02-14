package com.songoda.epicspawners.Events;

import com.songoda.arconix.Arconix;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Utils.Debugger;
import org.bukkit.Rotation;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by songoda on 3/13/2017.
 */
public class TestListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEntityInteract(PlayerInteractAtEntityEvent e) {
        try {
            if (e.getRightClicked() instanceof ItemFrame) {
                if (EpicSpawners.getInstance().dataFile.getConfig().getString("data.entityshop") != null) {
                    String uuid = e.getRightClicked().getUniqueId().toString();
                    if (EpicSpawners.getInstance().dataFile.getConfig().getString("data.entityshop." + uuid) != null) {
                        ((ItemFrame) e.getRightClicked()).setRotation(Rotation.CLOCKWISE_45);
                        EpicSpawners.getInstance().shop.show(EpicSpawners.getInstance().dataFile.getConfig().getString("data.entityshop." + uuid).toUpperCase(), 1, e.getPlayer());
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.isOp() && EpicSpawners.getInstance().getConfig().getBoolean("settings.Helpful-Tips")) {
            if (EpicSpawners.getInstance().getServer().getPluginManager().getPlugin("Factions") != null && EpicSpawners.getInstance().hooks.FactionsHook == null) {
                p.sendMessage("");
                p.sendMessage(Arconix.pl().format().formatText(EpicSpawners.getInstance().references.getPrefix() + "&7Heres the deal,"));
                p.sendMessage(Arconix.pl().format().formatText("&7Because you're not using the offical versions of &6Factions"));
                p.sendMessage(Arconix.pl().format().formatText("&7I cannot give you full support out of the box."));
                p.sendMessage(Arconix.pl().format().formatText("&7Things will work without it but if you wan't a flawless"));
                p.sendMessage(Arconix.pl().format().formatText("&7experience you need to download"));
                p.sendMessage(Arconix.pl().format().formatText("&7&6https://www.spigotmc.org/resources/22278/&7."));
                p.sendMessage(Arconix.pl().format().formatText("&7If you don't care and don't want to see this message again"));
                p.sendMessage(Arconix.pl().format().formatText("&7turn &6Helpful-Tips &7off in the config."));
                p.sendMessage("");
            }
        }
    }
}
