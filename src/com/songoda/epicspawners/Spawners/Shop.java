package com.songoda.epicspawners.Spawners;

import com.songoda.arconix.Arconix;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Lang;
import com.songoda.epicspawners.Utils.Debugger;
import com.songoda.epicspawners.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songoda on 3/10/2017.
 */
public class Shop {

    public void open(Player p, int page) {
        try {
            EpicSpawners.getInstance().page.put(p, page);

            List<String> entities = new ArrayList<>();

            int num = 0;
            int show = 0;
            int start = (page - 1) * 32;
            ConfigurationSection cs = EpicSpawners.getInstance().spawnerFile.getConfig().getConfigurationSection("Entities");
            for (String value : cs.getKeys(false)) {
                if (!value.toLowerCase().equals("omni")) {
                    if (EpicSpawners.getInstance().spawnerFile.getConfig().getBoolean("Entities." + Methods.getTypeFromString(value) + ".In-Shop")) {
                        if (p.hasPermission("epicspawners.*") || p.hasPermission("epicspawners.shop.*") || p.hasPermission("epicspawners.shop." + Methods.getTypeFromString(value).replaceAll(" ", "_"))) {
                            if (num >= start) {
                                if (show <= 32) {
                                    entities.add(value);
                                    show++;
                                }
                            }
                        }
                        num++;
                    }
                }
            }

            int amt = entities.size();
            Inventory i = Bukkit.createInventory(null, 54, Arconix.pl().format().formatTitle(Lang.SPAWNER_SHOP.getConfigValue()));
            int max2 = 54;
            if (amt <= 7) {
                i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle(Lang.SPAWNER_SHOP.getConfigValue()));
                max2 = 27;
            } else if (amt <= 15) {
                i = Bukkit.createInventory(null, 36, Arconix.pl().format().formatTitle(Lang.SPAWNER_SHOP.getConfigValue()));
                max2 = 36;
            } else if (amt <= 25) {
                i = Bukkit.createInventory(null, 45, Arconix.pl().format().formatTitle(Lang.SPAWNER_SHOP.getConfigValue()));
                max2 = 45;
            }

            final int max22 = max2;
            int place = 10;
            for (String value : entities) {
                if (place == 17)
                    place ++;
                if (place == (max22 - 18))
                    place ++;


                ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

                ItemStack item = EpicSpawners.getInstance().heads.addTexture(it, Methods.getTypeFromString(value));



                if (EpicSpawners.getInstance().spawnerFile.getConfig().getString("Entities." + Methods.getTypeFromString(value) + ".Display-Item") != null) {
                    item = new ItemStack(Material.getMaterial(EpicSpawners.getInstance().spawnerFile.getConfig().getString("Entities." + Methods.getTypeFromString(value) + ".Display-Item")), 1, (byte) 3);
                }

                ItemMeta itemmeta = item.getItemMeta();
                String name = Methods.compileName(value, 0, true);
                ArrayList<String> lore = new ArrayList<>();
                double price = EpicSpawners.getInstance().spawnerFile.getConfig().getDouble("Entities." + Methods.getTypeFromString(value) + ".Shop-Price");
                lore.add(Arconix.pl().format().formatText(Lang.BUY_PRICE.getConfigValue(Arconix.pl().format().formatEconomy(price))));
                String loreString = Lang.SHOP_LORE.getConfigValue(Methods.getTypeFromString(Methods.getTypeFromString(value)));
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    loreString = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(p, loreString.replace(" ", "_")).replace("_", " ");
                }
                lore.add(loreString);
                itemmeta.setLore(lore);
                itemmeta.setDisplayName(name);
                item.setItemMeta(itemmeta);
                i.setItem(place, item);
                place++;
            }

            int max = (int) Math.ceil((double) num / (double) 36);
            num = 0;
            while (num != 9) {
                i.setItem(num, Methods.getGlass());
                num++;
            }
            int num2 = max2 - 9;
            while (num2 != max2) {
                i.setItem(num2, Methods.getGlass());
                num2++;
            }

