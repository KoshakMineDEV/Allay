package cn.allay.api.block.interfaces.concretepowder;

import cn.allay.api.block.BlockBehavior;
import cn.allay.api.block.type.BlockType;
import cn.allay.api.block.type.BlockTypeBuilder;
import cn.allay.api.data.VanillaBlockId;

/**
 * @author daoge_cmd | Cool_Loong <br>
 * Allay Project <br>
 */
public interface BlockMagentaConcretePowderBehavior extends BlockBehavior {
  BlockType<BlockMagentaConcretePowderBehavior> MAGENTA_CONCRETE_POWDER_TYPE = BlockTypeBuilder
          .builder(BlockMagentaConcretePowderBehavior.class)
          .vanillaBlock(VanillaBlockId.MAGENTA_CONCRETE_POWDER)
          .build();
}
