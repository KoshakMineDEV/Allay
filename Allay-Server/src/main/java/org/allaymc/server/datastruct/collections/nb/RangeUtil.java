package org.allaymc.server.datastruct.collections.nb;

import lombok.experimental.UtilityClass;

/**
 * Allay Project 2023/7/8
 *
 * @author SuperIceCN
 */
@UtilityClass
public final class RangeUtil {
    public static int checkPositiveOrZero(int n, String name) {
        if (n < 0) {
            throw new IllegalArgumentException(name + ": " + n + " (expected: >= 0)");
        }
        return n;
    }
}