            ItemStack exit = new ItemStack(Material.valueOf(EpicSpawners.getInstance().getConfig().getString("settings.Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack skull = head;
            if (!EpicSpawners.getInstance().v1_7)
                skull = Arconix.pl().getGUI().addTexture(head, "http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b");
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            if (EpicSpawners.getInstance().v1_7)
                skullMeta.setOwner("MHF_ArrowRight");
            skull.setDurability((short) 3);
            skullMeta.setDisplayName(Lang.NEXT.getConfigValue());
            skull.setItemMeta(skullMeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack skull2 = head2;
            if (!EpicSpawners.getInstance().v1_7)
                skull2 = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) skull2.getItemMeta();
            if (EpicSpawners.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            skull2.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            skull2.setItemMeta(skull2Meta);

            i.setItem(8, exit);

            i.setItem(0, Methods.getBackgroundGlass(true));
            i.setItem(1, Methods.getBackgroundGlass(true));
            i.setItem(9, Methods.getBackgroundGlass(true));

            i.setItem(7, Methods.getBackgroundGlass(true));
            i.setItem(17, Methods.getBackgroundGlass(true));

            i.setItem(max22 - 18, Methods.getBackgroundGlass(true));
            i.setItem(max22 - 9, Methods.getBackgroundGlass(true));
            i.setItem(max22 - 8, Methods.getBackgroundGlass(true));

            i.setItem(max22 - 10, Methods.getBackgroundGlass(true));
            i.setItem(max22 - 2, Methods.getBackgroundGlass(true));
            i.setItem(max22 - 1, Methods.getBackgroundGlass(true));

            i.setItem(2, Methods.getBackgroundGlass(false));
            i.setItem(6, Methods.getBackgroundGlass(false));
            i.setItem(max22 - 7, Methods.getBackgroundGlass(false));
            i.setItem(max22 - 3, Methods.getBackgroundGlass(false));

            if (page != 1) {
                i.setItem(max22 - 8, skull2);
            }
            if (page != max) {
                i.setItem(max22 - 2, skull);
            }

            p.openInventory(i);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public void show(String type, int amt, Player p) {
        try {
            Inventory i = Bukkit.createInventory(null, 45, Arconix.pl().format().formatTitle(Lang.SPAWNER_SHOW.getConfigValue(Methods.compileName(type, 1, false))));

            int num = 0;
            while (num != 9) {
                i.setItem(num, Methods.getGlass());
                num++;
            }

            num = 36;
            while (num != 45) {
                i.setItem(num, Methods.getGlass());
                num++;
            }

            i.setItem(1, Methods.getBackgroundGlass(true));
            i.setItem(9, Methods.getBackgroundGlass(true));

            i.setItem(7, Methods.getBackgroundGlass(true));
            i.setItem(17, Methods.getBackgroundGlass(true));

            i.setItem(27, Methods.getBackgroundGlass(true));
            i.setItem(36, Methods.getBackgroundGlass(true));
            i.setItem(37, Methods.getBackgroundGlass(true));

            i.setItem(35, Methods.getBackgroundGlass(true));
            i.setItem(43, Methods.getBackgroundGlass(true));
            i.setItem(44, Methods.getBackgroundGlass(true));

            i.setItem(2, Methods.getBackgroundGlass(false));
            i.setItem(6, Methods.getBackgroundGlass(false));
            i.setItem(38, Methods.getBackgroundGlass(false));
            i.setItem(42, Methods.getBackgroundGlass(false));

            double price = EpicSpawners.getInstance().spawnerFile.getConfig().getDouble("Entities." + Methods.getTypeFromString(type) + ".Shop-Price") * amt;

            ItemStack it = new ItemStack(Material.SKULL_ITEM, amt, (byte) 3);


            ItemStack item = EpicSpawners.getInstance().heads.addTexture(it, Methods.getTypeFromString(type));

            if (EpicSpawners.getInstance().spawnerFile.getConfig().getString("Entities." + Methods.getTypeFromString(type) + ".Display-Item") != null) {
                item = new ItemStack(Material.getMaterial(EpicSpawners.getInstance().spawnerFile.getConfig().getString("Entities." + Methods.getTypeFromString(type) + ".Display-Item")), 1, (byte) 3);
            }

            item.setAmount(amt);
            ItemMeta itemmeta = item.getItemMeta();
            String name = Methods.compileName(type, 0, false);
            itemmeta.setDisplayName(name);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText(Lang.BUY_PRICE.getConfigValue(Arconix.pl().format().formatEconomy(price))));
            itemmeta.setLore(lore);
            item.setItemMeta(itemmeta);
            i.setItem(22, item);

            ItemStack plus = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta plusmeta = plus.getItemMeta();
            plusmeta.setDisplayName(Lang.ADD_1.getConfigValue());
            plus.setItemMeta(plusmeta);
            if (item.getAmount() + 1 <= 64) {
                i.setItem(15, plus);
            }

            plus = new ItemStack(Material.STAINED_GLASS_PANE, 10, (short) 5);
            plusmeta.setDisplayName(Lang.ADD_10.getConfigValue());
            plus.setItemMeta(plusmeta);
            if (item.getAmount() + 10 <= 64) {
                i.setItem(33, plus);
            }

            plus = new ItemStack(Material.STAINED_GLASS_PANE, 64, (short) 5);
            plusmeta.setDisplayName(Lang.SET_64.getConfigValue());
            plus.setItemMeta(plusmeta);
            if (item.getAmount() != 64) {
                i.setItem(25, plus);
            }

            ItemStack minus = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta minusmeta = minus.getItemMeta();
            minusmeta.setDisplayName(Lang.REM_1.getConfigValue());
            minus.setItemMeta(minusmeta);
            if (item.getAmount() != 1) {
                i.setItem(11, minus);
            }

            minus = new ItemStack(Material.STAINED_GLASS_PANE, 10, (short) 14);
            minusmeta.setDisplayName(Lang.REM_10.getConfigValue());
            minus.setItemMeta(minusmeta);
            if (item.getAmount() - 10 >= 0) {
                i.setItem(29, minus);
            }

            minus = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            minusmeta.setDisplayName(Lang.SET_1.getConfigValue());
            minus.setItemMeta(minusmeta);
            if (item.getAmount() != 1) {
                i.setItem(19, minus);
            }

            ItemStack exit = new ItemStack(Material.valueOf(EpicSpawners.getInstance().getConfig().getString("settings.Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);
            i.setItem(8, exit);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack skull2 = head2;
            if (!EpicSpawners.getInstance().v1_7)
                skull2 = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) skull2.getItemMeta();
            if (EpicSpawners.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            skull2.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            skull2.setItemMeta(skull2Meta);

            i.setItem(0, skull2);

            ItemStack buy = new ItemStack(Material.valueOf(EpicSpawners.getInstance().getConfig().getString("settings.Buy-Icon")), 1);
            ItemMeta buymeta = buy.getItemMeta();
            buymeta.setDisplayName(Lang.CONFIRM.getConfigValue());
            buy.setItemMeta(buymeta);
            i.setItem(40, buy);

            p.openInventory(i);
            EpicSpawners.getInstance().inShow.put(p, type);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public void confirm(Player p, int amt) {
        try {
            String type = EpicSpawners.getInstance().inShow.get(p);
            if (EpicSpawners.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
                p.sendMessage("Vault is not installed.");
            } else {
                RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = EpicSpawners.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                net.milkbowl.vault.economy.Economy econ = rsp.getProvider();
                double price = EpicSpawners.getInstance().spawnerFile.getConfig().getDouble("Entities." + Methods.getTypeFromString(type) + ".Shop-Price") * amt;
                if (econ.has(p, price) || p.isOp()) {
                    ItemStack item = EpicSpawners.getInstance().getApi().newSpawnerItem(type, amt);
                    p.getInventory().addItem(item);

                    p.sendMessage(EpicSpawners.getInstance().references.getPrefix() + Lang.PURCHASE_SUCCESS.getConfigValue());

                    if (!p.isOp()) {
                        econ.withdrawPlayer(p, price);
                    }
                } else {
                    p.sendMessage(EpicSpawners.getInstance().references.getPrefix() + Lang.CANNOT_AFFORD.getConfigValue());
                }
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }
}
