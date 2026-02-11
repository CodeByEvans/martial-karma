package com.codebyevans.martialkarma.mixin;

import com.codebyevans.martialkarma.KarmaType;
import com.codebyevans.martialkarma.PlayerKarma;
import com.codebyevans.martialkarma.ability.AbilityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initKarmaData(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(PlayerKarma.KARMA_TYPE, 0);
        builder.add(PlayerKarma.KARMA_POINTS, 100);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void saveKarmaData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        int karmaType = player.getDataTracker().get(PlayerKarma.KARMA_TYPE);
        int karmaPoints = player.getDataTracker().get(PlayerKarma.KARMA_POINTS);

        nbt.putInt("MartialKarma", karmaType);
        nbt.putInt("MartialKarmaPoints", karmaPoints);

        // Guardar AbilityData
        NbtCompound abilityNbt = new NbtCompound();
        PlayerKarma.getAbilityData(player).writeNbt(abilityNbt);
        nbt.put("MartialKarmaAbilities", abilityNbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void loadKarmaData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        if (nbt.contains("MartialKarma")) {
            int karma = nbt.getInt("MartialKarma");
            player.getDataTracker().set(PlayerKarma.KARMA_TYPE, karma);
        }

        if (nbt.contains("MartialKarmaPoints")) {
            int points = nbt.getInt("MartialKarmaPoints");
            player.getDataTracker().set(PlayerKarma.KARMA_POINTS, points);
        }

        // Cargar AbilityData
        if (nbt.contains("MartialKarmaAbilities")) {
            NbtCompound abilityNbt = nbt.getCompound("MartialKarmaAbilities");
            PlayerKarma.getAbilityData(player).readNbt(abilityNbt);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickKarmaRegeneration(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;

        // Regeneración de Karma
        int currentKarma = PlayerKarma.getKarmaPoints(player);
        int maxKarma = PlayerKarma.getMaxKarmaPoints(player);

        if (currentKarma < maxKarma) {
            if (player.age % 20 == 0) {
                PlayerKarma.addKarmaPoints(player, 5);
            }
        }

        // Regeneración lenta para DEATH karma
        KarmaType karma = PlayerKarma.getKarma(player);

        if (karma == KarmaType.DEATH) {
            if (player.age % 80 == 0) {
                float currentSaturation = player.getHungerManager().getSaturationLevel();
                if (currentSaturation > 0) {
                    player.getHungerManager().setSaturationLevel(currentSaturation * 0.5f);
                }
            }
        }
    }
}