package cn.allay.server.entity.type;

import cn.allay.api.data.VanillaEntityTypes;
import cn.allay.api.entity.impl.EntitySheep;
import cn.allay.api.entity.type.EntityInitInfo;
import cn.allay.api.math.location.Location3f;
import cn.allay.api.world.World;
import cn.allay.testutils.AllayTestExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Allay Project 2023/5/26
 *
 * @author daoge_cmd
 */
@ExtendWith(AllayTestExtension.class)
class AllayEntityTypeTest {
    static EntitySheep sheep;
    static World mockWorld = Mockito.mock(World.class);

    @BeforeAll
    static void init() {
        sheep = VanillaEntityTypes.SHEEP_TYPE.createEntity(new EntityInitInfo.Simple(new Location3f(0f, 1f, 2f, 0, 0, 0, mockWorld)));
    }

    @Test
    void testCommon() {
        assertEquals(new Location3f(0f, 1f, 2f, 0, 0, 0, mockWorld), sheep.getLocation());
    }
}