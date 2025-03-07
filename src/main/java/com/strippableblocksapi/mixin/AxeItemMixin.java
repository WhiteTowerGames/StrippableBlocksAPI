package com.strippableblocksapi.mixin;

import com.strippableblocksapi.StrippableBlocksAPI;
import com.strippableblocksapi.StrippableCustomRegistry;
import net.chris.pedestals.block.entity.PedestalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(AxeItem.class)
public class AxeItemMixin {

	@Unique
	public SoundEvent getStripSound() {return SoundEvents.ITEM_AXE_STRIP;}


	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void onUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {

		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Block block = world.getBlockState(pos).getBlock();

		Block strippedBlock = StrippableCustomRegistry.getStrippedResult(block);

		if (strippedBlock != null){


            if (StrippableBlocksAPI.isPedestalsLoaded) {

				PedestalBlockEntity pedestalBlockEntity = (PedestalBlockEntity) world.getBlockEntity(pos);
                assert pedestalBlockEntity != null;
                ItemStack stack = pedestalBlockEntity.getStoredItem();
				world.setBlockState(pos, strippedBlock.getDefaultState(), 3);
				pedestalBlockEntity = (PedestalBlockEntity) world.getBlockEntity(pos);
                assert pedestalBlockEntity != null;
                pedestalBlockEntity.setStoredItem(stack);

            } else {
				world.setBlockState(pos, strippedBlock.getDefaultState(), 3);
			}

			world.playSound(null, pos, getStripSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            cir.setReturnValue(ActionResult.SUCCESS);

		}
	}
}