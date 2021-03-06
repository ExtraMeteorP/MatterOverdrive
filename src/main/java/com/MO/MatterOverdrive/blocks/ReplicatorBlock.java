package com.MO.MatterOverdrive.blocks;

import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveIcons;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.MO.MatterOverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import com.MO.MatterOverdrive.tile.TileEntityMachineReplicator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ReplicatorBlock extends MOMatterEnergyStorageBlock
{
	public float replication_volume;
    public boolean hasVentParticles;
	
	public ReplicatorBlock(Material material,String name)
	{
		super(material, name, true, true);
		setHardness(2.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
	}

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        return MatterOverdriveIcons.Base;
    }
	 
	 @Override
	 public boolean isOpaqueCube()
	    {
	        return false;
	    }
	 
	 @Override
	 public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	 {
		 return true;
	 }
	 
	 public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	 {
		 return true;
	 }
	 
	 @Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityMachineReplicator();
	}


    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return -1;
    }

	@Override
	public void loadConfigs(MOConfigurationHandler configurationHandler)
	{
		super.loadConfigs(configurationHandler);
        replication_volume = configurationHandler.getMachineFloat(getUnlocalizedName() + ".volume.replicate",1f,0,2f,"The volume of the replication animation");
        hasVentParticles = configurationHandler.getMachineBool(getUnlocalizedName() + ".particles", true, "Sould vent particles be displayed");
	}
}
