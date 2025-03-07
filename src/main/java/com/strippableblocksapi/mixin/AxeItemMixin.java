package com.strippableblocksapi.mixin;

import com.strippableblocksapi.StrippableBlocksAPI;
import com.strippableblocksapi.StrippableCustomRegistry;
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

				BlockEntity blockEntity = world.getBlockEntity(pos);
				try {
					// Use reflection to check for the class existence
					Class<?> pedestalClass = Class.forName("net.chris.pedestals.block.entity.PedestalBlockEntity");

					if (pedestalClass.isInstance(blockEntity)) {
						// If the block entity is a PedestalBlockEntity, perform the stripping logic
						Method getStoredItem = pedestalClass.getDeclaredMethod("getStoredItem");
						ItemStack currentItem = (ItemStack) getStoredItem.invoke(blockEntity);

						// Change the block state to the stripped version
						world.setBlockState(pos, strippedBlock.getDefaultState(), 3);

						// Retrieve the new block entity and set the item back
						BlockEntity newBlockEntity = world.getBlockEntity(pos);
						if (pedestalClass.isInstance(newBlockEntity)) {
							Method setStoredItem = pedestalClass.getDeclaredMethod("setStoredItem", ItemStack.class);
							setStoredItem.invoke(newBlockEntity, currentItem);
						}
					}
				} catch (ClassNotFoundException e) {
					// This should only happen if the Pedestals mod is not loaded
					StrippableBlocksAPI.LOGGER.warn("PedestalBlockEntity class not found. Ensure Pedestals mod is correctly loaded.");
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					StrippableBlocksAPI.LOGGER.error("Failed to interact with PedestalBlockEntity via reflection.", e);
				}
            } else {
				world.setBlockState(pos, strippedBlock.getDefaultState(), 3);
			}

			world.playSound(null, pos, getStripSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            cir.setReturnValue(ActionResult.SUCCESS);

		}
	}
}