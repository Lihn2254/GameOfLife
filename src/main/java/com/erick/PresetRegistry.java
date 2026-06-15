package com.erick;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PresetRegistry {
    private static final Map<String, boolean[][]> PRESETS = new HashMap<>();

    static {
        PRESETS.put("Glider", new boolean[][] {
                { false, true, false },
                { false, false, true },
                { true, true, true }
        });

        PRESETS.put("Blinker", new boolean[][] {
                { false, false, false },
                { true, true, true },
                { false, false, false }
        });

        PRESETS.put("Empty", new boolean[][] {
                { false, false },
                { false, false }
        });
    }

    public static boolean[][] getPreset(String name) {
        boolean[][] original = PRESETS.get(name);
        if (original == null) {
            throw new IllegalArgumentException("Preset not found: " + name);
        }

        boolean[][] copy = new boolean[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }

        return copy;
    }

    public static int getPresetSize(String name) {
        int size = 0;
        boolean[][] original = PRESETS.get(name);

        if (original == null) {
            throw new IllegalArgumentException("Preset not found: " + name);
        }

        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original.length; j++) {
                if (original[i][j]) {
                    size++;
                }
            }
        }

        return size;
    }

    public static Set<String> getPresetNames() {
        return PRESETS.keySet();
    }
}