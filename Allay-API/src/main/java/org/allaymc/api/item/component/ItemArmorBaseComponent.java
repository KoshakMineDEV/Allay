package org.allaymc.api.item.component;

/**
 * Allay Project 2024/8/10
 *
 * @author daoge_cmd
 */
public interface ItemArmorBaseComponent extends ItemBaseComponent {

    ArmorType getArmorType();

    enum ArmorType {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }
}
