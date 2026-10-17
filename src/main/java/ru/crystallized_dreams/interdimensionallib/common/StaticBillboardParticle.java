package ru.crystallized_dreams.interdimensionallib.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class StaticBillboardParticle extends SpriteBillboardParticle {
    StaticBillboardParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public Particle move(float speed) { return this; }
    @Override
    public void move(double dx, double dy, double dz) { }
    @Override
    public void setVelocity(double velocityX, double velocityY, double velocityZ) { }
    @Override
    public void tick() {
        if(age++>=maxAge) markDead();
    }
    @Override
    public int getMaxAge() { return 16; }
    @Override
    public float getSize(float tickDelta) { return 0.5f; }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            StaticBillboardParticle particle = new StaticBillboardParticle(clientWorld, d, e, f, g, h, i);
            particle.setSprite(spriteProvider);
            return particle;
        }
    }
}

