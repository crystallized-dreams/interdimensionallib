package ru.crystallized_dreams.interdimensionallib.content.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import ru.crystallized_dreams.interdimensionallib.content.INTERDIM_BlockEntities;
import ru.crystallized_dreams.interdimensionallib.content.block.PlushieBlock;
import ru.crystallized_dreams.interdimensionallib.content.component.PlushieData;

public class PlushieBlockEntity extends BlockEntity {
    private PlushieData data = PlushieData.DEFAULT;
    public PlushieBlockEntity(BlockPos pos, BlockState state) {
        super(INTERDIM_BlockEntities.PLUSHIE, pos, state);
    }

    public PlushieData getData() { return data; }
    public void setData(PlushieData data) {
        this.data = data;
        markDirty();
        if (world!=null && !world.isClient) {
            world.setBlockState(pos, getCachedState().with(PlushieBlock.WIDE, data.wide()), Block.NOTIFY_ALL);
            world.updateListeners(pos, world.getBlockState(getPos()), world.getBlockState(getPos()), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putString("Player", data.nickname());
        nbt.putBoolean("Wide", data.wide());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        String name = nbt.contains("Player")?nbt.getString("Player"):"Steve";
        boolean wide = !nbt.contains("Wide")||nbt.getBoolean("Wide");
        data = new PlushieData(name, wide);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt, registryLookup);
        return nbt;
    }
}
