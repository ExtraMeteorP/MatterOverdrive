package com.MO.MatterOverdrive.container;

import com.MO.MatterOverdrive.container.slot.SlotEnergy;
import com.MO.MatterOverdrive.tile.TileEntityMachineTransporter;
import com.MO.MatterOverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 5/3/2015.
 */
public class ContainerTransporter extends ContainerMachine<TileEntityMachineTransporter>
{
    public ContainerTransporter(InventoryPlayer inventory, TileEntityMachineTransporter machine)
    {
        super(inventory, machine);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        addSlotToContainer(new SlotEnergy(machine.getInventory(),machine.getEnergySlotID(),8,55));
        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }
}
