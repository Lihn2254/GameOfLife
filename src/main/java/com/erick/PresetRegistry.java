package com.erick;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PresetRegistry {
    private static final Map<String, boolean[][]> PRESETS = new HashMap<>();

    static {
        // --- EXISTING ---
        PRESETS.put("glider", new boolean[][] {
                { false, true,  false },
                { false, false, true  },
                { true,  true,  true  }
        });

        PRESETS.put("blinker", new boolean[][] {
                { false, false, false },
                { true,  true,  true  },
                { false, false, false }
        });

        // --- OSCILLATORS ---
        
        // Toad (Period 2)
        PRESETS.put("toad", new boolean[][] {
                { false, false, false, false },
                { false, true,  true,  true  },
                { true,  true,  true,  false },
                { false, false, false, false }
        });

        // Beacon (Period 2)
        PRESETS.put("beacon", new boolean[][] {
                { true,  true,  false, false },
                { true,  true,  false, false },
                { false, false, true,  true  },
                { false, false, true,  true  }
        });

        // Pulsar (Period 3) - A larger, more complex oscillator
        PRESETS.put("pulsar", new boolean[][] {
                { false, false, true,  true,  true,  false, false, false, true,  true,  true,  false, false },
                { false, false, false, false, false, false, false, false, false, false, false, false, false },
                { true,  false, false, false, false, true,  false, true,  false, false, false, false, true  },
                { true,  false, false, false, false, true,  false, true,  false, false, false, false, true  },
                { true,  false, false, false, false, true,  false, true,  false, false, false, false, true  },
                { false, false, true,  true,  true,  false, false, false, true,  true,  true,  false, false },
                { false, false, false, false, false, false, false, false, false, false, false, false, false },
                { false, false, true,  true,  true,  false, false, false, true,  true,  true,  false, false },
                { true,  false, false, false, false, true,  false, true,  false, false, false, false, true  },
                { true,  false, false, false, false, true,  false, true,  false, false, false, false, true  },
                { true,  false, false, false, false, true,  false, true,  false, false, false, false, true  },
                { false, false, false, false, false, false, false, false, false, false, false, false, false },
                { false, false, true,  true,  true,  false, false, false, true,  true,  true,  false, false }
        });

        // --- SPACESHIPS ---

        // Lightweight Spaceship (LWSS)
        PRESETS.put("lwss", new boolean[][] {
                { false, true,  false, false, true  },
                { true,  false, false, false, false },
                { true,  false, false, false, true  },
                { true,  true,  true,  true,  false }
        });

        // Middleweight Spaceship (MWSS)
        PRESETS.put("mwss", new boolean[][] {
                { false, false, true,  false, false, false },
                { true,  false, false, false, true,  false },
                { false, false, false, false, false, true  },
                { true,  false, false, false, false, true  },
                { false, true,  true,  true,  true,  true  }
        });

        // --- STILL LIFES (Static patterns) ---

        // Block
        PRESETS.put("block", new boolean[][] {
                { true, true },
                { true, true }
        });

        // Beehive
        PRESETS.put("beehive", new boolean[][] {
                { false, true,  true,  false },
                { true,  false, false, true  },
                { false, true,  true,  false }
        });
        
        // Loaf
        PRESETS.put("loaf", new boolean[][] {
                { false, true,  true,  false },
                { true,  false, false, true  },
                { false, true,  false, true  },
                { false, false, true,  false }
        });

        // Glider gun
        PRESETS.put("glider gun", new boolean[][] {
            { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true,  false, false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true,  false, true,  false, false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false, false, false, true,  true,  false, false, false, false, false, false, true,  true,  false, false, false, false, false, false, false, false, false, false, false, false, true,  true  },
            { false, false, false, false, false, false, false, false, false, false, false, true,  false, false, false, true,  false, false, false, false, true,  true,  false, false, false, false, false, false, false, false, false, false, false, false, true,  true  },
            { true,  true,  false, false, false, false, false, false, false, false, true,  false, false, false, false, false, true,  false, false, false, true,  true,  false, false, false, false, false, false, false, false, false, false, false, false, false, false },
            { true,  true,  false, false, false, false, false, false, false, false, true,  false, false, false, true,  false, true,  true,  false, false, false, false, true,  false, true,  false, false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false, true,  false, false, false, false, false, true,  false, false, false, false, false, false, false, true,  false, false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false, false, true,  false, false, false, true,  false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false, false, false, true,  true,  false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false }
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
            for (int j = 0; j < original[i].length; j++) {
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