package cn.allay.api.item.enchantment;

import cn.allay.api.identifier.Identifier;

/**
 * Allay Project 2023/10/21
 *
 * @author daoge_cmd
 */
public interface EnchantmentType {

    Identifier getIdentifier();

    short getId();

    short getMaxLevel();

    Rarity getRarity();

    EnchantmentInstance createInstance(short level);

    default boolean checkCompatibility(EnchantmentType other) {
        return true;
    }
}
