package com.MO.MatterOverdrive.container;

import cofh.lib.util.helpers.InventoryHelper;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkSwitch;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/1/2015.
 */
public class ContainerNetworkSwitch extends ContainerMachine<TileEntityMachineNetworkSwitch>
{
    public ContainerNetworkSwitch(InventoryPlayer inventoryPlayer,TileEntityMachineNetworkSwitch entity)
    {
        super(inventoryPlayer,entity);
    }

    @Override
    public  void init(InventoryPlayer inventory)
    {
        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    protected boolean putInPlayerInventory(ItemStack itemStack)
    {
        return InventoryHelper.mergeItemStack(inventorySlots, itemStack, machine.getInventory().getSizeInventory() - 1, (inventoryItemStacks.size() - machine.getInventory().getSizeInventory()), false, true);
    }
}
