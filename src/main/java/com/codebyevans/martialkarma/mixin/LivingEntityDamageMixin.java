package com.codebyevans.martialkarma.mixin;

import com.codebyevans.martialkarma.KarmaType;
import com.codebyevans.martialkarma.PlayerKarma;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityDamageMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamageDealt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            Entity attacker = source.getAttacker();

            if (attacker instanceof PlayerEntity player) {
                KarmaType karma = PlayerKarma.getKarma(player);

                if (karma == KarmaType.DEATH) {
                    float healAmount = amount * 0.25f;

                    float currentHealth = player.getHealth();
                    float maxHealth = player.getMaxHealth();

                    if (currentHealth < maxHealth) {
                        // Curar
                        player.heal(healAmount);

                        // EFECTO DE REGENERACIÓN para hacer que los corazones brillen
                        player.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.REGENERATION,
                                10, // duración MUY corta (2 ticks = 0.1 segundos)
                                0, //
                                false, // sin partículas ambientales
                                false, // sin mostrar en HUD
                                false  // sin mostrar icono
                        ));

                    }
                }
            }
        }
    }
}