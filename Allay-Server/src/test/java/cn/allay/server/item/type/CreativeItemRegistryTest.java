package cn.allay.server.item.type;

import cn.allay.api.item.type.CreativeItemRegistry;
import cn.allay.testutils.AllayTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Allay Project 2023/7/22
 *
 * @author daoge_cmd
 */
@ExtendWith(AllayTestExtension.class)
public class CreativeItemRegistryTest {
    @Test
    void testCreativeItemRegistry() {
        assertFalse(CreativeItemRegistry.getRegistry().getContent().isEmpty());
    }
}
