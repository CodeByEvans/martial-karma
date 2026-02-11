package com.codebyevans.martialkarma.ai;

import com.codebyevans.martialkarma.KarmaType;
import com.codebyevans.martialkarma.PlayerKarma;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

public class KarmaAwareTargetGoal extends ActiveTargetGoal<PlayerEntity> {

    public KarmaAwareTargetGoal(MobEntity mob) {
        super(mob, PlayerEntity.class, true);
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) {
            return false;
        }

        if (this.targetEntity instanceof PlayerEntity player) {
            KarmaType karma = PlayerKarma.getKarma(player);
            if (karma == KarmaType.DEATH) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (!super.shouldContinue()) {
            return false;
        }

        if (this.targetEntity instanceof PlayerEntity player) {
            KarmaType karma = PlayerKarma.getKarma(player);
            if (karma == KarmaType.DEATH) {
                this.mob.setTarget(null);
                return false;
            }
        }

        return true;
    }
}