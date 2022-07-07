package com.spiritlight.easyitemnbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringUtils {

    /**
     * Useful method to split a long string into multiple small ones.
     *
     * @param message The string to split
     * @param limit The maximum allowed size for each split string
     * @param priority_delimiters Array of characters (by order) to take priority when splitting
     * @return A list of split strings.
     */

    public static @NotNull List<String> cutString(@Nonnull String message, @Range(from = 1, to = Integer.MAX_VALUE) int limit, char[] priority_delimiters) {
        final List<String> ret = new ArrayList<>();
        String t = message;
        if(t.length() < limit && !t.equals("")) {
            return Collections.singletonList(message);
        }
        while(t.length() > limit) {
            int temp = limit;
            int[] in = {-1, -1, -1};
            String tmp = t.substring(0, limit);
            for(int x = 0; x< priority_delimiters.length; x++) {
                in[x] = tmp.lastIndexOf(priority_delimiters[x]);
            }
            for (int i : in) {
                if (i > 0) {
                    temp = i;
                    break;
                }
            }
            ret.add(t.substring(0, temp+1));
            t = t.substring(temp+1).trim();
        }
        ret.add(t);
        return ret;
    }
}
