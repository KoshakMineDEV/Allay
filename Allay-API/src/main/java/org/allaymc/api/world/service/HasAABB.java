package org.allaymc.api.world.service;

import org.joml.primitives.AABBf;

/**
 * Allay Project 2023/7/30
 *
 * @author daoge_cmd
 */
public interface HasAABB {
    AABBf copyOffsetAABBTo(AABBf dest);
}
