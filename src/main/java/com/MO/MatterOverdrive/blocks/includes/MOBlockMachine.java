package com.MO.MatterOverdrive.blocks.includes;

import cofh.api.block.IDismantleable;
import cofh.lib.util.helpers.InventoryHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.data.inventory.Slot;
import com.MO.MatterOverdrive.data.inventory.UpgradeSlot;
import com.MO.MatterOverdrive.handler.GuiHandler;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.items.includes.MOEnergyMatterBlockItem;
import com.MO.MatterOverdrive.tile.IMOTileEntity;
import com.MO.MatterOverdrive.tile.MOTileEntityMachine;
import com.MO.MatterOverdrive.util.MatterHelper;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Simeon on 4/5/2015.
 */
public class MOBlockMachine extends MOBlockContainer implements IDismantleable
{
    public float volume;
    public boolean hasGui;

    public MOBlockMachine(Material material, String name)
    {
        super(material, name);
    }

    @Override
    protected void RegisterBlock()
    {
        GameRegistry.registerBlock(this, MOEnergyMatterBlockItem.class, this.getUnlocalizedName().substring(5));
    }

    public boolean doNormalDrops(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return null;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        try
        {
            IMOTileEntity entity = (IMOTileEntity)world.getTileEntity(x,y,z);
            if(entity != null)
            {
                entity.readFromPlaceItem(itemStack);
            }
        }
        catch (Exception e)
        {
            FMLLog.warning("Could not load settings from placing item");
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        super.breakBlock(world,x,y,z,block,meta);

        //drops inventory
        MOTileEntityMachine machine = (MOTileEntityMachine) world.getTileEntity(x, y, z);
        if (machine != null)
            MatterHelper.DropInventory(world, (MOTileEntityMachine) world.getTileEntity(x, y, z), x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        if(!world.isRemote && hasGui)
        {
            FMLNetworkHandler.openGui(player, MatterOverdrive.instance, -1, world, x, y, z);
        }

        return true;
    }

    public ItemStack getNBTDrop(World world, int x, int y, int z, IMOTileEntity te)
    {
        int meta = damageDropped(world.getBlockMetadata(x,y,z));
        ItemStack itemStack = new ItemStack(this, 1, meta);
        if(te != null)
            te.writeToDropItem(itemStack);
        return itemStack;
    }

    public void loadConfigs(MOConfigurationHandler configurationHandler)
    {
        volume = configurationHandler.getMachineFloat(getUnlocalizedName() + ".volume",1,0,2,"The volume of the Machine");
    }

    public boolean isHasGui() {
        return hasGui;
    }

    public void setHasGui(boolean hasGui) {
        this.hasGui = hasGui;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops)
    {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack blockItem = getNBTDrop(world, x, y, z, (IMOTileEntity) world.getTileEntity(x, y, z));
        MOTileEntityMachine machine = (MOTileEntityMachine)world.getTileEntity(x,y,z);
        items.add(blockItem);

        Block block = world.getBlock(x, y, z);
        int l = world.getBlockMetadata(x, y, z);
        boolean flag = block.removedByPlayer(world, player, x, y, z, true);
        super.breakBlock(world,x,y,z,block,l);

        if (flag)
        {
            block.onBlockDestroyedByPlayer(world, x, y, z, l);
        }

        if (!returnDrops)
        {
            for (int i1 = 0; i1 < machine.getInventory().getSizeInventory(); ++i1)
            {
                Slot slot = machine.getInventory().getSlot(i1);
                ItemStack itemstack = slot.getItem();

                if (itemstack != null && !(slot instanceof UpgradeSlot))
                {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }

            dropBlockAsItem(world, x, y, z, blockItem);
        }
        else
        {
            InventoryHelper.insertItemStackIntoInventory(player.inventory, blockItem, 0);
        }

        return items;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }
}
