package org.allaymc.server.item.component;

import org.allaymc.api.block.dto.PlayerInteractInfo;
import org.allaymc.api.item.initinfo.ItemStackInitInfo;
import org.allaymc.api.world.Dimension;
import org.joml.Vector3ic;

import static org.allaymc.api.block.type.BlockTypes.CAULDRON;

/**
 * Allay Project 2023/12/6
 *
 * @author daoge_cmd
 */
public class ItemCauldronBaseComponentImpl extends ItemBaseComponentImpl {
    public ItemCauldronBaseComponentImpl(ItemStackInitInfo initInfo) {
        super(initInfo);
    }

    @Override
    public boolean placeBlock(Dimension dimension, Vector3ic placeBlockPos, PlayerInteractInfo placementInfo) {
        return tryPlaceBlockState(dimension, CAULDRON.getDefaultState(), placeBlockPos, placementInfo);
    }
}
