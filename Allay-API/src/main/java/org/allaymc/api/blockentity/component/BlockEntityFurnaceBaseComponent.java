package org.allaymc.api.blockentity.component;

import org.allaymc.api.block.type.BlockType;

/**
 * Allay Project 2024/8/11
 *
 * @author daoge_cmd
 */
public interface BlockEntityFurnaceBaseComponent extends BlockEntityBaseComponent {
    int getBurnTime();

    int getCookTime();

    int getBurnDuration();

    float getStoredXP();

    void setStoredXP(float storedXP);

    BlockType<?> getUnlitBlockType();

    BlockType<?> getLitBlockType();

    void setLit(boolean lit);

    String getFurnaceRecipeTag();

    float getNormalSpeed();

    float getIdealSpeed();

    float getCurrentSpeed();
}
