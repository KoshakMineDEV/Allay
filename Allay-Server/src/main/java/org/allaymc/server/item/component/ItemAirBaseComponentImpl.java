package org.allaymc.server.item.component;

import org.allaymc.api.item.initinfo.ItemStackInitInfo;
import org.allaymc.api.item.initinfo.SimpleItemStackInitInfo;
import org.allaymc.server.component.annotation.OnInitFinish;

import static org.allaymc.api.item.type.ItemTypes.AIR;

/**
 * Allay Project 2023/12/6
 *
 * @author daoge_cmd
 */
public class ItemAirBaseComponentImpl extends ItemBaseComponentImpl {

    public static SimpleItemStackInitInfo AIR_TYPE_INIT_INFO =
            SimpleItemStackInitInfo
                    .builder()
                    .autoAssignStackNetworkId(false)
                    .build();

    static {
        AIR_TYPE_INIT_INFO.setItemType(AIR);
    }

    public ItemAirBaseComponentImpl() {
        super(AIR_TYPE_INIT_INFO);
    }

    @Override
    @OnInitFinish
    public void onInitFinish(ItemStackInitInfo initInfo) {
        // Do nothing here
    }
}
