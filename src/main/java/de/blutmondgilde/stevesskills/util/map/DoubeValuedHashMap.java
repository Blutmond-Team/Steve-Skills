package de.blutmondgilde.stevesskills.util.map;

import com.mojang.datafixers.util.Pair;

import java.util.HashMap;

public class DoubeValuedHashMap<K, V1, V2> extends HashMap<K, Pair<V1, V2>> implements DoubleValuedMap<K, V1, V2> {
}
