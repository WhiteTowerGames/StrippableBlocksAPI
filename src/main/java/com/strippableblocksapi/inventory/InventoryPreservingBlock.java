package com.strippableblocksapi.inventory;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface InventoryPreservingBlock {
    void onStripped(World world, BlockPos blockPos, BlockState newState);
}
