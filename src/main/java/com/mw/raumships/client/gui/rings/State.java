package com.mw.raumships.client.gui.rings;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class State implements INBTSerializable<NBTTagCompound> {
    public abstract void toBytes(ByteBuf buf);

    public abstract void fromBytes(ByteBuf buf);

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        ByteBuf buf = Unpooled.buffer();
        toBytes(buf);

        byte[] dst = new byte[buf.readableBytes()];
        buf.readBytes(dst);

        compound.setByteArray("byteArray", dst);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        byte[] dst = compound.getByteArray("byteArray");

        if (dst != null && dst.length > 0) {
            ByteBuf buf = Unpooled.copiedBuffer(dst);
            fromBytes(buf);
        }
    }
}
