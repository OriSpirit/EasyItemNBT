package com.spiritlight.easyitemnbt;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class cItem {
    private final Set<Integer> itemSet = new CopyOnWriteArraySet<>();

    @SubscribeEvent
    public void writeContainerNBT(GuiScreenEvent.DrawScreenEvent.Post event) {
        if(!(event.getGui() instanceof GuiContainer)) return;
        Container container = ((GuiContainer) event.getGui()).inventorySlots;
        for (Slot slot : container.inventorySlots) {
            ItemStack stx = slot.getStack();
            if (itemSet.contains(stx.hashCode())) continue;
            final NBTTagCompound tag = stx.serializeNBT().getCompoundTag("tag");
            final NBTTagCompound ret = stx.serializeNBT();
            if (tag.hasNoTags()) continue;
            if (tag.hasKey("EINParsed")) continue;
            List<String> itemLore = new ArrayList<>();
            NBTTagCompound display = tag.getCompoundTag("display");
            if (display.hasKey("Lore", 9)) {
                NBTTagList list = display.getTagList("Lore", 8);
                for (int i = 0; i < list.tagCount(); i++) {
                    itemLore.add(list.getStringTagAt(i));
                }
            }
            itemLore.add("");
            itemLore.addAll(StringUtils.cutString(tag.toString(), 56, new char[]{',', '}', ' '}));
            NBTTagList tagList = new NBTTagList();
            tagList.appendTag(new NBTTagString(TextFormatting.RESET + "Item NBT: "));
            itemLore.add("");
            List<String> coloredLore = new ArrayList<>();
            for (String s : itemLore) {
                coloredLore.add(format(s));
            }
            for (String s : coloredLore) {
                tagList.appendTag(new NBTTagString(s));
            }
            if (ret.hasKey("tag")) {
                if (ret.hasKey("display")) {
                    ret.getCompoundTag("tag").getCompoundTag("display").setTag("Lore", tagList);
                } else {
                    ret.getCompoundTag("tag").setTag("display", new NBTTagCompound());
                    ret.getCompoundTag("tag").getCompoundTag("display").setTag("Lore", tagList);
                }
            } else {
                ret.setTag("tag", new NBTTagCompound());
                ret.getCompoundTag("tag").setTag("display", new NBTTagCompound());
                ret.getCompoundTag("tag").getCompoundTag("display").setTag("Lore", tagList);
            }
            ret.getCompoundTag("tag").setTag("EINParsed", new NBTTagInt(1));
            stx.deserializeNBT(ret);
            itemSet.add(stx.hashCode());
        }
    }

    private String format(String s) {
        return s
                .replace("{", TextFormatting.AQUA + "{" + TextFormatting.GOLD)
                .replace("}", TextFormatting.AQUA + "}" + TextFormatting.GOLD)
                .replace("[", TextFormatting.GREEN + "[" + TextFormatting.GOLD)
                .replace("]", TextFormatting.GREEN + "]" + TextFormatting.GOLD)
                .replace(",", TextFormatting.RESET + "," + TextFormatting.GOLD)
                .replace(":", TextFormatting.RESET + ":" + TextFormatting.AQUA)
                .replace("'", TextFormatting.YELLOW + "'" + TextFormatting.RESET)
                .replace("\"", TextFormatting.GREEN + "\"" + TextFormatting.GOLD);
    }
}
