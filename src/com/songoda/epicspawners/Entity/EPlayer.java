package com.songoda.epicspawners.Entity;

import com.songoda.arconix.Arconix;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Lang;
import com.songoda.epicspawners.Spawners.Spawner;
import com.songoda.epicspawners.Spawners.SpawnerDropEvent;
import com.songoda.epicspawners.Spawners.SpawnerItem;
import com.songoda.epicspawners.Utils.Debugger;
import com.songoda.epicspawners.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by songoda on 2/25/2017.
 */
public class EPlayer {
    Player p;

    public EPlayer(Player p) {
        this.p = p;
    }

    public void plus(Entity entity, int amt) {
        try {
            if (EpicSpawners.getInstance().getConfig().getInt("settings.Goal") != 0 && EpicSpawners.getInstance().getConfig().getBoolean("settings.Mob-kill-counting") && p.hasPermission("epicspawners.Killcounter")) {
                String type = Methods.getType(entity.getType());
                if (EpicSpawners.getInstance().spawnerFile.getConfig().getBoolean("Entities." + type + ".Allowed")) {
                    String uuid = p.getUniqueId().toString();
                    int total = 0;
                    if (EpicSpawners.getInstance().dataFile.getConfig().getInt("data.kills." + uuid + "." + type) != 0)
                        total = EpicSpawners.getInstance().dataFile.getConfig().getInt("data.kills." + uuid + "." + type);
                    int goal = EpicSpawners.getInstance().getConfig().getInt("settings.Goal");
                    if (EpicSpawners.getInstance().spawnerFile.getConfig().getInt("Entities." + type + ".CustomGoal") != 0) {
                        goal = EpicSpawners.getInstance().spawnerFile.getConfig().getInt("Entities." + type + ".CustomGoal");
                    }
                    if (total > goal)
                        total = 1;
                    total = amt + total;

                    if (EpicSpawners.getInstance().getConfig().getInt("settings.Alert-every") != 0) {
                        if (total % EpicSpawners.getInstance().getConfig().getInt("settings.Alert-every") == 0 && total != goal) {
                            Arconix.pl().packetLibrary.getActionBarManager().sendActionBar(p, Lang.ALERT.getConfigValue(Integer.toString(goal - total), type));
                        }

                    }
                    if (total % goal == 0) {
                        dropSpawner(entity.getLocation(), 0, entity.getType().name());
                        EpicSpawners.getInstance().dataFile.getConfig().set("data.kills." + uuid + "." + type, 0);
                        Arconix.pl().packetLibrary.getActionBarManager().sendActionBar(p, Lang.DROPPED.getConfigValue(type));
                    } else
                        EpicSpawners.getInstance().dataFile.getConfig().set("data.kills." + uuid + "." + type, total);
                }
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public void dropSpawner(Location location, int multi, String type) {
        try {
            SpawnerDropEvent event = new SpawnerDropEvent(location, p);
            Bukkit.getPluginManager().callEvent(event);
            ItemStack item;

            if (!event.isCancelled()) {
                if (!type.toUpperCase().equals("OMNI")) {
                    item = EpicSpawners.getInstance().getApi().newSpawnerItem(Methods.restoreType(type), multi, 1);
                } else {
                    if (!p.isSneaking() || p.isSneaking() && !EpicSpawners.getInstance().getConfig().getBoolean("settings.Sneak-for-stack")) {
                        List<SpawnerItem> spawners = EpicSpawners.getInstance().getApi().convertFromList(EpicSpawners.getInstance().dataFile.getConfig().getStringList("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(location) + ".entities"));
                        List<ItemStack> items = EpicSpawners.getInstance().getApi().removeOmni(EpicSpawners.getInstance().getApi().newOmniSpawner(spawners));
                        item = items.get(0);
                        if (EpicSpawners.getInstance().getApi().getType(items.get(1)).equals("OMNI"))
                            EpicSpawners.getInstance().getApi().saveCustomSpawner(items.get(1), location.getBlock());
                    } else {
                        List<SpawnerItem> spawners = EpicSpawners.getInstance().getApi().convertFromList(EpicSpawners.getInstance().dataFile.getConfig().getStringList("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(location) + ".entities"));
                        item = EpicSpawners.getInstance().getApi().newOmniSpawner(spawners);
                    }
                }

                if (EpicSpawners.getInstance().getConfig().getBoolean("settings.Add-Spawner-To-Inventory-On-Drop") && p != null) {
                    if (p.getInventory().firstEmpty() == -1)
                        location.getWorld().dropItemNaturally(location, item);
                    else
                        p.getInventory().addItem(item);
                } else
                    location.getWorld().dropItemNaturally(location, item);
            }


        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public Player getP() {
        return p;
    }
}
