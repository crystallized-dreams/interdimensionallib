package ru.alexalabai.interdimensionallib.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class SeatEntity extends Entity {
    public SeatEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if(getWorld().isClient) return;
        if(!hasPassengers() || Objects.requireNonNull(getFirstPassenger()).doesNotCollide(0,0.1f,0)) discard();
    }
    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        return passenger.getPos().add(0,0.6f,0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) { }
    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) { }
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) { }
}
