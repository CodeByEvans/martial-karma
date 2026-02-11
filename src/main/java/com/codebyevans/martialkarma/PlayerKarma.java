package com.codebyevans.martialkarma;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerKarma {
    // Registramos la varialbe para que Minecraft la sincronice
    public static final TrackedData<Integer> KARMA_TYPE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    //Metodo para asignar Karma
    public static void setKarmaType (PlayerEntity player, KarmaType type) {
        try {
            player.getDataTracker().set(KARMA_TYPE, type.ordinal());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para leer Karma
    public static KarmaType getKarma(PlayerEntity player) {
        try {
            int karmaOrdinal = player.getDataTracker().get(KARMA_TYPE);

            // Validar que el índice esté dentro del rango
            if (karmaOrdinal >= 0 && karmaOrdinal < KarmaType.values().length) {
                return KarmaType.values()[karmaOrdinal];
            } else {
                System.out.println("Valor de karma fuera de rango: " + karmaOrdinal);
                return KarmaType.values()[0]; // Retorna el primer valor del enum como default
            }
        } catch (Exception e) {
            System.out.println("Error al obtener karma: " + e.getMessage());
            return KarmaType.values()[0]; // Retorna el primer valor del enum como default
        }
    }
}
