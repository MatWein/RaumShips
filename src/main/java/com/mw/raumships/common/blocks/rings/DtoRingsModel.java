package com.mw.raumships.common.blocks.rings;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class DtoRingsModel {
    private int address;

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    private String name;

    public String getName() {
        if (name == null)
            return "[empty]";

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private BlockPos pos;

    public BlockPos getPos() {
        return pos;
    }

    private double distance;

    public double getDistance() {
        return distance;
    }

    private boolean isClone;

    public DtoRingsModel(BlockPos pos) {
        this(-1, null, pos, false);
    }

    public DtoRingsModel(int address, String name) {
        this(address, name, new BlockPos(0, 0, 0), false);
    }

    private DtoRingsModel(int address, String name, BlockPos pos, boolean isClone) {
        this.address = address;
        this.name = name;
        this.pos = pos;

        this.isClone = isClone;
    }

    public DtoRingsModel cloneWithNewDistance(BlockPos callerPos) {
        return new DtoRingsModel(address, name, pos, true).setDistanceTo(callerPos);
    }

    public boolean isInGrid() {
        return address != -1;
    }

    private DtoRingsModel setDistanceTo(BlockPos pos) {
        distance = this.pos.getDistance(pos.getX(), pos.getY(), pos.getZ());

        return this;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        if (address != -1)
            compound.setInteger("address", address);

        if (name != null)
            compound.setString("name", name);

        compound.setLong("pos", pos.toLong());

        if (isClone)
            compound.setDouble("distance", distance);

        return compound;
    }

    public DtoRingsModel deserializeNBT(NBTTagCompound compound) {
        if (compound.hasKey("address"))
            address = compound.getInteger("address");
        else
            address = -1;

        if (compound.hasKey("name"))
            name = compound.getString("name");

        pos = BlockPos.fromLong(compound.getLong("pos"));

        if (compound.hasKey("distance")) {
            isClone = true;

            distance = compound.getDouble("distance");
        }

        return this;
    }

    @Override
    public String toString() {
        return "[pos=" + pos.toString() + ", address=" + address + ", name=" + name + "]";
    }
}
