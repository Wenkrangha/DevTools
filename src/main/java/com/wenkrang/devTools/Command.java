package com.wenkrang.devTools;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        try {
            if (strings[0].equalsIgnoreCase("setitem")) {
                DevTools.itemStack = player.getInventory().getItemInMainHand();
            }
            if (strings[0].equalsIgnoreCase("setname")) {
                ItemStack itemStack = DevTools.itemStack;
                ItemMeta itemMeta = itemStack.getItemMeta();
                String replace = strings[1].replace("#", "§").replace("none", " ");

                player.sendMessage(replace);
                itemMeta.setDisplayName(replace);
                itemStack.setItemMeta(itemMeta);
                if (strings[1].equalsIgnoreCase("none")) itemMeta.setItemName("");
                DevTools.itemStack = itemStack;
            }
            if (strings[0].equalsIgnoreCase("setlore")) {
                ItemStack itemStack = DevTools.itemStack;
                ItemMeta itemMeta = itemStack.getItemMeta();
                ArrayList<String> arrayList = new ArrayList<>();
                String string = strings[1].replace("#", "§").replace("none", " ");
                String[] split = string.split("\\^");
                itemMeta.setLore(List.of(split));
                for (String string1 : split) {
                    player.sendMessage(string1);
                }
                itemStack.setItemMeta(itemMeta);
            }
            if (strings[0].equalsIgnoreCase("builditem")){
                String build = "";
                build = build + "ItemStack itemStack = new ItemStack(Material." + DevTools.itemStack.getType().toString() + ");\n";
                build = build + "ItemMeta itemMeta = itemStack.getItemMeta();\n";
                build = build + "itemMeta.setDisplayName(" + DevTools.itemStack.getItemMeta().getDisplayName() + ");\n";
                List<String> lore = DevTools.itemStack.getItemMeta().getLore();
                if (lore != null) {
                    build = build + "ArrayList<String> lore = new ArrayList<>();\n";

                    for (String string : lore) {
                        build = build + "lore.add(\"" + string + "\");\n";
                    }
                    build = build + "itemMeta.setLore(lore);\n";
                }

                build = build + "itemStack.setItemMeta(itemMeta);";
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./item.txt"))) {
                    bufferedWriter.write(build);
                    bufferedWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (strings[0].equalsIgnoreCase("get")) {
                player.getInventory().addItem(DevTools.itemStack);
            }
            if (strings[0].equalsIgnoreCase("setinv")) {

                DevTools.inventory = Bukkit.createInventory(null, Integer.valueOf(strings[1]), "DevTools");
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                yamlConfiguration.set("size", Integer.valueOf(strings[1]));
                try {
                    yamlConfiguration.save("./inv.yaml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (strings[0].equalsIgnoreCase("inv")) {
                player.openInventory(DevTools.inventory);
            }
            if (strings[0].equalsIgnoreCase("save")) {
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                try {
                    yamlConfiguration.load("./inv.yaml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0;i < yamlConfiguration.getInt("size");i++) {
                    yamlConfiguration.set("inv-" + Integer.valueOf(i), DevTools.inventory.getItem(i));
                }
                try {
                    yamlConfiguration.save("./inv.yaml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (strings[0].equalsIgnoreCase("load")) {
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                try {
                    yamlConfiguration.load("./inv.yaml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
                DevTools.inventory = Bukkit.createInventory(null, yamlConfiguration.getInt("size"), "DevTools");
                for (int i = 0;i < yamlConfiguration.getInt("size");i++) {
                    DevTools.inventory.setItem(i, yamlConfiguration.getItemStack("inv-" + Integer.valueOf(i)));
                }
            }
            if (strings[0].equalsIgnoreCase("buildinv")) {
                ArrayList<ItemStack> itemStacks = new ArrayList<>();
                for (int i = 0;i < DevTools.inventory.getSize();i++) {
                    if (!itemStacks.contains(DevTools.inventory.getItem(i))) {
                        itemStacks.add(DevTools.inventory.getItem(i));
                    }
                }
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./inv.txt"))) {
                    bufferedWriter.write("Inventory inventory = Bukkit.createInventory(null, "+Integer.valueOf(DevTools.inventory.getSize())+", \""+ strings[1] +"\");\n");
                    String build = "";
                    for (int i = 0;i < itemStacks.size();i++) {
                        build = build + "ItemStack itemStack"+ Integer.valueOf(i) +" = new ItemStack(Material." + itemStacks.get(i).getType().toString() + ");\n";
                        build = build + "ItemMeta itemMeta"+ Integer.valueOf(i) +" = itemStack"+ Integer.valueOf(i) +".getItemMeta();\n";
                        build = build + "itemMeta"+ Integer.valueOf(i) +".setDisplayName(" + itemStacks.get(i).getItemMeta().getDisplayName() + ");\n";
                        List<String> lore = itemStacks.get(i).getItemMeta().getLore();
                        if (lore != null) {
                            build = build + "ArrayList<String> lore"+ Integer.valueOf(i) +" = new ArrayList<>();\n";

                            for (String string : lore) {
                                build = build + "lore"+ Integer.valueOf(i) +".add(\"" + string + "\");\n";
                            }
                            build = build + "itemMeta"+ Integer.valueOf(i) +".setLore(lore"+ Integer.valueOf(i) +");\n";
                        }
                        build = build + "itemStack"+ Integer.valueOf(i) +".setItemMeta(itemMeta"+ Integer.valueOf(i) +");\n";
                        bufferedWriter.write(build);
                    }
                    for (int i = 0;i < DevTools.inventory.getSize();i++) {
                        if (DevTools.inventory.getItem(i) != null) {
                            bufferedWriter.write("inventory.setItem("+Integer.valueOf(i)+", itemStack"+Integer.valueOf(itemStacks.indexOf(DevTools.inventory.getItem(i)))+");");
                        }else{
                            bufferedWriter.write("inventory.setItem("+Integer.valueOf(i)+", null");
                        }
                        bufferedWriter.close();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
