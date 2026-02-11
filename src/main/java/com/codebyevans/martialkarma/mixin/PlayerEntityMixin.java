package com.codebyevans.martialkarma.mixin;

import com.codebyevans.martialkarma.PlayerKarma;
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

    // Inicializar el DataTracker
    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initKarmaData(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(PlayerKarma.KARMA_TYPE, 0);
    }

    // Guardar el karma en NBT
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void saveKarmaData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        int karma = player.getDataTracker().get(PlayerKarma.KARMA_TYPE);
        nbt.putInt("MartialKarma", karma);
    }

    // Cargar el karma desde NBT
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void loadKarmaData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (nbt.contains("MartialKarma")) {
            int karma = nbt.getInt("MartialKarma");
            player.getDataTracker().set(PlayerKarma.KARMA_TYPE, karma);
        }
    }
}