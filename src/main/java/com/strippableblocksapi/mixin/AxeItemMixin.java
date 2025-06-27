package com.strippableblocksapi.mixin;

import com.strippableblocksapi.StrippableCustomRegistry;
import com.strippableblocksapi.inventory.InventoryPreservingBlock;
import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(AxeItem.class)
public class AxeItemMixin {

	@Unique
	public SoundEvent getStripSound() {
		return SoundEvents.ITEM_AXE_STRIP;
	}


	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void onUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {

		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Block block = world.getBlockState(pos).getBlock();

		Block strippedBlock = StrippableCustomRegistry.getStrippedResult(block);

		if (strippedBlock == null) return;

        world.playSound(null, pos, getStripSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        context.getStack().damage(1, context.getPlayer());

		if (block instanceof InventoryPreservingBlock inventoryPreservingBlock) {
			inventoryPreservingBlock.onStripped(world, pos, strippedBlock.getDefaultState());
			if (world.getBlockState(pos).isOf(strippedBlock)) {
				world.setBlockState(pos, strippedBlock.getDefaultState(), 3);
			}
			cir.setReturnValue(ActionResult.SUCCESS);
			return;
		}

        world.setBlockState(pos, strippedBlock.getDefaultState(), 3);
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}