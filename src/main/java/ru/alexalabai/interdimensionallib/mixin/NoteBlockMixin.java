package ru.alexalabai.interdimensionallib.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.alexalabai.interdimensionallib.common.NoteBlockInstrumentRegistry;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
    @Inject(method = "onSyncedBlockEvent", at = @At("HEAD"), cancellable = true)
    void onSyncedBlockEvent$interdim(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> info) {
        BlockState instrumentState=world.getBlockState(pos.down());
        NoteBlockInstrumentRegistry.InstrumentSoundEntry instrument=NoteBlockInstrumentRegistry.get(instrumentState.getBlock());
        if(instrument==null) {
            instrumentState=world.getBlockState(pos.up());
            instrument=NoteBlockInstrumentRegistry.get(instrumentState.getBlock());
        }
        if(instrument==null) return;

        int note =state.get(NoteBlock.NOTE);
        float pitch=NoteBlock.getNotePitch(note);
        if(!world.isClient&&instrument.pitched()) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 1, 0.0, 0.0, 0.0, note / 24.0);
        } else if(!instrument.pitched()) pitch=1;
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, instrument.sound(), SoundCategory.RECORDS, instrument.volume(), pitch, world.random.nextLong());
        info.setReturnValue(true);
    }
}
