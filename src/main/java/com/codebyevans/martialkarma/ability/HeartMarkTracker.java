package com.codebyevans.martialkarma.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartMarkTracker {

    private static final Map<UUID, HeartMarkData> activeMarks = new HashMap<>();

    public static class HeartMarkData {
        public final UUID casterUUID;
        public final int level;
        public int ticksActive;

        public HeartMarkData(UUID casterUUID, int level) {
            this.casterUUID = casterUUID;
            this.level = level;
            this.ticksActive = 0;
        }

        public float getDamagePercent() {
            return switch (level) {
                case 1 -> 0.30f; // 30%
                case 2 -> 0.40f; // 40%
                case 3 -> 0.50f; // 50%
                default -> 0.30f;
            };
        }
    }

    public static void applyHeartMark(LivingEntity caster, LivingEntity target, int level) {
        activeMarks.put(target.getUuid(), new HeartMarkData(caster.getUuid(), level));
    }

    public static boolean hasHeartMark(LivingEntity target) {
        return activeMarks.containsKey(target.getUuid());
    }

    public static void tick(ServerWorld world) {
        activeMarks.entrySet().removeIf(entry -> {
            UUID targetUUID = entry.getKey();
            HeartMarkData data = entry.getValue();

            LivingEntity target = (LivingEntity) world.getEntity(targetUUID);
            LivingEntity caster = (LivingEntity) world.getEntity(data.casterUUID);

            // Remover si el objetivo o el caster no existen
            if (target == null || !target.isAlive() || caster == null) {
                return true;
            }

            data.ticksActive++;

            // Partículas cada tick
            if (data.ticksActive % 5 == 0) {
                world.spawnParticles(
                        ParticleTypes.HEART,
                        target.getX(),
                        target.getY() + target.getHeight() / 2,
                        target.getZ(),
                        1,
                        0.2, 0.3, 0.2,
                        0.02
                );
            }

            // Sonido de latido cada segundo
            if (data.ticksActive % 20 == 0) {
                target.playSound(SoundEvents.ENTITY_WARDEN_HEARTBEAT, 0.3f, 1.5f);
            }

            // Detonar después de 3 segundos (60 ticks)
            if (data.ticksActive >= 60) {
                detonateHeartMark(target, caster, data);
                return true; // Remover la marca
            }

            return false;
        });
    }

    private static void detonateHeartMark(LivingEntity target, LivingEntity caster, HeartMarkData data) {
        float targetCurrentHealth = target.getHealth();
        float damagePercent = data.getDamagePercent();

        // Calcular daño (% de vida actual del objetivo)
        float damage = targetCurrentHealth * damagePercent;

        // Aplicar daño al objetivo
        target.damage(target.getWorld().getDamageSources().magic(), damage);

        // Efectos visuales de explosión
        if (target.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.HEART,
                    target.getX(),
                    target.getY() + 1,
                    target.getZ(),
                    30,
                    0.5, 0.8, 0.5,
                    0.2
            );

            serverWorld.spawnParticles(
                    ParticleTypes.EXPLOSION,
                    target.getX(),
                    target.getY() + 1,
                    target.getZ(),
                    1,
                    0, 0, 0,
                    0
            );

            serverWorld.spawnParticles(
                    ParticleTypes.DAMAGE_INDICATOR,
                    target.getX(),
                    target.getY() + 1,
                    target.getZ(),
                    20,
                    0.5, 0.8, 0.5,
                    0.2
            );
        }

        // Sonido de explosión
        target.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 0.8f, 1.2f);
        target.playSound(SoundEvents.ENTITY_WITHER_HURT, 1.0f, 0.8f);
    }
}