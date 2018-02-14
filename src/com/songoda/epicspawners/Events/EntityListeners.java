package com.songoda.epicspawners.Events;

import com.songoda.arconix.Arconix;
import com.songoda.epicspawners.Entity.EPlayer;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Spawners.Spawner;
import com.songoda.epicspawners.Utils.Debugger;
import com.songoda.epicspawners.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Iterator;
import java.util.List;

/**
 * Created by songoda on 2/25/2017.
 */
public class EntityListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlow(EntityExplodeEvent e) {
        try {
            if (!e.isCancelled()) {
                List<Block> destroyed = e.blockList();
                Iterator<Block> it = destroyed.iterator();
                while (it.hasNext()) {
                    Block b = it.next();
                    if (b.getType() == Material.MOB_SPAWNER) {
                        Location spawnLocation = b.getLocation();
                        if (EpicSpawners.getInstance().getConfig().getBoolean("settings.Spawners-dont-explode"))
                            e.blockList().remove(b);
                        else if (e.getEntity() instanceof Creeper && EpicSpawners.getInstance().getConfig().getBoolean("settings.Drop-on-creeper-explosion") || e.getEntity() instanceof TNTPrimed && EpicSpawners.getInstance().getConfig().getBoolean("settings.Drop-on-tnt-explosion")) {
                            int multi = 0;

                            String locationStr = Arconix.pl().serialize().serializeLocation(b);
                            if (EpicSpawners.getInstance().dataFile.getConfig().getInt("data.spawner." + locationStr) != 0) {
                                multi = EpicSpawners.getInstance().dataFile.getConfig().getInt("data.spawner." + locationStr);
                                EpicSpawners.getInstance().dataFile.getConfig().set("data.spawner." + locationStr, null);
                            }

                            Spawner spawner = new Spawner(b);
                            String type = spawner.spawnedType;
                            String chance = "";
                            if (e.getEntity() instanceof Creeper && EpicSpawners.getInstance().getConfig().getBoolean("settings.Drop-on-creeper-explosion"))
                                chance = EpicSpawners.getInstance().getConfig().getString("settings.Tnt-explosion-drop-chance");
                            else if (e.getEntity() instanceof TNTPrimed && EpicSpawners.getInstance().getConfig().getBoolean("settings.Drop-on-tnt-explosion"))
                                chance = EpicSpawners.getInstance().getConfig().getString("settings.Creeper-explosion-drop-chance");
                            int ch = Integer.parseInt(chance.replace("%", ""));
                            double rand = Math.random() * 100;
                            if (rand - ch < 0 || ch == 100) {
                                if (EpicSpawners.getInstance().dataFile.getConfig().contains("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(b.getLocation()) + ".type")) {
                                    if (EpicSpawners.getInstance().dataFile.getConfig().getString("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(b.getLocation()) + ".type").equals("OMNI")) {
                                        type = "Omni";
                                        multi = 100;
                                    }
                                }
                                new EPlayer(null).dropSpawner(spawnLocation, multi, type);
                            }
                        }
                        EpicSpawners.getInstance().holo.processChange(b);
                        Location nloc = spawnLocation.clone();
                        nloc.add(.5, -.4, .5);
                        List<Entity> near = (List<Entity>) nloc.getWorld().getNearbyEntities(nloc, 8, 8, 8);
                        for (Entity ee : near) {
                            if (ee.getLocation().getX() == nloc.getX() && ee.getLocation().getY() == nloc.getY() && ee.getLocation().getZ() == nloc.getZ()) {
                                ee.remove();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        try {
            if (e.getEntity().getKiller() != null) {
                if (e.getEntity().getKiller() instanceof Player) {
                    Player p = e.getEntity().getKiller();
                    if (!EpicSpawners.getInstance().dataFile.getConfig().getBoolean("data.Entities." + e.getEntity().getUniqueId()) || EpicSpawners.getInstance().getConfig().getBoolean("settings.Count-unnatural-kills")) {
                        new EPlayer(p).plus(e.getEntity(), 1);
                    }
                }
            }
            EpicSpawners.getInstance().dataFile.getConfig().set("data.Entities." + e.getEntity().getUniqueId(), null);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler
    public void onDeath(CreatureSpawnEvent e) {
        try {
            if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL &&
                    e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CHUNK_GEN) {
                EpicSpawners.getInstance().dataFile.getConfig().set("data.Entities." + e.getEntity().getUniqueId(), true);
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}
