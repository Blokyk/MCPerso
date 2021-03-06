package net.minecraft.client.settings;

import java.util.ArrayList;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * A class describing a hotbar snapshot (also know as saved toolbar)
 *
 */
public class HotbarSnapshot extends ArrayList<ItemStack>
{
    public static final int hotbarSize = InventoryPlayer.getHotbarSize();

    public HotbarSnapshot()
    {
        this.ensureCapacity(hotbarSize);

        for (int i = 0; i < hotbarSize; ++i)
        {
            this.add(ItemStack.nullItemStack);
        }
    }

    
    /**
     * Returns a NBT tag list, the size of the hotbar (, with clean NBT tags ?)
     * @return
     */
    public NBTTagList func_192834_a()
    {
        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < hotbarSize; ++i)
        {
            tagList.appendTag(((ItemStack)this.get(i)).writeToNBT(new NBTTagCompound())); // Gets the ItemStack at the index i, then return an NBT Tag and append it to the taglist (output)
        }

        return tagList;
    }

    /**
     * Set all items in the hotbar from the nbtTagList parameter
	 *
     */
    public void setAllItem(NBTTagList nbtTagList)
    {
        for (int i = 0; i < hotbarSize; ++i)
        {
            this.set(i, new ItemStack(nbtTagList.getCompoundTagAt(i)));
        }
    }

    public boolean isEmpty()
    {
        for (int i = 0; i < hotbarSize; ++i)
        {
            if (!((ItemStack)this.get(i)).isNull())
            {
                return false;
            }
        }

        return true;
    }
}
