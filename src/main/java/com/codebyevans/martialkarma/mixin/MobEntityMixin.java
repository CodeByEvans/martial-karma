package com.codebyevans.martialkarma.mixin;

import com.codebyevans.martialkarma.KarmaType;
import com.codebyevans.martialkarma.PlayerKarma;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void preventDeathKarmaTargeting(LivingEntity target, CallbackInfo ci) {
        if (target instanceof PlayerEntity player) {
            KarmaType karma = PlayerKarma.getKarma(player);
            if (karma == KarmaType.DEATH) {
                ci.cancel(); // Cancela establecer al jugador como objetivo
            }
        }
    }
}