package com.codebyevans.martialkarma.entity;

import com.codebyevans.martialkarma.MartialKarma;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<HeartMarkEntity> HEART_MARK = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of("martialkarma", "heart_mark"),
            FabricEntityTypeBuilder.<HeartMarkEntity>create(SpawnGroup.MISC, HeartMarkEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void register() {
        MartialKarma.LOGGER.info("Registrando entidades para Martial Karma");
    }
}