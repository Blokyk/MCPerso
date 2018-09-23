package net.minecraft.client.settings;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreativeSettings
{
    private static final Logger LOGGER = LogManager.getLogger();
    protected Minecraft mcInstance;
    private final File hotbarFile;
    private final HotbarSnapshot[] hotbarSnapshotArray = new HotbarSnapshot[9];

    public CreativeSettings(Minecraft mcInstance, File hotbarnbtParentFile)
    {
        this.mcInstance = mcInstance;
        this.hotbarFile = new File(hotbarnbtParentFile, "hotbar.nbt");

        for (int i = 0; i < 9; ++i)
        {
            this.hotbarSnapshotArray[i] = new HotbarSnapshot();
        }

        this.func_192562_a();
    }

    
    /**
     * Probably set the hotbar items based on the hotbar data of the player stored in the world file
     */
    public void func_192562_a()
    {
        try
        {
            NBTTagCompound hotbarData = CompressedStreamTools.read(this.hotbarFile);

            if (hotbarData == null)
            {
                return;
            }

            for (int i = 0; i < 9; ++i)
            {
                this.hotbarSnapshotArray[i].setAllItem(hotbarData.getTagList(String.valueOf(i), 10));
            }
        }
        catch (Exception exception)
        {
            LOGGER.error("Failed to load creative mode options", (Throwable)exception);
        }
    }

    public void func_192564_b()
    {
        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            for (int i = 0; i < 9; ++i)
            {
                nbttagcompound.setTag(String.valueOf(i), this.hotbarSnapshotArray[i].func_192834_a());
            }

            CompressedStreamTools.write(nbttagcompound, this.hotbarFile);
        }
        catch (Exception exception)
        {
            LOGGER.error("Failed to save creative mode options", (Throwable)exception);
        }
    }

    /**
     * Returns the saved hotbar (HotbarSnapshot) at the given index
     * @param index
     */
    public HotbarSnapshot getHotbarAt(int index)
    {
        return this.hotbarSnapshotArray[index];
    }
}
