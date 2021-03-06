package com.MO.MatterOverdrive.container.slot;

import com.MO.MatterOverdrive.util.MatterHelper;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMatter extends MOSlot
{
	public SlotMatter(IInventory inventory, int index, int x, int y) {

		super(inventory, index, x, y);
	}

	@Override
	public boolean isValid(ItemStack stack) {

		return MatterHelper.containsMatter(stack);
	}
}
