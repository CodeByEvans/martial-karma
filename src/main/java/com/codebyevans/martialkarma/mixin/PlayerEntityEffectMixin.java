package com.codebyevans.martialkarma.mixin;

import com.codebyevans.martialkarma.KarmaType;
import com.codebyevans.martialkarma.PlayerKarma;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityEffectMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void removeNegativeEffects(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        KarmaType karma = PlayerKarma.getKarma(player);

        if (karma == KarmaType.DEATH) {
            // Remover Hambre, Veneno y Wither cada tick
            if (player.hasStatusEffect(StatusEffects.HUNGER)) {
                player.removeStatusEffect(StatusEffects.HUNGER);
            }
            if (player.hasStatusEffect(StatusEffects.POISON)) {
                player.removeStatusEffect(StatusEffects.POISON);
            }
            if (player.hasStatusEffect(StatusEffects.WITHER)) {
                player.removeStatusEffect(StatusEffects.WITHER);
            }
        }
    }
}