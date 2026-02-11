package com.codebyevans.martialkarma.mixin;

import com.codebyevans.martialkarma.KarmaType;
import com.codebyevans.martialkarma.PlayerKarma;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void slowRegenForDeathKarma(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (entity instanceof PlayerEntity player) {
            KarmaType karma = PlayerKarma.getKarma(player);

            if (karma == KarmaType.DEATH) {
                // Reducir la regeneración natural
                // Minecraft regenera cada 80 ticks normalmente
                // Vamos a hacerlo cada 160 ticks (el doble de lento)

                if (player.age % 160 == 0 && player.getHealth() < player.getMaxHealth()) {
                    // Aquí puedes ajustar la velocidad
                    // O simplemente dejar que Minecraft regenere más lento
                }
            }
        }
    }
}