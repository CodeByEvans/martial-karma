package com.codebyevans.martialkarma.ability;

import net.minecraft.nbt.NbtCompound;

public class AbilityData {
    private int heartMarkLevel = 0; // 0-3
    private long lastHeartMarkUse = 0;

    public int getHeartMarkLevel() {
        return heartMarkLevel;
    }

    public void setHeartMarkLevel(int level) {
        this.heartMarkLevel = Math.max(0, Math.min(3, level));
    }

    public void levelUpHeartMark() {
        if (heartMarkLevel < 3) {
            heartMarkLevel++;
        }
    }

    public long getLastHeartMarkUse() {
        return lastHeartMarkUse;
    }

    public void setLastHeartMarkUse(long time) {
        this.lastHeartMarkUse = time;
    }

    public int getCooldownSeconds() {
        // 60 segundos en nivel 1, 52.5 en nivel 2, 45 en nivel 3
        return switch (heartMarkLevel) {
            case 1 -> 60;
            case 2 -> 52;
            case 3 -> 45;
            default -> 60;
        };
    }

    public boolean isOnCooldown(long currentTime) {
        long cooldownMs = getCooldownSeconds() * 1000L;
        return (currentTime - lastHeartMarkUse) < cooldownMs;
    }

    public int getRemainingCooldown(long currentTime) {
        long cooldownMs = getCooldownSeconds() * 1000L;
        long elapsed = currentTime - lastHeartMarkUse;
        return (int) Math.max(0, (cooldownMs - elapsed) / 1000);
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("HeartMarkLevel", heartMarkLevel);
        nbt.putLong("LastHeartMarkUse", lastHeartMarkUse);
    }

    public void readNbt(NbtCompound nbt) {
        heartMarkLevel = nbt.getInt("HeartMarkLevel");
        lastHeartMarkUse = nbt.getLong("LastHeartMarkUse");
    }
}