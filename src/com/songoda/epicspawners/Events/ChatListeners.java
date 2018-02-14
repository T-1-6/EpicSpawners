package com.songoda.epicspawners.Events;

import com.songoda.arconix.Arconix;
import com.songoda.epicspawners.Entity.EPlayer;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Lang;
import com.songoda.epicspawners.Spawners.Spawner;
import com.songoda.epicspawners.Utils.Debugger;
import com.songoda.epicspawners.Utils.Methods;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;

/**
 * Created by songoda on 2/25/2017.
 */
public class ChatListeners implements Listener {

    @EventHandler
    public void chatListeners(AsyncPlayerChatEvent e) {
        try {
            if (!e.isCancelled()) {
                if (EpicSpawners.getInstance().chatEditing.containsKey(e.getPlayer())) {
                    if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("destroy")) {
                        EpicSpawners.getInstance().editor.destroyFinal(e.getPlayer(), e.getMessage());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("name")) {
                        EpicSpawners.getInstance().editor.saveSpawnerName(e.getPlayer(), e.getMessage());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("tick")) {
                        EpicSpawners.getInstance().editor.saveChatEdit(e.getPlayer(), Integer.parseInt(e.getMessage()));
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("addEntity")) {
                        EpicSpawners.getInstance().isEntityInstanceSaved = true;
                        EpicSpawners.getInstance().editor.addEntity(e.getPlayer(), e.getMessage());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("Shop-Price")) {
                        if (Arconix.pl().doMath().isNumeric(e.getMessage())) {
                            EpicSpawners.getInstance().spawnerFile.getConfig().set("Entities." + Methods.getTypeFromString(EpicSpawners.getInstance().editor.getType(EpicSpawners.getInstance().editing.get(e.getPlayer()))) + ".Shop-Price", Double.parseDouble(e.getMessage()));
                        } else {
                            e.getPlayer().sendMessage(Arconix.pl().format().formatText("&CYou must enter a number."));
                        }
                        EpicSpawners.getInstance().editor.basicSettings(e.getPlayer());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("Custom-ECO-Cost")) {
                        if (Arconix.pl().doMath().isNumeric(e.getMessage())) {
                            EpicSpawners.getInstance().spawnerFile.getConfig().set("Entities." + Methods.getTypeFromString(EpicSpawners.getInstance().editor.getType(EpicSpawners.getInstance().editing.get(e.getPlayer()))) + ".Custom-ECO-Cost", Double.parseDouble(e.getMessage()));
                        } else {
                            e.getPlayer().sendMessage(Arconix.pl().format().formatText("&CYou must enter a number."));
                        }
                        EpicSpawners.getInstance().editor.basicSettings(e.getPlayer());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("Custom-XP-Cost")) {
                        if (Arconix.pl().doMath().isNumeric(e.getMessage())) {
                            EpicSpawners.getInstance().spawnerFile.getConfig().set("Entities." + Methods.getTypeFromString(EpicSpawners.getInstance().editor.getType(EpicSpawners.getInstance().editing.get(e.getPlayer()))) + ".Custom-XP-Cost", Integer.parseInt(e.getMessage()));
                        } else {
                            e.getPlayer().sendMessage(Arconix.pl().format().formatText("&CYou must enter a number."));
                        }
                        EpicSpawners.getInstance().editor.basicSettings(e.getPlayer());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("Command")) {
                        String msg = e.getMessage();
                        e.getPlayer().sendMessage(Arconix.pl().format().formatText(EpicSpawners.getInstance().references.getPrefix() + "&8Command &5" + msg + "&8 saved to your inventory."));
                        EpicSpawners.getInstance().editor.addCommand(e.getPlayer(), e.getMessage());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("CustomGoal")) {
                        if (Arconix.pl().doMath().isNumeric(e.getMessage())) {
                            EpicSpawners.getInstance().spawnerFile.getConfig().set("Entities." + Methods.getTypeFromString(EpicSpawners.getInstance().editor.getType(EpicSpawners.getInstance().editing.get(e.getPlayer()))) + ".CustomGoal", Integer.parseInt(e.getMessage()));
                        } else {
                            e.getPlayer().sendMessage(Arconix.pl().format().formatText("&CYou must enter a number."));
                        }
                        EpicSpawners.getInstance().editor.basicSettings(e.getPlayer());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("Pickup-cost")) {
                        if (Arconix.pl().doMath().isNumeric(e.getMessage())) {
                            EpicSpawners.getInstance().spawnerFile.getConfig().set("Entities." + Methods.getTypeFromString(EpicSpawners.getInstance().editor.getType(EpicSpawners.getInstance().editing.get(e.getPlayer()))) + ".Pickup-cost", Double.parseDouble(e.getMessage()));
                        } else {
                            e.getPlayer().sendMessage(Arconix.pl().format().formatText("&CYou must enter a number."));
                        }
                        EpicSpawners.getInstance().editor.basicSettings(e.getPlayer());
                    } else if (EpicSpawners.getInstance().chatEditing.get(e.getPlayer()).equals("spawnLimit")) {
                        if (Arconix.pl().doMath().isNumeric(e.getMessage())) {
                            EpicSpawners.getInstance().spawnerFile.getConfig().set("Entities." + Methods.getTypeFromString(EpicSpawners.getInstance().editor.getType(EpicSpawners.getInstance().editing.get(e.getPlayer()))) + ".commandSpawnLimit", Double.parseDouble(e.getMessage()));
                            EpicSpawners.getInstance().editor.editor(e.getPlayer(), "Command");
                        } else {
                            e.getPlayer().sendMessage(Arconix.pl().format().formatText("&CYou must enter a number."));
                        }
                        EpicSpawners.getInstance().editor.basicSettings(e.getPlayer());
                    }
                    EpicSpawners.getInstance().chatEditing.remove(e.getPlayer());
                    e.setCancelled(true);
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }
}
