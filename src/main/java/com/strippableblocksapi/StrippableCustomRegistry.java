package com.strippableblocksapi;

import net.minecraft.block.*;

import java.util.HashMap;
import java.util.Map;

public class StrippableCustomRegistry {

    private static final Map<Block, Block> STRIPPABLES = new HashMap<>();

    public static void register(Block input, Block output){
        STRIPPABLES.put(input, output);
    }

    public static Block getStrippedResult(Block input){
        return STRIPPABLES.get(input);
    }

}
