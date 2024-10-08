package org.allaymc.api.utils;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;
import org.allaymc.api.block.data.BlockId;
import org.allaymc.api.block.tag.BlockTag;
import org.allaymc.api.block.type.BlockState;
import org.allaymc.api.item.ItemStack;
import org.allaymc.api.item.data.ItemId;
import org.allaymc.api.item.tag.ItemTag;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Allay Project 2023/6/23
 *
 * @author daoge_cmd
 */
@UtilityClass
public class Utils {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final BlockState[][][] EMPTY_BLOCK_STATE_ARRAY_3D = new BlockState[0][0][0];
    public static final BlockTag[] EMPTY_BLOCK_TAG_ARRAY = new BlockTag[0];
    public static final ItemTag[] EMPTY_ITEM_TAG_ARRAY = new ItemTag[0];
    public static final ItemId[] EMPTY_VANILLA_ITEM_ID_ARRAY = new ItemId[0];
    public static final BlockId[] EMPTY_VANILLA_BLOCK_ID_ARRAY = new BlockId[0];
    public static final ItemStack[] EMPTY_ITEM_STACK_ARRAY = new ItemStack[0];
    public static final Set<ItemStack> EMPTY_ITEM_STACK_SET = Set.of();

    public byte[] appendBytes(byte[] bytes1, byte[]... bytes2) {
        int length = bytes1.length;
        for (byte[] bytes : bytes2) {
            length += bytes.length;
        }

        byte[] appendedBytes = new byte[length];
        System.arraycopy(bytes1, 0, appendedBytes, 0, bytes1.length);
        int index = bytes1.length;

        for (byte[] b : bytes2) {
            System.arraycopy(b, 0, appendedBytes, index, b.length);
            index += b.length;
        }
        return appendedBytes;
    }

    /**
     * Calculates the number of bit that the specified value convert to binary
     *
     * @param value the value
     *
     * @return the bits
     */
    public byte computeRequiredBits(int value) {
        Preconditions.checkArgument(value >= 0);
        if (value <= 1) return 1;
        byte bits = 1;
        while (value >= (1 << bits)) {
            bits++;
        }
        return bits;
    }

    public byte[] convertByteBuf2Array(ByteBuf buf) {
        byte[] payload = new byte[buf.readableBytes()];
        buf.readBytes(payload);
        return payload;
    }

    public String[] objectArrayToStringArray(Object[] objectArray) {
        String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {
            stringArray[i] = objectArray[i].toString();
        }
        return stringArray;
    }

    public static String readString(InputStream inputStream) throws IOException {
        return readString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public static String readString(Reader reader) throws IOException {
        try (BufferedReader br = new BufferedReader(reader)) {
            String temp;
            StringBuilder stringBuilder = new StringBuilder();
            temp = br.readLine();
            while (temp != null) {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append("\n");
                }
                stringBuilder.append(temp);
                temp = br.readLine();
            }
            return stringBuilder.toString();
        }
    }

    public static InputStream getResource(String resourceName) {
        return Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(resourceName));
    }
}
