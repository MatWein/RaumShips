package com.mw.raumships.client.gui.rings;

import com.mw.raumships.common.blocks.rings.DtoRingsModel;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RingsGuiState extends State {
    private boolean inGrid;
    private int address;
    private String name;
    private List<DtoRingsModel> ringsList = new ArrayList<>();

    public RingsGuiState() {
    }

    public RingsGuiState(DtoRingsModel rings, Collection<DtoRingsModel> ringsList) {
        inGrid = rings.isInGrid();

        if (inGrid) {
            this.address = rings.getAddress();
            this.name = rings.getName();

            this.ringsList.addAll(ringsList);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(inGrid);

        if (inGrid) {
            buf.writeInt(address);
            buf.writeInt(name.length());
            buf.writeCharSequence(name, StandardCharsets.UTF_8);

            buf.writeInt(ringsList.size());

            for (DtoRingsModel rings : ringsList) {
                buf.writeInt(rings.getAddress());
                buf.writeInt(rings.getName().length());
                buf.writeCharSequence(rings.getName(), StandardCharsets.UTF_8);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        inGrid = buf.readBoolean();

        if (inGrid) {
            address = buf.readInt();
            int length = buf.readInt();
            name = buf.readCharSequence(length, StandardCharsets.UTF_8).toString();

            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                int address = buf.readInt();

                length = buf.readInt();
                String name = buf.readCharSequence(length, StandardCharsets.UTF_8).toString();

                ringsList.add(new DtoRingsModel(address, name));
            }
        }
    }

    public boolean isInGrid() {
        return inGrid;
    }

    public int getAddress() {
        return address;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public List<DtoRingsModel> getRings() {
        return ringsList;
    }
}
