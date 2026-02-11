package com.codebyevans.martialkarma;

import com.codebyevans.martialkarma.ability.AbilityData;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerKarma {
    // Karma Type (el tipo de karma)
    public static final TrackedData<Integer> KARMA_TYPE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    // Karma Points (la "barra de maná")
    public static final TrackedData<Integer> KARMA_POINTS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final Map<UUID, AbilityData> abilityDataMap = new HashMap<>();

    // Método para asignar Karma Type
    public static void setKarmaType(PlayerEntity player, KarmaType type) {
        try {
            player.getDataTracker().set(KARMA_TYPE, type.ordinal());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para leer Karma Type
    public static KarmaType getKarma(PlayerEntity player) {
        try {
            int karmaOrdinal = player.getDataTracker().get(KARMA_TYPE);

            if (karmaOrdinal >= 0 && karmaOrdinal < KarmaType.values().length) {
                return KarmaType.values()[karmaOrdinal];
            } else {
                System.out.println("Valor de karma fuera de rango: " + karmaOrdinal);
                return KarmaType.values()[0];
            }
        } catch (Exception e) {
            System.out.println("Error al obtener karma: " + e.getMessage());
            return KarmaType.values()[0];
        }
    }

    // NUEVOS MÉTODOS para Karma Points

    // Establecer puntos de karma
    public static void setKarmaPoints(PlayerEntity player, int points) {
        // Limitar entre 0 y 100 (o el máximo que quieras)
        int clampedPoints = Math.max(0, Math.min(points, 100));
        player.getDataTracker().set(KARMA_POINTS, clampedPoints);
    }

    // Obtener puntos de karma
    public static int getKarmaPoints(PlayerEntity player) {
        try {
            return player.getDataTracker().get(KARMA_POINTS);
        } catch (Exception e) {
            return 0;
        }
    }

    // Agregar puntos de karma
    public static void addKarmaPoints(PlayerEntity player, int amount) {
        int current = getKarmaPoints(player);
        setKarmaPoints(player, current + amount);
    }

    // Consumir puntos de karma (retorna true si había suficientes)
    public static boolean consumeKarmaPoints(PlayerEntity player, int amount) {
        int current = getKarmaPoints(player);
        if (current >= amount) {
            setKarmaPoints(player, current - amount);
            return true;
        }
        return false;
    }

    // Obtener el máximo de karma points
    public static int getMaxKarmaPoints(PlayerEntity player) {
        return 100; // Puedes cambiar esto o hacer  lo dinámico según el nivel
    }

    // Métodos para AbilityData
    public static AbilityData getAbilityData(PlayerEntity player) {
        return abilityDataMap.computeIfAbsent(player.getUuid(), uuid -> new AbilityData());
    }
}