package com.MO.MatterOverdrive.network.packet.server;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.api.network.IMatterNetworkDispatcher;
import com.MO.MatterOverdrive.network.packet.TileEntityUpdatePacket;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 4/28/2015.
 */
public class PacketRemoveTask extends TileEntityUpdatePacket
{
    int taskIndex;
    byte queueID;
    byte task_state;

    public PacketRemoveTask(){super();}
    public PacketRemoveTask(TileEntity dispatcher,int taskIndex,byte queueID,byte task_state)
    {
        super(dispatcher);
        this.taskIndex = taskIndex;
        this.queueID = queueID;
        this.task_state = task_state;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        taskIndex = buf.readInt();
        queueID = buf.readByte();
        task_state = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(taskIndex);
        buf.writeByte(queueID);
        buf.writeByte(task_state);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketRemoveTask> {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketRemoveTask message, MessageContext ctx)
        {
            TileEntity entity = message.getTileEntity(player.worldObj);

            if (entity instanceof IMatterNetworkConnectionProxy)
            {
                if (((IMatterNetworkConnectionProxy) entity).getMatterNetworkConnection() instanceof IMatterNetworkDispatcher)
                {
                    IMatterNetworkDispatcher dispatcher = (IMatterNetworkDispatcher)((IMatterNetworkConnectionProxy) entity).getMatterNetworkConnection();
                    dispatcher.getQueue(message.queueID).dropAt(message.taskIndex).setState(message.task_state);
                    player.worldObj.markBlockForUpdate(entity.xCoord, entity.yCoord, entity.zCoord);
                    entity.markDirty();
                }
            }
            return null;
        }
    }
}
