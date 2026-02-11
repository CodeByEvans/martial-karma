package com.codebyevans.martialkarma.entity;

import com.codebyevans.martialkarma.ability.HeartMarkTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.entity.damage.DamageSource;

public class HeartMarkEntity extends ProjectileEntity {

    private int abilityLevel = 1;
    private LivingEntity markedTarget;
    private long markTime; // tiempo en ms en que se aplicó la marca
    private boolean exploded = false;

    public HeartMarkEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public HeartMarkEntity(World world, LivingEntity owner, int level) {
        super(ModEntities.HEART_MARK, world);
        this.setOwner(owner);
        this.abilityLevel = level;
        this.setPosition(owner.getEyePos());
        this.setVelocity(owner, owner.getPitch(), owner.getYaw(), 0.0F, 2.0F, 1.0F);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        // No necesitamos datos extra
    }

    @Override
    public void tick() {
        super.tick();

        // Partículas rojas siguiendo el proyectil
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.HEART,
                    this.getX(), this.getY(), this.getZ(),
                    1,
                    0.05, 0.05, 0.05,
                    0.01
            );
        }

        // Descartar después de 10 segundos si no impacta
        if (this.age > 200 && !exploded) {
            this.discard();
        }

        // Manejo de la marca y explosión
        if (markedTarget != null && !exploded) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - markTime >= 3000) { // 3 segundos después
                explodeMark();
                exploded = true;
                this.discard();
            }
        }

        // Detección de colisión
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) hitResult);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        if (target instanceof LivingEntity livingTarget && owner instanceof LivingEntity livingOwner) {
            // Aplicar la marca
            HeartMarkTracker.applyHeartMark(livingOwner, livingTarget, abilityLevel);

            // Guardamos el objetivo marcado y el tiempo de impacto
            this.markedTarget = livingTarget;
            this.markTime = System.currentTimeMillis();

            // Partículas de impacto
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                        ParticleTypes.HEART,
                        target.getX(), target.getY() + 1, target.getZ(),
                        15,
                        0.3, 0.5, 0.3,
                        0.1
                );

                serverWorld.spawnParticles(
                        ParticleTypes.CRIT,
                        target.getX(), target.getY() + 1, target.getZ(),
                        10,
                        0.3, 0.5, 0.3,
                        0.15
                );
            }

            // Sonido de impacto
            target.playSound(SoundEvents.BLOCK_BELL_USE, 1.0f, 2.0f);
        }
    }

    private void explodeMark() {
        if (markedTarget == null || !markedTarget.isAlive()) return;

        // Calcular daño según nivel
        float damage = switch (abilityLevel) {
            case 1 -> markedTarget.getHealth() * 0.3f; // 30%
            case 2 -> markedTarget.getHealth() * 0.4f; // 40%
            case 3 -> markedTarget.getHealth() * 0.5f; // 50%
            default -> markedTarget.getHealth() * 0.3f;
        };

        // Infligir daño usando el proyectil como fuente

        DamageSource source = markedTarget.getWorld().getDamageSources().indirectMagic(this.getOwner(), this);
        markedTarget.damage(source, damage);

        // Partículas de explosión
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.EXPLOSION,
                    markedTarget.getX(), markedTarget.getY() + 1, markedTarget.getZ(),
                    20,
                    0.5, 0.5, 0.5,
                    0.2
            );
        }

        // Sonido de explosión
        markedTarget.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1.0f, 1.5f);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.abilityLevel = nbt.getInt("AbilityLevel");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("AbilityLevel", this.abilityLevel);
    }
}
